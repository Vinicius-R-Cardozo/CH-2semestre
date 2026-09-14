package br.com.fiap.clyvovet.model;

import java.util.Set;

public enum StatusAgendamento {

    SOLICITADO,
    CONFIRMADO,
    RECUSADO,
    REALIZADO;

    public static final Set<StatusAgendamento> EM_ABERTO = Set.of(SOLICITADO, CONFIRMADO);
}
