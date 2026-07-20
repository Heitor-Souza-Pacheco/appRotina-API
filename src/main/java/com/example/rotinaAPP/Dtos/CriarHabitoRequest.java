package com.example.rotinaAPP.Dtos;

import jakarta.validation.constraints.NotBlank;

public record CriarHabitoRequest(
        @NotBlank(message = "Título é obrigatório")
        String titulo,

        String descricao
) {}
