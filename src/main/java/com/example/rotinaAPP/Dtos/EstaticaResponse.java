package com.example.rotinaAPP.Dtos;

import java.util.UUID;

public record EstaticaResponse(
        UUID habitoId,
        String titulo,
        int streakAtual,
        int maiorStreak,
        double taxaSemana,
        double taxaMes,
        int totalDiasConcluidos
) {}
