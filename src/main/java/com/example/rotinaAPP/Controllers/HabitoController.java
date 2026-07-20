package com.example.rotinaAPP.Controllers;

import com.example.rotinaAPP.Dtos.CriarHabitoRequest;
import com.example.rotinaAPP.Dtos.EditarHabitoRequest;
import com.example.rotinaAPP.Dtos.EstaticaResponse;
import com.example.rotinaAPP.Dtos.HabitoDoDiaResponse;
import com.example.rotinaAPP.Entities.Habito;
import com.example.rotinaAPP.Services.HabitoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/habitos")
public class HabitoController {

    @Autowired
    private HabitoService habitoService;

    @GetMapping("/hoje")
    public ResponseEntity<List<HabitoDoDiaResponse>> listarHoje(
            @RequestParam UUID usuarioId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {

        List<HabitoDoDiaResponse> habitos = habitoService.listarHabitosDoDia(usuarioId, data);
        return ResponseEntity.ok(habitos);
    }

    @GetMapping("/{habitoId}/estatisticas")
    public ResponseEntity<EstaticaResponse> estatisticas(
            @PathVariable UUID habitoId,
            Authentication authentication) {

        EstaticaResponse stats = habitoService.calcularEstatiscas(habitoId, authentication.getName());
        return ResponseEntity.ok(stats);
    }

    @GetMapping
    public ResponseEntity<List<Habito>> listarTodos(@RequestParam UUID usuarioId) {
        List<Habito> habitos = habitoService.listarTodos(usuarioId);
        return ResponseEntity.ok(habitos);
    }

    @PostMapping
    public ResponseEntity<Habito> criar(@Valid @RequestBody CriarHabitoRequest request) {
        Habito habito = habitoService.criar(request.usuarioId(), request.titulo(), request.descricao());
        return ResponseEntity.status(HttpStatus.CREATED).body(habito);
    }

    @PostMapping("/{habitoId}/concluir")
    public ResponseEntity<Void> marcarComoConcluido(
            @PathVariable UUID habitoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            Authentication authentication) {

        habitoService.marcarConcluido(habitoId, data, authentication.getName());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{habitoId}")
    public ResponseEntity<Habito> editar(
            @PathVariable UUID habitoId,
            @RequestBody EditarHabitoRequest request,
            Authentication authentication) {

        Habito habito = habitoService.editar(habitoId, request.titulo(), request.descricao(), request.ativo(), authentication.getName());
        return ResponseEntity.ok(habito);
    }

    @DeleteMapping("/{habitoId}")
    public ResponseEntity<Void> deletar(
            @PathVariable UUID habitoId,
            Authentication authentication) {

        habitoService.deletar(habitoId, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{habitoId}/concluir")
    public ResponseEntity<Void> desmarcar(
            @PathVariable UUID habitoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            Authentication authentication) {

        habitoService.desmarcar(habitoId, data, authentication.getName());
        return ResponseEntity.ok().build();
    }
}
