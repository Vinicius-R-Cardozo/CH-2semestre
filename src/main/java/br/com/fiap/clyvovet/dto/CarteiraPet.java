package br.com.fiap.clyvovet.dto;

import br.com.fiap.clyvovet.model.Agendamento;
import br.com.fiap.clyvovet.model.Pet;

import java.util.List;

public record CarteiraPet(Pet pet, List<ProximoCuidado> proximosCuidados, List<Agendamento> historico) {
}
