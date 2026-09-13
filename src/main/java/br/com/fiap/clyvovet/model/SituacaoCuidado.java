package br.com.fiap.clyvovet.model;

import java.time.LocalDate;

public enum SituacaoCuidado {

    SEM_REGISTRO("Sem registro"),
    ATRASADO("Atrasado"),
    PROXIMO("Próximo"),
    EM_DIA("Em dia");

    private static final int DIAS_DE_ANTECEDENCIA = 30;

    private final String descricao;

    SituacaoCuidado(String descricao) {
        this.descricao = descricao;
    }

    public static SituacaoCuidado para(LocalDate dataPrevista, LocalDate hoje) {
        if (dataPrevista.isBefore(hoje)) {
            return ATRASADO;
        }
        if (dataPrevista.isAfter(hoje.plusDays(DIAS_DE_ANTECEDENCIA))) {
            return EM_DIA;
        }
        return PROXIMO;
    }

    public String getDescricao() {
        return descricao;
    }
}
