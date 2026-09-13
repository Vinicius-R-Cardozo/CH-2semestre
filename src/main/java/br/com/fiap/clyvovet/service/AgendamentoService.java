package br.com.fiap.clyvovet.service;

import br.com.fiap.clyvovet.dto.AgendamentoForm;
import br.com.fiap.clyvovet.exception.RecursoNaoEncontradoException;
import br.com.fiap.clyvovet.exception.RegraDeNegocioException;
import br.com.fiap.clyvovet.model.Agendamento;
import br.com.fiap.clyvovet.model.Pet;
import br.com.fiap.clyvovet.model.StatusAgendamento;
import br.com.fiap.clyvovet.repository.AgendamentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final PetService petService;

    public AgendamentoService(AgendamentoRepository agendamentoRepository, PetService petService) {
        this.agendamentoRepository = agendamentoRepository;
        this.petService = petService;
    }

    @Transactional
    public void solicitar(Long tutorId, AgendamentoForm form) {
        Pet pet = petService.buscarDoTutor(tutorId, form.getPetId());
        boolean jaExisteEmAberto = agendamentoRepository
                .existsByPetIdAndTipoAndStatusIn(pet.getId(), form.getTipo(), StatusAgendamento.EM_ABERTO);
        if (jaExisteEmAberto) {
            throw new RegraDeNegocioException("%s já tem um agendamento de %s em aberto."
                    .formatted(pet.getNome(), form.getTipo().getDescricao().toLowerCase()));
        }
        agendamentoRepository.save(new Agendamento(pet, form.getTipo(), form.getData()));
    }

    @Transactional(readOnly = true)
    public List<Agendamento> listarDoTutor(Long tutorId) {
        return agendamentoRepository.findByPetTutorIdOrderByDataDesc(tutorId);
    }

    @Transactional(readOnly = true)
    public List<Agendamento> listarEmAberto() {
        return agendamentoRepository.findByStatusInOrderByData(StatusAgendamento.EM_ABERTO);
    }

    @Transactional(readOnly = true)
    public Agendamento buscar(Long id) {
        return agendamentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Agendamento não encontrado."));
    }

    @Transactional(readOnly = true)
    public Agendamento buscarParaConclusao(Long id) {
        Agendamento agendamento = buscar(id);
        agendamento.garantirQuePodeSerConcluido();
        return agendamento;
    }

    @Transactional
    public void confirmar(Long id) {
        buscar(id).confirmar();
    }

    @Transactional
    public void recusar(Long id) {
        buscar(id).recusar();
    }

    @Transactional
    public Agendamento concluir(Long id, String observacao) {
        Agendamento agendamento = buscar(id);
        agendamento.concluir(observacao);
        return agendamento;
    }
}
