package br.com.fiap.clyvovet.controller;

import br.com.fiap.clyvovet.security.UsuarioAutenticado;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String paginaInicial(@AuthenticationPrincipal UsuarioAutenticado usuario) {
        return switch (usuario.getPerfil()) {
            case TUTOR -> "redirect:/tutor/pets";
            case VETERINARIO -> "redirect:/veterinario/agendamentos";
        };
    }
}
