package com.honorarios.honorarios_api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// Defesas contra abuso/DoS, antes do processamento pesado (BCrypt, desserializacao):
//  1) limite de tamanho de corpo -> evita OOM por body gigante;
//  2) rate limit por IP nos /api/auth/** -> evita brute-force, spam de cadastro e
//     exaustao das threads do Tomcat (cada login custa ~300ms de BCrypt).
public class AbuseGuardFilter extends OncePerRequestFilter {

    private static final long MAX_BODY_BYTES = 64 * 1024;          // JSONs da app sao pequenos
    private static final int AUTH_CAPACITY = 10;                   // ate 10 tentativas em rajada...
    private static final long AUTH_REFILL_NANOS = 6_000_000_000L;  // ...recupera 1 a cada 6s (~10/min)
    private static final int MAX_BUCKETS = 50_000;                 // teto de memoria do mapa
    private static final long IDLE_NANOS = 600_000_000_000L;       // 10 min sem uso -> elegivel a limpeza

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        // ponytail: cobre Content-Length (axios/browser sempre mandam). Requisicao chunked sem
        // Content-Length passa por aqui; teto real seria envolver o InputStream num contador.
        if (req.getContentLengthLong() > MAX_BODY_BYTES) {
            res.sendError(HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE, "Corpo grande demais");
            return;
        }

        if (req.getRequestURI().startsWith("/api/auth/") && !allow(clientIp(req))) {
            res.setHeader("Retry-After", "60");
            res.sendError(429, "Muitas tentativas. Tente novamente em instantes.");
            return;
        }

        chain.doFilter(req, res);
    }

    private boolean allow(String ip) {
        long now = System.nanoTime();
        // teto de memoria: sob flood de IPs distintos (XFF forjado), limpa buckets ociosos.
        if (buckets.size() > MAX_BUCKETS) {
            buckets.values().removeIf(b -> b.idle(now - IDLE_NANOS));
        }
        return buckets.computeIfAbsent(ip, k -> new Bucket(now)).tryConsume(now);
    }

    // ponytail: XFF e spoofavel por atacante determinado, mas sobe muito a barra vs nada e barra
    // o script ingenuo. Defesa contra ataque distribuido real fica no proxy/WAF, fora da app.
    private static String clientIp(HttpServletRequest req) {
        String xff = req.getHeader("X-Forwarded-For");
        if (xff != null && !xff.isBlank()) return xff.split(",")[0].trim();
        return req.getRemoteAddr();
    }

    // Token bucket em memoria. ponytail: instancia unica (1 replica no Railway); se escalar
    // horizontal, migrar pra Redis/Bucket4j.
    private static final class Bucket {
        private double tokens = AUTH_CAPACITY;
        private long last;

        Bucket(long now) { this.last = now; }

        synchronized boolean tryConsume(long now) {
            tokens = Math.min(AUTH_CAPACITY, tokens + (double) (now - last) / AUTH_REFILL_NANOS);
            last = now;
            if (tokens >= 1) { tokens -= 1; return true; }
            return false;
        }

        synchronized boolean idle(long cutoff) { return last < cutoff; }
    }
}
