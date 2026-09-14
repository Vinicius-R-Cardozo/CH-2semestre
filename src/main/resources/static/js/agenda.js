(() => {
    if (!exigirPerfil('VETERINARIO')) {
        return;
    }

    const lista = document.querySelector('#lista');
    const MENSAGENS = { confirmar: 'Agendamento confirmado.', recusar: 'Solicitação recusada.' };

    function acoes(agendamento) {
        if (agendamento.aguardaConfirmacao) {
            return `
                <button type="button" class="botao" data-acao="confirmar" data-id="${agendamento.id}">Confirmar</button>
                <button type="button" class="botao botao-perigo" data-acao="recusar" data-id="${agendamento.id}">Recusar</button>`;
        }
        if (agendamento.podeSerConcluido) {
            return `<a class="botao" href="conclusao.html?id=${agendamento.id}">Registrar atendimento</a>`;
        }
        return '';
    }

    function exibir(agendamentos) {
        renderizarTabela(lista, agendamentos, 'Nenhum agendamento em aberto.',
            ['Data', 'Pet', 'Tutor', 'Cuidado', 'Situação', ''], agendamento => `
            <tr>
                <td>${formatarData(agendamento.data)}</td>
                <td><a href="carteira.html?pet=${agendamento.pet.id}">${escapar(agendamento.pet.nome)}</a></td>
                <td>${escapar(agendamento.pet.tutor.nome)}</td>
                <td>${ROTULOS[agendamento.tipo]}</td>
                <td>${selo(agendamento.status)}</td>
                <td class="acoes">${acoes(agendamento)}</td>
            </tr>`);
    }

    lista.addEventListener('click', async evento => {
        const { acao, id } = evento.target.dataset;
        if (!acao || (acao === 'recusar' && !confirm('Recusar esta solicitação?'))) {
            return;
        }
        try {
            await chamarApi('PATCH', `agendamentos/${id}/${acao}`);
            mostrarAviso(MENSAGENS[acao]);
            await carregarNaTela('agendamentos/abertos', exibir);
        } catch (erro) {
            mostrarAviso(erro.message, 'erro');
        }
    });

    carregarNaTela('agendamentos/abertos', exibir);
})();
