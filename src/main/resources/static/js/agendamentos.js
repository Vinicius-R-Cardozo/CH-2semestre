(() => {
    if (!exigirPerfil('TUTOR')) {
        return;
    }

    carregarNaTela('agendamentos', agendamentos => renderizarTabela(document.querySelector('#lista'), agendamentos,
        'Nenhum agendamento por aqui ainda.',
        ['Data', 'Pet', 'Cuidado', 'Situação', 'Observação da clínica'], agendamento => `
        <tr>
            <td>${formatarData(agendamento.data)}</td>
            <td><a href="carteira.html?pet=${agendamento.pet.id}">${escapar(agendamento.pet.nome)}</a></td>
            <td>${ROTULOS[agendamento.tipo]}</td>
            <td>${selo(agendamento.status)}</td>
            <td>${escapar(agendamento.observacao) || '—'}</td>
        </tr>`));
})();
