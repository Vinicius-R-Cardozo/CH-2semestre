package br.com.fiap.clyvovet.controller;

import br.com.fiap.clyvovet.model.CarteiraPet;
import br.com.fiap.clyvovet.model.Pet;
import br.com.fiap.clyvovet.security.UsuarioAutenticado;
import br.com.fiap.clyvovet.service.CarteiraService;
import br.com.fiap.clyvovet.service.PetService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("pets")
public class PetController {

    private final PetService petService;
    private final CarteiraService carteiraService;

    public PetController(PetService petService, CarteiraService carteiraService) {
        this.petService = petService;
        this.carteiraService = carteiraService;
    }

    @GetMapping
    List<Pet> listar(@AuthenticationPrincipal UsuarioAutenticado usuario) {
        return petService.listarDoTutor(usuario.getId());
    }

    @GetMapping("{id}")
    Pet buscar(@AuthenticationPrincipal UsuarioAutenticado usuario, @PathVariable Long id) {
        return petService.buscarDoTutor(usuario.getId(), id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Pet cadastrar(@AuthenticationPrincipal UsuarioAutenticado usuario, @RequestBody @Valid Pet pet) {
        return petService.cadastrar(usuario.getId(), pet);
    }

    @PutMapping("{id}")
    Pet atualizar(@AuthenticationPrincipal UsuarioAutenticado usuario, @PathVariable Long id,
                  @RequestBody @Valid Pet pet) {
        return petService.atualizar(usuario.getId(), id, pet);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void excluir(@AuthenticationPrincipal UsuarioAutenticado usuario, @PathVariable Long id) {
        petService.excluir(usuario.getId(), id);
    }

    @GetMapping("{id}/carteira")
    CarteiraPet carteira(@AuthenticationPrincipal UsuarioAutenticado usuario, @PathVariable Long id) {
        return carteiraService.montar(petService.buscarDoTutor(usuario.getId(), id));
    }
}
