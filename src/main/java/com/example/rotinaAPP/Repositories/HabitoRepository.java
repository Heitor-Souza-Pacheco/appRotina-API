package com.example.rotinaAPP.Repositories;

import com.example.rotinaAPP.Entities.Habito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface HabitoRepository extends JpaRepository<Habito, UUID> {

    List<Habito> findByUsuarioIdAndAtivoTrue(UUID usuarioId);
    List<Habito> findByUsuarioId(UUID usuarioId);
}
