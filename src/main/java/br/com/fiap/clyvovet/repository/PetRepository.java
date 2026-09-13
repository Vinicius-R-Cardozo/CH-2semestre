package br.com.fiap.clyvovet.repository;

import br.com.fiap.clyvovet.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PetRepository extends JpaRepository<Pet, Long> {

    List<Pet> findByTutorIdOrderByNome(Long tutorId);

    List<Pet> findAllByOrderByNome();

    Optional<Pet> findByIdAndTutorId(Long id, Long tutorId);
}
