package com.example.rotinaAPP.Dtos;

import java.util.UUID;

public record CriarHabitoRequest(
        UUID usuarioId,
        String titulo,
        String descricao
){}

