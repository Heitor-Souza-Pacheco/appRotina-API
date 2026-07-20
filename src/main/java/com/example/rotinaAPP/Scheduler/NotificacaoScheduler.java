package com.example.rotinaAPP.Scheduler;

import com.example.rotinaAPP.Entities.Habito;
import com.example.rotinaAPP.Entities.Usuario;
import com.example.rotinaAPP.Repositories.HabitoRepository;
import com.example.rotinaAPP.Repositories.RegistroHabitoRepository;
import com.example.rotinaAPP.Repositories.UsuarioRepository;
import com.example.rotinaAPP.Services.NotificacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class NotificacaoScheduler {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private HabitoRepository habitoRepository;

    @Autowired
    private RegistroHabitoRepository registroHabitoRepository;

    @Autowired
    private NotificacaoService notificacaoService;

    @Scheduled(cron = "0 0 * * * *")
    public void verificarENotificar() {
        // Arredonda para a hora cheia (sem minutos/segundos/nanos)
        LocalTime agora = LocalTime.now().withMinute(0).withSecond(0).withNano(0);
        LocalDate hoje = LocalDate.now();

        List<Usuario> usuarios = usuarioRepository.findByHorarioNotificacao(agora);

        for (Usuario usuario : usuarios) {
            if (usuario.getFcmToken() == null || usuario.getFcmToken().isBlank()) {
                continue;
            }

            List<Habito> habitos = habitoRepository.findByUsuarioIdAndAtivoTrue(usuario.getId());

            List<String> pendentes = habitos.stream()
                    .filter(habito -> registroHabitoRepository
                            .findByHabitoIdAndData(habito.getId(), hoje)
                            .isEmpty())
                    .map(Habito::getTitulo)
                    .collect(Collectors.toList());

            if (!pendentes.isEmpty()) {
                String corpo = "Ainda falta: " + String.join(", ", pendentes) + ". Bora!";
                notificacaoService.enviarNotificacao(
                        usuario.getFcmToken(),
                        "Ei, não esqueça dos hábitos!",
                        corpo
                );
            }
        }
    }
}
