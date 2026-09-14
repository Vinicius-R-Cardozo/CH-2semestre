package br.com.fiap.clyvovet.controller;

import br.com.fiap.clyvovet.dto.ConclusaoForm;
import br.com.fiap.clyvovet.exception.RegraDeNegocioException;
import br.com.fiap.clyvovet.model.Agendamento;
import br.com.fiap.clyvovet.service.AgendamentoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/veterinario/agendamentos")
public class VeterinarioAgendamentoController {

    private static final String REDIRECT_AGENDA = "redirect:/veterinario/agendamentos";

    private final AgendamentoService agendamentoService;

    public VeterinarioAgendamentoController(AgendamentoService agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    @GetMapping
    public String agenda(Model model) {
        model.addAttribute("agendamentos", agendamentoService.listarEmAberto());
        return "veterinario/agenda";
    }

    @PostMapping("/{id}/confirmar")
    public String confirmar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        agendamentoService.confirmar(id);
        redirectAttributes.addFlashAttribute("sucesso", "Agendamento confirmado.");
        return REDIRECT_AGENDA;
    }

    @PostMapping("/{id}/recusar")
    public String recusar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        agendamentoService.recusar(id);
        redirectAttributes.addFlashAttribute("sucesso", "Solicitação recusada.");
        return REDIRECT_AGENDA;
    }

    @GetMapping("/{id}/concluir")
    public String formularioConclusao(@PathVariable Long id, Model model) {
        model.addAttribute("conclusaoForm", new ConclusaoForm());
        return exibirFormularioConclusao(id, model);
    }

    @PostMapping("/{id}/concluir")
    public String concluir(@PathVariable Long id, @Valid ConclusaoForm conclusaoForm, BindingResult resultado,
                           Model model, RedirectAttributes redirectAttributes) {
        if (resultado.hasErrors()) {
            return exibirFormularioConclusao(id, model);
        }
        Agendamento agendamento = agendamentoService.concluir(id, conclusaoForm.getObservacao());
        redirectAttributes.addFlashAttribute("sucesso",
                "Atendimento registrado. A carteira de cuidados foi atualizada.");
        return "redirect:/veterinario/pets/" + agendamento.getPet().getId();
    }

    @ExceptionHandler(RegraDeNegocioException.class)
    public String tratarRegraDeNegocio(RegraDeNegocioException excecao, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("erro", excecao.getMessage());
        return REDIRECT_AGENDA;
    }

    private String exibirFormularioConclusao(Long id, Model model) {
        model.addAttribute("agendamento", agendamentoService.buscarParaConclusao(id));
        return "veterinario/conclusao";
    }
}
