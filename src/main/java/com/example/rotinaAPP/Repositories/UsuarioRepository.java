package com.example.rotinaAPP.Repositories;

import com.example.rotinaAPP.Entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    Optional<Usuario> findByEmail(String email);
    List<Usuario> findByHorarioNotificacao(LocalTime horario);
    List<Usuario> findByHorarioReset(LocalTime horario);
}
