package br.com.fiap.clyvovet.controller;

import br.com.fiap.clyvovet.model.Perfil;
import br.com.fiap.clyvovet.security.UsuarioAutenticado;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    record LoginResponse(String nome, Perfil perfil) {}

    @PostMapping("login")
    LoginResponse login(@AuthenticationPrincipal UsuarioAutenticado usuario) {
        return new LoginResponse(usuario.getNome(), usuario.getPerfil());
    }
}
