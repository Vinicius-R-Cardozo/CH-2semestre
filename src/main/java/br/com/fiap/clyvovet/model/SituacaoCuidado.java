package br.com.fiap.clyvovet.model;

import java.time.LocalDate;

public enum SituacaoCuidado {

    SEM_REGISTRO,
    ATRASADO,
    PROXIMO,
    EM_DIA;

    private static final int DIAS_DE_ANTECEDENCIA = 30;

    public static SituacaoCuidado para(LocalDate dataPrevista, LocalDate hoje) {
        if (dataPrevista.isBefore(hoje)) {
            return ATRASADO;
        }
        if (dataPrevista.isAfter(hoje.plusDays(DIAS_DE_ANTECEDENCIA))) {
            return EM_DIA;
        }
        return PROXIMO;
    }
}
