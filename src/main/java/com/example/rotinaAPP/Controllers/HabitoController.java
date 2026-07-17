package com.example.rotinaAPP.Controllers;

import com.example.rotinaAPP.Dtos.CriarHabitoRequest;
import com.example.rotinaAPP.Dtos.HabitoDoDiaResponse;
import com.example.rotinaAPP.Entities.Habito;
import com.example.rotinaAPP.Services.HabitoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

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
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate data){
        List<HabitoDoDiaResponse> habitos = habitoService.listarHabitosDoDia(usuarioId, data);
        return ResponseEntity.ok(habitos);
    }

    @PostMapping
    public ResponseEntity<Habito> criar(@RequestBody CriarHabitoRequest request){
        Habito habito = habitoService.criar(request.usuarioId(), request.titulo(), request.descricao());
        return ResponseEntity.status(HttpStatus.CREATED).body(habito);
    }

    @PostMapping("/{habito}/concluir")
    public ResponseEntity<Void> marcarComoConcluido(
            @PathVariable UUID habitoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data){

        habitoService.marcarConcluido(habitoId, data);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{habito}/concluir")
    public ResponseEntity<Void> desmarcar(
            @PathVariable UUID habitoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data){

        habitoService.desmarcar(habitoId, data);
        return ResponseEntity.ok().build();
    }
}
