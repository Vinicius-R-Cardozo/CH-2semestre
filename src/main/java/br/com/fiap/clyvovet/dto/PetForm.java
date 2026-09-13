package br.com.fiap.clyvovet.dto;

import br.com.fiap.clyvovet.model.Especie;
import br.com.fiap.clyvovet.model.Pet;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class PetForm {

    private Long id;

    @NotBlank(message = "Informe o nome do pet.")
    @Size(max = 60, message = "O nome deve ter no máximo 60 caracteres.")
    private String nome;

    @NotNull(message = "Selecione a espécie.")
    private Especie especie;

    @NotBlank(message = "Informe a raça. Se não tiver raça definida, use SRD.")
    @Size(max = 60, message = "A raça deve ter no máximo 60 caracteres.")
    private String raca;

    @NotNull(message = "Informe a data de nascimento.")
    @PastOrPresent(message = "A data de nascimento não pode estar no futuro.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dataNascimento;

    public static PetForm de(Pet pet) {
        PetForm form = new PetForm();
        form.id = pet.getId();
        form.nome = pet.getNome();
        form.especie = pet.getEspecie();
        form.raca = pet.getRaca();
        form.dataNascimento = pet.getDataNascimento();
        return form;
    }

    public void aplicarEm(Pet pet) {
        pet.atualizarDados(nome.strip(), especie, raca.strip(), dataNascimento);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Especie getEspecie() {
        return especie;
    }

    public void setEspecie(Especie especie) {
        this.especie = especie;
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
}
