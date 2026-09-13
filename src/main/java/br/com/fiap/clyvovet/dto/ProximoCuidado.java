package br.com.fiap.clyvovet.dto;

import br.com.fiap.clyvovet.model.SituacaoCuidado;
import br.com.fiap.clyvovet.model.TipoCuidado;

import java.time.LocalDate;

public record ProximoCuidado(
        TipoCuidado tipo,
        LocalDate ultimaRealizacao,
        LocalDate dataPrevista,
        SituacaoCuidado situacao,
        boolean agendado) {

    public static ProximoCuidado semRegistro(TipoCuidado tipo, boolean agendado) {
        return new ProximoCuidado(tipo, null, null, SituacaoCuidado.SEM_REGISTRO, agendado);
    }
}
