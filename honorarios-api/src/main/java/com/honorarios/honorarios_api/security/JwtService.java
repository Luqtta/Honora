package com.honorarios.honorarios_api.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

    private final SecretKey key;
    private final long expiration;

    public JwtService(@Value("${jwt.secret}") String secret,
                      @Value("${jwt.expiration}") long expiration) {
        if (secret == null || secret.isBlank()) {
            throw new IllegalStateException(
                    "JWT_SECRET nao configurado. Defina a variavel de ambiente JWT_SECRET com uma chave aleatoria de no minimo 32 caracteres (256 bits).");
        }
        byte[] bytes = secret.getBytes(StandardCharsets.UTF_8);
        if (bytes.length < 32) {
            throw new IllegalStateException(
                    "JWT_SECRET fraco: " + bytes.length + " bytes. Use no minimo 32 bytes (256 bits) para HMAC-SHA256.");
        }
        this.key = Keys.hmacShaKeyFor(bytes);
        this.expiration = expiration;
    }

    public String generate(UUID usuarioId) {
        Date agora = new Date();
        return Jwts.builder()
                .subject(usuarioId.toString())
                .issuedAt(agora)
                .expiration(new Date(agora.getTime() + expiration))
                .signWith(key)
                .compact();
    }

    public UUID extractUserId(String token) {
        String sub = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
        return UUID.fromString(sub);
    }
}
