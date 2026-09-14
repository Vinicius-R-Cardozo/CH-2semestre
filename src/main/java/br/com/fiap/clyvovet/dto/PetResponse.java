package br.com.fiap.clyvovet.dto;

import br.com.fiap.clyvovet.model.Especie;
import br.com.fiap.clyvovet.model.FaseVida;
import br.com.fiap.clyvovet.model.Pet;

import java.time.LocalDate;
import java.util.List;

public record PetResponse(
        Long id,
        String nome,
        Especie especie,
        String raca,
        LocalDate dataNascimento,
        FaseVida faseVida) {

    public static PetResponse de(Pet pet) {
        return new PetResponse(pet.getId(), pet.getNome(), pet.getEspecie(), pet.getRaca(),
                pet.getDataNascimento(), pet.getFaseVida());
    }

    public static List<PetResponse> listaDe(List<Pet> pets) {
        return pets.stream().map(PetResponse::de).toList();
    }
}
