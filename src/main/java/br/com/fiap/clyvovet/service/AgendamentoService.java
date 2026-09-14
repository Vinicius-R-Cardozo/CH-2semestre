package br.com.fiap.clyvovet.service;

import br.com.fiap.clyvovet.exception.RecursoNaoEncontradoException;
import br.com.fiap.clyvovet.exception.RegraDeNegocioException;
import br.com.fiap.clyvovet.model.Agendamento;
import br.com.fiap.clyvovet.model.Pet;
import br.com.fiap.clyvovet.model.StatusAgendamento;
import br.com.fiap.clyvovet.model.TipoCuidado;
import br.com.fiap.clyvovet.repository.AgendamentoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final PetService petService;

    public AgendamentoService(AgendamentoRepository agendamentoRepository, PetService petService) {
        this.agendamentoRepository = agendamentoRepository;
        this.petService = petService;
    }

    public Agendamento solicitar(Long tutorId, Long petId, TipoCuidado tipo, LocalDate data) {
        Pet pet = petService.buscarDoTutor(tutorId, petId);
        boolean jaExisteEmAberto = agendamentoRepository
                .existsByPetIdAndTipoAndStatusIn(pet.getId(), tipo, StatusAgendamento.EM_ABERTO);
        if (jaExisteEmAberto) {
            throw new RegraDeNegocioException("%s já tem um agendamento de %s em aberto."
                    .formatted(pet.getNome(), tipo.getDescricao().toLowerCase()));
        }

        Agendamento agendamento = new Agendamento();
        agendamento.setPet(pet);
        agendamento.setTipo(tipo);
        agendamento.setData(data);
        return agendamentoRepository.save(agendamento);
    }

    public List<Agendamento> listarDoTutor(Long tutorId) {
        return agendamentoRepository.findByPetTutorIdOrderByDataDesc(tutorId);
    }

    public List<Agendamento> listarEmAberto() {
        return agendamentoRepository.findByStatusInOrderByData(StatusAgendamento.EM_ABERTO);
    }

    public Agendamento buscar(Long id) {
        return agendamentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Agendamento não encontrado."));
    }

    public Agendamento confirmar(Long id) {
        Agendamento agendamento = buscar(id);
        agendamento.confirmar();
        return agendamentoRepository.save(agendamento);
    }

    public Agendamento recusar(Long id) {
        Agendamento agendamento = buscar(id);
        agendamento.recusar();
        return agendamentoRepository.save(agendamento);
    }

    public Agendamento concluir(Long id, String observacao) {
        Agendamento agendamento = buscar(id);
        agendamento.concluir(observacao.strip());
        return agendamentoRepository.save(agendamento);
    }
}
