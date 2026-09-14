package br.com.fiap.clyvovet.controller.api;

import br.com.fiap.clyvovet.dto.CarteiraResponse;
import br.com.fiap.clyvovet.dto.PetForm;
import br.com.fiap.clyvovet.dto.PetResponse;
import br.com.fiap.clyvovet.model.Pet;
import br.com.fiap.clyvovet.security.UsuarioAutenticado;
import br.com.fiap.clyvovet.service.CarteiraService;
import br.com.fiap.clyvovet.service.PetService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/tutor/pets")
public class TutorPetApiController {

    private final PetService petService;
    private final CarteiraService carteiraService;

    public TutorPetApiController(PetService petService, CarteiraService carteiraService) {
        this.petService = petService;
        this.carteiraService = carteiraService;
    }

    @GetMapping
    public ResponseEntity<List<PetResponse>> listar(@AuthenticationPrincipal UsuarioAutenticado usuario) {
        return ResponseEntity.ok(PetResponse.listaDe(petService.listarDoTutor(usuario.getId())));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PetResponse> buscar(@AuthenticationPrincipal UsuarioAutenticado usuario,
                                              @PathVariable Long id) {
        return ResponseEntity.ok(PetResponse.de(petService.buscarDoTutor(usuario.getId(), id)));
    }

    @PostMapping
    public ResponseEntity<PetResponse> cadastrar(@AuthenticationPrincipal UsuarioAutenticado usuario,
                                                 @RequestBody @Valid PetForm petForm,
                                                 UriComponentsBuilder uriBuilder) {
        Pet pet = petService.cadastrar(usuario.getId(), petForm);
        URI endereco = uriBuilder.path("/api/tutor/pets/{id}").buildAndExpand(pet.getId()).toUri();
        return ResponseEntity.created(endereco).body(PetResponse.de(pet));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PetResponse> atualizar(@AuthenticationPrincipal UsuarioAutenticado usuario,
                                                 @PathVariable Long id,
                                                 @RequestBody @Valid PetForm petForm) {
        Pet pet = petService.atualizar(usuario.getId(), id, petForm);
        return ResponseEntity.ok(PetResponse.de(pet));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@AuthenticationPrincipal UsuarioAutenticado usuario,
                                        @PathVariable Long id) {
        petService.excluir(usuario.getId(), id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/carteira")
    public ResponseEntity<CarteiraResponse> carteira(@AuthenticationPrincipal UsuarioAutenticado usuario,
                                                     @PathVariable Long id) {
        Pet pet = petService.buscarDoTutor(usuario.getId(), id);
        return ResponseEntity.ok(CarteiraResponse.de(carteiraService.montar(pet)));
    }
}
