package com.example.rotinaAPP.Services;

import com.example.rotinaAPP.Dtos.HabitoDoDiaResponse;
import com.example.rotinaAPP.Entities.Habito;
import com.example.rotinaAPP.Entities.RegistroHabito;
import com.example.rotinaAPP.Entities.Usuario;
import com.example.rotinaAPP.Exceptions.RecursoNaoEncontradoException;
import com.example.rotinaAPP.Repositories.HabitoRepository;
import com.example.rotinaAPP.Repositories.RegistroHabitoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class HabitoService {

    @Autowired
    private HabitoRepository habitoRepository;

    @Autowired
    private RegistroHabitoRepository registroHabitoRepository;

    public Habito criar(UUID usuarioId, String titulo, String descricao) {
        Habito habito = new Habito();
        habito.setTitulo(titulo);
        habito.setDescricao(descricao);
        habito.setAtivo(true);

        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);
        habito.setUsuario(usuario);
        return habitoRepository.save(habito);
    }

    public List<HabitoDoDiaResponse> listarHabitosDoDia(UUID usuarioId, LocalDate data) {
        List<Habito> habitos = habitoRepository.findByUsuarioIdAndAtivoTrue(usuarioId);

        return habitos.stream().map(habito -> {
            boolean concluido = registroHabitoRepository
                    .findByHabitoIdAndData(habito.getId(), data)
                    .map(RegistroHabito::isConcluido)
                    .orElse(false);

            return new HabitoDoDiaResponse(
                    habito.getId(),
                    habito.getTitulo(),
                    habito.getDescricao(),
                    concluido
            );
        }).toList();
    }

    public void marcarConcluido(UUID habitoId, LocalDate data) {
        habitoRepository.findById(habitoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Hábito não encontrado"));

        Optional<RegistroHabito> existente = registroHabitoRepository
                .findByHabitoIdAndData(habitoId, data);

        if (existente.isPresent()) {
            RegistroHabito registro = existente.get();
            registro.setConcluido(true);
            registroHabitoRepository.save(registro);
        } else {
            RegistroHabito registro = new RegistroHabito();
            Habito habito = new Habito();
            habito.setId(habitoId);
            registro.setHabito(habito);
            registro.setData(data);
            registro.setConcluido(true);
            registroHabitoRepository.save(registro);
        }
    }

    public void desmarcar(UUID habitoId, LocalDate data) {
        registroHabitoRepository.findByHabitoIdAndData(habitoId, data)
                .ifPresent(registroHabitoRepository::delete);
    }
}
