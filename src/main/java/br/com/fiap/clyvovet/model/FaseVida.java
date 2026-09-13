package br.com.fiap.clyvovet.model;

import java.time.LocalDate;
import java.time.Period;

public enum FaseVida {

    FILHOTE("Filhote"),
    ADULTO("Adulto"),
    SENIOR("Sênior");

    private static final int IDADE_INICIO_ADULTO = 1;

    private final String descricao;

    FaseVida(String descricao) {
        this.descricao = descricao;
    }

    public static FaseVida de(Especie especie, LocalDate dataNascimento, LocalDate hoje) {
        int idade = Period.between(dataNascimento, hoje).getYears();
        if (idade < IDADE_INICIO_ADULTO) {
            return FILHOTE;
        }
        return idade >= especie.getIdadeInicioSenior() ? SENIOR : ADULTO;
    }

    public String getDescricao() {
        return descricao;
    }
}
