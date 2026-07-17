package com.example.rotinaAPP.Controllers;

import com.example.rotinaAPP.Dtos.LoginRequest;
import com.example.rotinaAPP.Dtos.RegistrarUsuarioRequest;
import com.example.rotinaAPP.Security.JwtService;
import com.example.rotinaAPP.Services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/registrar")
    public ResponseEntity<Map<String, String>> registrar(@Valid @RequestBody RegistrarUsuarioRequest request) {
        usuarioService.registrar(request.nome(), request.email(), request.senha());
        String token = jwtService.gerarToken(request.email());
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("token", token));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.senha())
        );
        String token = jwtService.gerarToken(request.email());
        return ResponseEntity.ok(Map.of("token", token));
    }
}
