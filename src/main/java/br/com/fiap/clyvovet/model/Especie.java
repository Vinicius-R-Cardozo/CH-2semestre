package br.com.fiap.clyvovet.model;

public enum Especie {

    CAO(7),
    GATO(10);

    private final int idadeInicioSenior;

    Especie(int idadeInicioSenior) {
        this.idadeInicioSenior = idadeInicioSenior;
    }

    public int getIdadeInicioSenior() {
        return idadeInicioSenior;
    }
}
