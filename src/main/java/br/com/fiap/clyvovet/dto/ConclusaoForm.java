package br.com.fiap.clyvovet.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ConclusaoForm {

    @NotBlank(message = "Descreva o que foi realizado no atendimento.")
    @Size(max = 500, message = "A observação deve ter no máximo 500 caracteres.")
    private String observacao;

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
