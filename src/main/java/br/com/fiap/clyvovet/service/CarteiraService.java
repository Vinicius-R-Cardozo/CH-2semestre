package br.com.fiap.clyvovet.service;

import br.com.fiap.clyvovet.dto.CarteiraPet;
import br.com.fiap.clyvovet.dto.ProximoCuidado;
import br.com.fiap.clyvovet.model.Agendamento;
import br.com.fiap.clyvovet.model.Pet;
import br.com.fiap.clyvovet.model.SituacaoCuidado;
import br.com.fiap.clyvovet.model.StatusAgendamento;
import br.com.fiap.clyvovet.model.TipoCuidado;
import br.com.fiap.clyvovet.repository.AgendamentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
public class CarteiraService {

    private final AgendamentoRepository agendamentoRepository;

    public CarteiraService(AgendamentoRepository agendamentoRepository) {
        this.agendamentoRepository = agendamentoRepository;
    }

    @Transactional(readOnly = true)
    public CarteiraPet montar(Pet pet) {
        List<Agendamento> historico = agendamentoRepository
                .findByPetIdAndStatusOrderByDataDesc(pet.getId(), StatusAgendamento.REALIZADO);
        List<Agendamento> emAberto = agendamentoRepository
                .findByPetIdAndStatusIn(pet.getId(), StatusAgendamento.EM_ABERTO);

        List<ProximoCuidado> proximosCuidados = Arrays.stream(TipoCuidado.values())
                .map(tipo -> calcularProximoCuidado(pet, tipo, historico, emAberto))
                .toList();

        return new CarteiraPet(pet, proximosCuidados, historico);
    }

    private ProximoCuidado calcularProximoCuidado(Pet pet, TipoCuidado tipo,
                                                  List<Agendamento> historico, List<Agendamento> emAberto) {
        boolean agendado = emAberto.stream().anyMatch(agendamento -> agendamento.getTipo() == tipo);

        return historico.stream()
                .filter(agendamento -> agendamento.getTipo() == tipo)
                .findFirst()
                .map(ultimo -> {
                    LocalDate dataPrevista = ultimo.getData().plusDays(tipo.intervaloEmDias(pet.getFaseVida()));
                    SituacaoCuidado situacao = SituacaoCuidado.para(dataPrevista, LocalDate.now());
                    return new ProximoCuidado(tipo, ultimo.getData(), dataPrevista, situacao, agendado);
                })
                .orElseGet(() -> ProximoCuidado.semRegistro(tipo, agendado));
    }
}
