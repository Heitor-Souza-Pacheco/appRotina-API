package com.example.rotinaAPP.Dtos;

import java.time.LocalTime;

public record AtualizarPerfilRequest(
        String fusoHorario,
        LocalTime horarioReset,
        LocalTime horarioNotificacao,
        String fcmToken
) {}
