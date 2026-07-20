package com.example.rotinaAPP.Services;

import com.example.rotinaAPP.Dtos.EstaticaResponse;
import com.example.rotinaAPP.Dtos.HabitoDoDiaResponse;
import com.example.rotinaAPP.Entities.Habito;
import com.example.rotinaAPP.Entities.RegistroHabito;
import com.example.rotinaAPP.Entities.Usuario;
import com.example.rotinaAPP.Exceptions.AcessoNegadoException;
import com.example.rotinaAPP.Exceptions.RecursoNaoEncontradoException;
import com.example.rotinaAPP.Repositories.HabitoRepository;
import com.example.rotinaAPP.Repositories.RegistroHabitoRepository;
import com.example.rotinaAPP.Repositories.UsuarioRepository;
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

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Habito criar(String emailUsuarioLogado, String titulo, String descricao) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuarioLogado)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

        Habito habito = new Habito();
        habito.setTitulo(titulo);
        habito.setDescricao(descricao);
        habito.setAtivo(true);
        habito.setUsuario(usuario);
        return habitoRepository.save(habito);
    }

    public List<HabitoDoDiaResponse> listarHabitosDoDia(String emailUsuarioLogado, LocalDate data) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuarioLogado)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

        List<Habito> habitos = habitoRepository.findByUsuarioIdAndAtivoTrue(usuario.getId());

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

    public void marcarConcluido(UUID habitoId, LocalDate data, String emailUsuarioLogado) {
        verificarPermissao(habitoId, emailUsuarioLogado);

        Optional<RegistroHabito> existente = registroHabitoRepository
                .findByHabitoIdAndData(habitoId, data);

        if (existente.isPresent()) {
            RegistroHabito registro = existente.get();
            registro.setConcluido(true);
            registroHabitoRepository.save(registro);
        } else {
            RegistroHabito registro = new RegistroHabito();
            Habito habito = habitoRepository.findById(habitoId).get();
            registro.setHabito(habito);
            registro.setData(data);
            registro.setConcluido(true);
            registroHabitoRepository.save(registro);
        }
    }

    public void desmarcar(UUID habitoId, LocalDate data, String emailUsuarioLogado) {
        verificarPermissao(habitoId, emailUsuarioLogado);
        registroHabitoRepository.findByHabitoIdAndData(habitoId, data)
                .ifPresent(registroHabitoRepository::delete);
    }

    public EstaticaResponse calcularEstatiscas(UUID habitoId, String emailUsuarioLogado) {
        verificarPermissao(habitoId, emailUsuarioLogado);
        Habito habito = habitoRepository.findById(habitoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Hábito não encontrado"));

        LocalDate hoje = LocalDate.now();

        int streakAtual = 0;
        LocalDate dia = hoje;
        while (true) {
            Optional<RegistroHabito> registro = registroHabitoRepository.findByHabitoIdAndData(habitoId, dia);
            if (registro.isPresent() && registro.get().isConcluido()) {
                streakAtual++;
                dia = dia.minusDays(1);
            } else {
                break;
            }
        }

        List<RegistroHabito> todosRegistros = registroHabitoRepository
                .findByHabitoIdAndDataBetween(habitoId, hoje.minusYears(1), hoje);

        int maiorStreak = 0;
        int streakTemp = 0;
        LocalDate diaAnterior = null;

        List<LocalDate> diasConcluidos = todosRegistros.stream()
                .filter(RegistroHabito::isConcluido)
                .map(RegistroHabito::getData)
                .sorted()
                .toList();

        for (LocalDate d : diasConcluidos) {
            if (diaAnterior == null || d.equals(diaAnterior.plusDays(1))) {
                streakTemp++;
            } else {
                streakTemp = 1;
            }
            maiorStreak = Math.max(maiorStreak, streakTemp);
            diaAnterior = d;
        }

        long concluidosSemana = registroHabitoRepository
                .findByHabitoIdAndDataBetween(habitoId, hoje.minusDays(6), hoje)
                .stream().filter(RegistroHabito::isConcluido).count();
        double taxaSemana = (concluidosSemana / 7.0) * 100;

        long concluidosMes = registroHabitoRepository
                .findByHabitoIdAndDataBetween(habitoId, hoje.minusDays(29), hoje)
                .stream().filter(RegistroHabito::isConcluido).count();
        double taxaMes = (concluidosMes / 30.0) * 100;

        int totalDiasConcluidos = diasConcluidos.size();

        return new EstaticaResponse(
                habito.getId(), habito.getTitulo(), streakAtual, maiorStreak,
                taxaSemana, taxaMes, totalDiasConcluidos);
    }

    public Habito editar(UUID habitoId, String titulo, String descricao, Boolean ativo, String emailUsuarioLogado) {
        verificarPermissao(habitoId, emailUsuarioLogado);
        Habito habito = habitoRepository.findById(habitoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Hábito não encontrado"));

        if (titulo != null) habito.setTitulo(titulo);
        if (descricao != null) habito.setDescricao(descricao);
        if (ativo != null) habito.setAtivo(ativo);

        return habitoRepository.save(habito);
    }

    public void deletar(UUID habitoId, String emailUsuarioLogado) {
        verificarPermissao(habitoId, emailUsuarioLogado);
        Habito habito = habitoRepository.findById(habitoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Hábito não encontrado"));

        List<RegistroHabito> registros = registroHabitoRepository
                .findByHabitoIdAndDataBetween(habitoId, LocalDate.of(2000, 1, 1), LocalDate.now());
        registroHabitoRepository.deleteAll(registros);
        habitoRepository.delete(habito);
    }

    public List<Habito> listarTodos(String emailUsuarioLogado) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuarioLogado)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));
        return habitoRepository.findByUsuarioId(usuario.getId());
    }

    private void verificarPermissao(UUID habitoId, String emailUsuarioLogado) {
        Habito habito = habitoRepository.findById(habitoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Hábito não encontrado"));

        if (!habito.getUsuario().getEmail().equals(emailUsuarioLogado)) {
            throw new AcessoNegadoException("Você não tem permissão para acessar este hábito");
        }
    }
}
