package com.example.rotinaAPP.Dtos;

import java.time.LocalTime;

public record AutalizarPerfilRequest(
        String fusoHorario,
        LocalTime horarioReset,
        LocalTime horarioNotificacao,
        String fcmToken
){}
