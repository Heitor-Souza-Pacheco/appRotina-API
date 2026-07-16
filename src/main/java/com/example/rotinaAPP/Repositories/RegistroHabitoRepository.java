package com.example.rotinaAPP.Repositories;

import com.example.rotinaAPP.Entities.RegistroHabito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RegistroHabitoRepository extends JpaRepository<RegistroHabito, UUID> {

    Optional<RegistroHabito> findByHabitoIdAndData(UUID habitoId, LocalDate data);
    List<RegistroHabito> findByHabitoIdAndDataBetwee(UUID habitoId, LocalDate inicio, LocalDate fim);
}