package br.com.fiap.clyvovet.controller;

import br.com.fiap.clyvovet.dto.AgendamentoForm;
import br.com.fiap.clyvovet.exception.RegraDeNegocioException;
import br.com.fiap.clyvovet.model.TipoCuidado;
import br.com.fiap.clyvovet.security.UsuarioAutenticado;
import br.com.fiap.clyvovet.service.AgendamentoService;
import br.com.fiap.clyvovet.service.PetService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/tutor/agendamentos")
public class TutorAgendamentoController {

    private final AgendamentoService agendamentoService;
    private final PetService petService;

    public TutorAgendamentoController(AgendamentoService agendamentoService, PetService petService) {
        this.agendamentoService = agendamentoService;
        this.petService = petService;
    }

    @GetMapping
    public String listar(@AuthenticationPrincipal UsuarioAutenticado usuario, Model model) {
        model.addAttribute("agendamentos", agendamentoService.listarDoTutor(usuario.getId()));
        return "tutor/agendamentos";
    }

    @GetMapping("/novo")
    public String novo(@AuthenticationPrincipal UsuarioAutenticado usuario,
                       @RequestParam(required = false) Long petId,
                       @RequestParam(required = false) TipoCuidado tipo,
                       Model model) {
        model.addAttribute("agendamentoForm", AgendamentoForm.para(petId, tipo));
        return exibirFormulario(usuario, model);
    }

    @PostMapping
    public String solicitar(@AuthenticationPrincipal UsuarioAutenticado usuario,
                            @Valid AgendamentoForm agendamentoForm, BindingResult resultado,
                            Model model, RedirectAttributes redirectAttributes) {
        if (resultado.hasErrors()) {
            return exibirFormulario(usuario, model);
        }
        try {
            agendamentoService.solicitar(usuario.getId(), agendamentoForm);
        } catch (RegraDeNegocioException excecao) {
            resultado.reject("agendamento.invalido", excecao.getMessage());
            return exibirFormulario(usuario, model);
        }
        redirectAttributes.addFlashAttribute("sucesso",
                "Solicitação enviada! Assim que a clínica confirmar, a situação muda aqui.");
        return "redirect:/tutor/agendamentos";
    }

    private String exibirFormulario(UsuarioAutenticado usuario, Model model) {
        model.addAttribute("pets", petService.listarDoTutor(usuario.getId()));
        model.addAttribute("tipos", TipoCuidado.values());
        return "tutor/agendamento-form";
    }
}
