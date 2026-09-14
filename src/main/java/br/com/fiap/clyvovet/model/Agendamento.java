package br.com.fiap.clyvovet.model;

import br.com.fiap.clyvovet.exception.RegraDeNegocioException;
import com.fasterxml.jackson.annotation.JsonProperty;
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
import lombok.Data;

import java.time.LocalDate;

@Data
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
    private StatusAgendamento status = StatusAgendamento.SOLICITADO;

    @Column(length = 500)
    private String observacao;

    @JsonProperty
    public boolean aguardaConfirmacao() {
        return status == StatusAgendamento.SOLICITADO;
    }

    @JsonProperty
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
        if (!podeSerConcluido()) {
            throw new RegraDeNegocioException(
                    "Só é possível registrar atendimentos confirmados e com data até hoje.");
        }
        this.observacao = observacao;
        status = StatusAgendamento.REALIZADO;
    }

    private void garantirQueAguardaConfirmacao() {
        if (!aguardaConfirmacao()) {
            throw new RegraDeNegocioException("Este agendamento não está mais aguardando confirmação.");
        }
    }
}
