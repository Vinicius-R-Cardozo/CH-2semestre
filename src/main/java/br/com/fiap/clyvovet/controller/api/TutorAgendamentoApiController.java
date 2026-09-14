package br.com.fiap.clyvovet.controller.api;

import br.com.fiap.clyvovet.dto.AgendamentoForm;
import br.com.fiap.clyvovet.dto.AgendamentoResponse;
import br.com.fiap.clyvovet.model.Agendamento;
import br.com.fiap.clyvovet.security.UsuarioAutenticado;
import br.com.fiap.clyvovet.service.AgendamentoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tutor/agendamentos")
public class TutorAgendamentoApiController {

    private final AgendamentoService agendamentoService;

    public TutorAgendamentoApiController(AgendamentoService agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    @GetMapping
    public ResponseEntity<List<AgendamentoResponse>> listar(@AuthenticationPrincipal UsuarioAutenticado usuario) {
        return ResponseEntity.ok(AgendamentoResponse.listaDe(agendamentoService.listarDoTutor(usuario.getId())));
    }

    @PostMapping
    public ResponseEntity<AgendamentoResponse> solicitar(@AuthenticationPrincipal UsuarioAutenticado usuario,
                                                         @RequestBody @Valid AgendamentoForm agendamentoForm) {
        Agendamento agendamento = agendamentoService.solicitar(usuario.getId(), agendamentoForm);
        return ResponseEntity.status(HttpStatus.CREATED).body(AgendamentoResponse.de(agendamento));
    }
}
