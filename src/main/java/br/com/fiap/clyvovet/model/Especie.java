package br.com.fiap.clyvovet.model;

public enum Especie {

    CAO("Cão", 7),
    GATO("Gato", 10);

    private final String descricao;
    private final int idadeInicioSenior;

    Especie(String descricao, int idadeInicioSenior) {
        this.descricao = descricao;
        this.idadeInicioSenior = idadeInicioSenior;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getIdadeInicioSenior() {
        return idadeInicioSenior;
    }
}
