package br.com.fiap.clyvovet.controller.api;

import br.com.fiap.clyvovet.dto.AgendamentoResponse;
import br.com.fiap.clyvovet.dto.ConclusaoForm;
import br.com.fiap.clyvovet.service.AgendamentoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/veterinario/agendamentos")
public class VeterinarioAgendamentoApiController {

    private final AgendamentoService agendamentoService;

    public VeterinarioAgendamentoApiController(AgendamentoService agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    @GetMapping
    public ResponseEntity<List<AgendamentoResponse>> listarEmAberto() {
        return ResponseEntity.ok(AgendamentoResponse.listaDe(agendamentoService.listarEmAberto()));
    }

    @PatchMapping("/{id}/confirmar")
    public ResponseEntity<AgendamentoResponse> confirmar(@PathVariable Long id) {
        return ResponseEntity.ok(AgendamentoResponse.de(agendamentoService.confirmar(id)));
    }

    @PatchMapping("/{id}/recusar")
    public ResponseEntity<AgendamentoResponse> recusar(@PathVariable Long id) {
        return ResponseEntity.ok(AgendamentoResponse.de(agendamentoService.recusar(id)));
    }

    @PatchMapping("/{id}/concluir")
    public ResponseEntity<AgendamentoResponse> concluir(@PathVariable Long id,
                                                        @RequestBody @Valid ConclusaoForm conclusaoForm) {
        return ResponseEntity.ok(AgendamentoResponse.de(
                agendamentoService.concluir(id, conclusaoForm.getObservacao())));
    }
}
