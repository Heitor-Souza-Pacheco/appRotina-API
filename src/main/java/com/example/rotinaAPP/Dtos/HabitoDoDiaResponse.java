package com.example.rotinaAPP.Dtos;

import java.util.UUID;

public record HabitoDoDiaResponse (
    UUID id,
    String titulo,
    String descricao,
    boolean concluido
){}
