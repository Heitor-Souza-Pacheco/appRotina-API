package com.example.rotinaAPP.Controllers;

import com.example.rotinaAPP.Entities.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioServce usuarioService;

    @PostMapping("/registrar")
    public ResponseEntity<Usuario> registrar(@RequestBody RegistrarUsuarioRequest request){
        Usuario usuario = usuarioService.registrar(request.nome(), request.email(), request.senha());
        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }
}
