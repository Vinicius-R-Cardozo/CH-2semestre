package br.com.fiap.clyvovet.model;

import br.com.fiap.clyvovet.exception.RegraDeNegocioException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "agendamento")
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "pet_id")
    private Pet pet;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoCuidado tipo;

    @Column(nullable = false)
    private LocalDate data;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusAgendamento status;

    @Column(length = 500)
    private String observacao;

    protected Agendamento() {
    }

    public Agendamento(Pet pet, TipoCuidado tipo, LocalDate data) {
        this.pet = pet;
        this.tipo = tipo;
        this.data = data;
        this.status = StatusAgendamento.SOLICITADO;
    }

    public boolean aguardaConfirmacao() {
        return status == StatusAgendamento.SOLICITADO;
    }

    public boolean podeSerConcluido() {
        return status == StatusAgendamento.CONFIRMADO && !data.isAfter(LocalDate.now());
    }

    public void confirmar() {
        garantirQueAguardaConfirmacao();
        status = StatusAgendamento.CONFIRMADO;
    }

    public void recusar() {
        garantirQueAguardaConfirmacao();
        status = StatusAgendamento.RECUSADO;
    }

    public void concluir(String observacao) {
        garantirQuePodeSerConcluido();
        this.observacao = observacao;
        status = StatusAgendamento.REALIZADO;
    }

    public void garantirQuePodeSerConcluido() {
        if (!podeSerConcluido()) {
            throw new RegraDeNegocioException(
                    "Só é possível registrar atendimentos confirmados e com data até hoje.");
        }
    }

    private void garantirQueAguardaConfirmacao() {
        if (!aguardaConfirmacao()) {
            throw new RegraDeNegocioException("Este agendamento não está mais aguardando confirmação.");
        }
    }

    public Long getId() {
        return id;
    }

    public Pet getPet() {
        return pet;
    }

    public TipoCuidado getTipo() {
        return tipo;
    }

    public LocalDate getData() {
        return data;
    }

    public StatusAgendamento getStatus() {
        return status;
    }

    public String getObservacao() {
        return observacao;
    }
}
