package com.example.rotinaAPP.Controllers;

import com.example.rotinaAPP.Dtos.RegistrarUsuarioRequest;
import com.example.rotinaAPP.Entities.Usuario;
import com.example.rotinaAPP.Services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/registrar")
    public ResponseEntity<Usuario> registrar(@RequestBody RegistrarUsuarioRequest request) {
        Usuario usuario = usuarioService.registrar(request.nome(), request.email(), request.senha());
        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }
}
