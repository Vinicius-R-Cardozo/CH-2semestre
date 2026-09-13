package br.com.fiap.clyvovet.model;

import java.util.Set;

public enum StatusAgendamento {

    SOLICITADO("Solicitado"),
    CONFIRMADO("Confirmado"),
    RECUSADO("Recusado"),
    REALIZADO("Realizado");

    public static final Set<StatusAgendamento> EM_ABERTO = Set.of(SOLICITADO, CONFIRMADO);

    private final String descricao;

    StatusAgendamento(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
