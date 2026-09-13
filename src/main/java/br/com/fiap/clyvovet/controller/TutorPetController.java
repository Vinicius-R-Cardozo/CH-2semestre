package br.com.fiap.clyvovet.controller;

import br.com.fiap.clyvovet.dto.PetForm;
import br.com.fiap.clyvovet.model.Especie;
import br.com.fiap.clyvovet.security.UsuarioAutenticado;
import br.com.fiap.clyvovet.service.CarteiraService;
import br.com.fiap.clyvovet.service.PetService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/tutor/pets")
public class TutorPetController {

    private static final String FORMULARIO = "tutor/pet-form";
    private static final String REDIRECT_LISTA = "redirect:/tutor/pets";

    private final PetService petService;
    private final CarteiraService carteiraService;

    public TutorPetController(PetService petService, CarteiraService carteiraService) {
        this.petService = petService;
        this.carteiraService = carteiraService;
    }

    @ModelAttribute("especies")
    public Especie[] especies() {
        return Especie.values();
    }

    @GetMapping
    public String listar(@AuthenticationPrincipal UsuarioAutenticado usuario, Model model) {
        model.addAttribute("pets", petService.listarDoTutor(usuario.getId()));
        return "tutor/pets";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("petForm", new PetForm());
        return FORMULARIO;
    }

    @PostMapping
    public String cadastrar(@AuthenticationPrincipal UsuarioAutenticado usuario,
                            @Valid PetForm petForm, BindingResult resultado,
                            RedirectAttributes redirectAttributes) {
        if (resultado.hasErrors()) {
            return FORMULARIO;
        }
        petService.cadastrar(usuario.getId(), petForm);
        redirectAttributes.addFlashAttribute("sucesso", "Pet cadastrado com sucesso.");
        return REDIRECT_LISTA;
    }

    @GetMapping("/{id}/editar")
    public String editar(@AuthenticationPrincipal UsuarioAutenticado usuario, @PathVariable Long id, Model model) {
        model.addAttribute("petForm", PetForm.de(petService.buscarDoTutor(usuario.getId(), id)));
        return FORMULARIO;
    }

    @PostMapping("/{id}")
    public String atualizar(@AuthenticationPrincipal UsuarioAutenticado usuario, @PathVariable Long id,
                            @Valid PetForm petForm, BindingResult resultado,
                            RedirectAttributes redirectAttributes) {
        if (resultado.hasErrors()) {
            return FORMULARIO;
        }
        petService.atualizar(usuario.getId(), id, petForm);
        redirectAttributes.addFlashAttribute("sucesso", "Dados do pet atualizados.");
        return REDIRECT_LISTA;
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@AuthenticationPrincipal UsuarioAutenticado usuario, @PathVariable Long id,
                          RedirectAttributes redirectAttributes) {
        petService.excluir(usuario.getId(), id);
        redirectAttributes.addFlashAttribute("sucesso", "Pet removido.");
        return REDIRECT_LISTA;
    }

    @GetMapping("/{id}")
    public String carteira(@AuthenticationPrincipal UsuarioAutenticado usuario, @PathVariable Long id, Model model) {
        model.addAttribute("carteira", carteiraService.montar(petService.buscarDoTutor(usuario.getId(), id)));
        return "carteira";
    }
}
