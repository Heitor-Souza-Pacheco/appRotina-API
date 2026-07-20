package com.example.rotinaAPP.Services;

import com.example.rotinaAPP.Entities.Usuario;
import com.example.rotinaAPP.Exceptions.EmailJaCadastradoException;
import com.example.rotinaAPP.Exceptions.RecursoNaoEncontradoException;
import com.example.rotinaAPP.Repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario registrar(String nome, String email, String senha) {
        if (usuarioRepository.findByEmail(email).isPresent()) {
            throw new EmailJaCadastradoException("Este email já está cadastrado");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSenha(passwordEncoder.encode(senha));
        usuario.setFusoHorario("America/Sao_Paulo");
        usuario.setHorarioReset(LocalTime.of(0, 0));
        return usuarioRepository.save(usuario);
    }

    public Usuario atualizarPerfil(String email, String fusoHorario, LocalTime horarioReset,
                                    LocalTime horarioNotificacao, String fcmToken) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

        if (fusoHorario != null) usuario.setFusoHorario(fusoHorario);
        if (horarioReset != null) usuario.setHorarioReset(horarioReset);
        if (horarioNotificacao != null) usuario.setHorarioNotificacao(horarioNotificacao);
        if (fcmToken != null) usuario.setFcmToken(fcmToken);

        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }
}
