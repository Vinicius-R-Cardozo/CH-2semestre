package br.com.fiap.clyvovet.model;

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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "pet")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Informe o nome do pet.")
    @Size(max = 60, message = "O nome deve ter no máximo 60 caracteres.")
    @Column(nullable = false, length = 60)
    private String nome;

    @NotNull(message = "Selecione a espécie.")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Especie especie;

    @NotBlank(message = "Informe a raça. Se não tiver raça definida, use SRD.")
    @Size(max = 60, message = "A raça deve ter no máximo 60 caracteres.")
    @Column(nullable = false, length = 60)
    private String raca;

    @NotNull(message = "Informe a data de nascimento.")
    @PastOrPresent(message = "A data de nascimento não pode estar no futuro.")
    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tutor_id")
    private Usuario tutor;

    public FaseVida getFaseVida() {
        return FaseVida.de(especie, dataNascimento, LocalDate.now());
    }
}
