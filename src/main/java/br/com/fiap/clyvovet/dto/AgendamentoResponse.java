package br.com.fiap.clyvovet.dto;

import br.com.fiap.clyvovet.model.Agendamento;
import br.com.fiap.clyvovet.model.StatusAgendamento;
import br.com.fiap.clyvovet.model.TipoCuidado;

import java.time.LocalDate;
import java.util.List;

public record AgendamentoResponse(
        Long id,
        Long petId,
        String petNome,
        TipoCuidado tipo,
        LocalDate data,
        StatusAgendamento status,
        String observacao) {

    public static AgendamentoResponse de(Agendamento agendamento) {
        return new AgendamentoResponse(agendamento.getId(), agendamento.getPet().getId(),
                agendamento.getPet().getNome(), agendamento.getTipo(), agendamento.getData(),
                agendamento.getStatus(), agendamento.getObservacao());
    }

    public static List<AgendamentoResponse> listaDe(List<Agendamento> agendamentos) {
        return agendamentos.stream().map(AgendamentoResponse::de).toList();
    }
}
