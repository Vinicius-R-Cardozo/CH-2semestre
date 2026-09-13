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

import java.time.LocalDate;

@Entity
@Table(name = "pet")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 60)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Especie especie;

    @Column(nullable = false, length = 60)
    private String raca;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tutor_id")
    private Usuario tutor;

    protected Pet() {
    }

    public Pet(Usuario tutor) {
        this.tutor = tutor;
    }

    public void atualizarDados(String nome, Especie especie, String raca, LocalDate dataNascimento) {
        this.nome = nome;
        this.especie = especie;
        this.raca = raca;
        this.dataNascimento = dataNascimento;
    }

    public FaseVida getFaseVida() {
        return FaseVida.de(especie, dataNascimento, LocalDate.now());
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Especie getEspecie() {
        return especie;
    }

    public String getRaca() {
        return raca;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public Usuario getTutor() {
        return tutor;
    }
}
