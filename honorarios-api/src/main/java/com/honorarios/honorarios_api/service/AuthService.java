package com.honorarios.honorarios_api.service;

import com.honorarios.honorarios_api.dto.AuthResponse;
import com.honorarios.honorarios_api.dto.LoginRequest;
import com.honorarios.honorarios_api.dto.RegistrarRequest;
import com.honorarios.honorarios_api.entity.Usuario;
import com.honorarios.honorarios_api.repository.UsuarioRepository;
import com.honorarios.honorarios_api.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarios;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    public AuthResponse registrar(RegistrarRequest req) {
        if (usuarios.existsByEmail(req.email())) {
            // mensagem generica: nao confirma se o email existe (anti-enumeracao)
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Nao foi possivel concluir o cadastro");
        }
        Usuario u = new Usuario();
        u.setNome(req.nome());
        u.setEmail(req.email());
        u.setSenhaHash(encoder.encode(req.senha()));
        usuarios.save(u);
        return new AuthResponse(jwt.generate(u.getId()));
    }

    public AuthResponse login(LoginRequest req) {
        Usuario u = usuarios.findByEmail(req.email())
                .filter(x -> encoder.matches(req.senha(), x.getSenhaHash()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais invalidas"));
        return new AuthResponse(jwt.generate(u.getId()));
    }
}
