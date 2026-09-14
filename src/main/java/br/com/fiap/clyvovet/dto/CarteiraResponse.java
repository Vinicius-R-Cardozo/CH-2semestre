package br.com.fiap.clyvovet.dto;

import java.util.List;

public record CarteiraResponse(
        PetResponse pet,
        List<ProximoCuidado> proximosCuidados,
        List<AgendamentoResponse> historico) {

    public static CarteiraResponse de(CarteiraPet carteira) {
        return new CarteiraResponse(
                PetResponse.de(carteira.pet()),
                carteira.proximosCuidados(),
                AgendamentoResponse.listaDe(carteira.historico()));
    }
}
