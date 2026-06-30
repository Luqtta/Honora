package com.honorarios.honorarios_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegistrarRequest(
        @NotBlank String nome,
        @NotBlank @Email String email,
        @NotBlank String senha
) {}
