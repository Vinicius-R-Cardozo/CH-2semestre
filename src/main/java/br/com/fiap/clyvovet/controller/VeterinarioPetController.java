package br.com.fiap.clyvovet.controller;

import br.com.fiap.clyvovet.service.CarteiraService;
import br.com.fiap.clyvovet.service.PetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/veterinario/pets")
public class VeterinarioPetController {

    private final PetService petService;
    private final CarteiraService carteiraService;

    public VeterinarioPetController(PetService petService, CarteiraService carteiraService) {
        this.petService = petService;
        this.carteiraService = carteiraService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("pets", petService.listarTodos());
        return "veterinario/pacientes";
    }

    @GetMapping("/{id}")
    public String carteira(@PathVariable Long id, Model model) {
        model.addAttribute("carteira", carteiraService.montar(petService.buscar(id)));
        return "carteira";
    }
}
