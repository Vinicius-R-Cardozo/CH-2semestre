package br.com.fiap.clyvovet.service;

import br.com.fiap.clyvovet.exception.RecursoNaoEncontradoException;
import br.com.fiap.clyvovet.model.Pet;
import br.com.fiap.clyvovet.repository.PetRepository;
import br.com.fiap.clyvovet.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetService {

    private final PetRepository petRepository;
    private final UsuarioRepository usuarioRepository;

    public PetService(PetRepository petRepository, UsuarioRepository usuarioRepository) {
        this.petRepository = petRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Pet> listarDoTutor(Long tutorId) {
        return petRepository.findByTutorIdOrderByNome(tutorId);
    }

    public List<Pet> listarTodos() {
        return petRepository.findAllByOrderByNome();
    }

    public Pet buscar(Long petId) {
        return petRepository.findById(petId).orElseThrow(PetService::petNaoEncontrado);
    }

    public Pet buscarDoTutor(Long tutorId, Long petId) {
        return petRepository.findByIdAndTutorId(petId, tutorId).orElseThrow(PetService::petNaoEncontrado);
    }

    public Pet cadastrar(Long tutorId, Pet pet) {
        pet.setId(null);
        pet.setTutor(usuarioRepository.findById(tutorId).orElseThrow());
        return petRepository.save(pet);
    }

    public Pet atualizar(Long tutorId, Long petId, Pet pet) {
        Pet existente = buscarDoTutor(tutorId, petId);
        pet.setId(existente.getId());
        pet.setTutor(existente.getTutor());
        return petRepository.save(pet);
    }

    public void excluir(Long tutorId, Long petId) {
        petRepository.delete(buscarDoTutor(tutorId, petId));
    }

    private static RecursoNaoEncontradoException petNaoEncontrado() {
        return new RecursoNaoEncontradoException("Pet não encontrado.");
    }
}
