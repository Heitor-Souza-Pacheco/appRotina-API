package com.example.rotinaAPP.Controllers;

import com.example.rotinaAPP.Entities.Usuario;
import com.example.rotinaAPP.Services.UsuarioService;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class PerfilController {

    @Autowired
    private UsuarioService usuarioService;

    @PutMapping("/perfil")
    public ResponseEntity<Usuario> atualizarPerfil(Authentication authentication, @RequestBody AtualizarPerfilRequest request){

        String email = authentication.getName();
        Usuario usuario = usuarioService.atualizarPerfil(
                email,
                request.fusoHorario(),
                request.horarioReset(),
                request.horarioNotificacao(),
                request.fcmToken()
        );
        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/perfil")
    public ResponseEntity<Usuario> verPerfil(Authentication authentication){

        String email = authentication.getName();
        Usuario usuario = usuarioService.buscarPorEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return ResponseEntity.ok(usuario);
    }
}
