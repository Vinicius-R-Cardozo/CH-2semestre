package br.com.fiap.clyvovet.model;

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
