package br.com.fiap.clyvovet.service;

import br.com.fiap.clyvovet.dto.PetForm;
import br.com.fiap.clyvovet.exception.RecursoNaoEncontradoException;
import br.com.fiap.clyvovet.model.Pet;
import br.com.fiap.clyvovet.repository.PetRepository;
import br.com.fiap.clyvovet.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PetService {

    private final PetRepository petRepository;
    private final UsuarioRepository usuarioRepository;

    public PetService(PetRepository petRepository, UsuarioRepository usuarioRepository) {
        this.petRepository = petRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public List<Pet> listarDoTutor(Long tutorId) {
        return petRepository.findByTutorIdOrderByNome(tutorId);
    }

    @Transactional(readOnly = true)
    public List<Pet> listarTodos() {
        return petRepository.findAllByOrderByNome();
    }

    @Transactional(readOnly = true)
    public Pet buscar(Long petId) {
        return petRepository.findById(petId).orElseThrow(PetService::petNaoEncontrado);
    }

    @Transactional(readOnly = true)
    public Pet buscarDoTutor(Long tutorId, Long petId) {
        return petRepository.findByIdAndTutorId(petId, tutorId).orElseThrow(PetService::petNaoEncontrado);
    }

    @Transactional
    public void cadastrar(Long tutorId, PetForm form) {
        Pet pet = new Pet(usuarioRepository.getReferenceById(tutorId));
        form.aplicarEm(pet);
        petRepository.save(pet);
    }

    @Transactional
    public void atualizar(Long tutorId, Long petId, PetForm form) {
        form.aplicarEm(buscarDoTutor(tutorId, petId));
    }

    @Transactional
    public void excluir(Long tutorId, Long petId) {
        petRepository.delete(buscarDoTutor(tutorId, petId));
    }

    private static RecursoNaoEncontradoException petNaoEncontrado() {
        return new RecursoNaoEncontradoException("Pet não encontrado.");
    }
}
