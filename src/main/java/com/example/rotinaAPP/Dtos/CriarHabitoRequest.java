package com.example.rotinaAPP.Dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CriarHabitoRequest(
        @NotNull(message = "ID do usuário é obrigatório")
        UUID usuarioId,

        @NotBlank(message = "Título é obrigatório")
        String titulo,

        String descricao
) {}
