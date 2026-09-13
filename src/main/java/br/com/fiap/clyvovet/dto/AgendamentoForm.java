package br.com.fiap.clyvovet.dto;

import br.com.fiap.clyvovet.model.TipoCuidado;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class AgendamentoForm {

    @NotNull(message = "Selecione o pet.")
    private Long petId;

    @NotNull(message = "Selecione o tipo de cuidado.")
    private TipoCuidado tipo;

    @NotNull(message = "Informe a data desejada.")
    @FutureOrPresent(message = "A data desejada não pode estar no passado.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate data;

    public static AgendamentoForm para(Long petId, TipoCuidado tipo) {
        AgendamentoForm form = new AgendamentoForm();
        form.petId = petId;
        form.tipo = tipo;
        form.data = LocalDate.now();
        return form;
    }

    public Long getPetId() {
        return petId;
    }

    public void setPetId(Long petId) {
        this.petId = petId;
    }

    public TipoCuidado getTipo() {
        return tipo;
    }

    public void setTipo(TipoCuidado tipo) {
        this.tipo = tipo;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }
}
