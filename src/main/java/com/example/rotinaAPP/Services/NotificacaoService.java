package com.example.rotinaAPP.Services;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Service;

@Service
public class NotificacaoService {

    public void enviarNotificacao(String fcmToken, String titulo, String corpo){

        try{

            Message message = Message.builder()
                    .setToken(fcmToken)
                    .setNotification(Notification.builder()
                            .setTitle(titulo)
                            .setBody(corpo)
                            .build())
                    .build();

            FirebaseMessaging.getInstance().send(message);
        } catch (Exception e){
            System.err.println("Erro ao enviar notificação: " + e.getMessage());
        }
    }
}
