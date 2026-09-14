package br.com.fiap.clyvovet.controller;

import br.com.fiap.clyvovet.model.CarteiraPet;
import br.com.fiap.clyvovet.model.Pet;
import br.com.fiap.clyvovet.service.CarteiraService;
import br.com.fiap.clyvovet.service.PetService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("pacientes")
public class PacienteController {

    private final PetService petService;
    private final CarteiraService carteiraService;

    public PacienteController(PetService petService, CarteiraService carteiraService) {
        this.petService = petService;
        this.carteiraService = carteiraService;
    }

    @GetMapping
    List<Pet> listar() {
        return petService.listarTodos();
    }

    @GetMapping("{id}/carteira")
    CarteiraPet carteira(@PathVariable Long id) {
        return carteiraService.montar(petService.buscar(id));
    }
}
