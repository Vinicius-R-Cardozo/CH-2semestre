(() => {
    const sessao = exigirPerfil('TUTOR', 'VETERINARIO');
    if (!sessao) {
        return;
    }

    const petId = parametro('pet');
    const ehTutor = sessao.perfil === 'TUTOR';

    function acaoDoTutor(cuidado) {
        if (cuidado.agendado) {
            return '<span class="selo selo-confirmado">Agendado</span>';
        }
        return `<a class="botao" href="agendamento-form.html?pet=${petId}&tipo=${cuidado.tipo}">Agendar</a>`;
    }

    function exibir(carteira) {
        const pet = carteira.pet;
        document.title = `${pet.nome} · Clyvo Vet`;
        document.querySelector('#nome-pet').textContent = pet.nome;
        document.querySelector('#detalhes-pet').textContent =
            `${ROTULOS[pet.especie]} · ${pet.raca} · ${ROTULOS[pet.faseVida]}`;
        if (!ehTutor) {
            document.querySelector('#tutor-pet').textContent = `Tutor: ${pet.tutor.nome}`;
        }

        const colunas = ['Cuidado', 'Última vez', 'Previsto para', 'Situação'];
        renderizarTabela(document.querySelector('#proximos'), carteira.proximosCuidados, '',
            ehTutor ? [...colunas, ''] : colunas, cuidado => `
            <tr>
                <td>${ROTULOS[cuidado.tipo]}</td>
                <td>${formatarData(cuidado.ultimaRealizacao)}</td>
                <td>${cuidado.dataPrevista ? formatarData(cuidado.dataPrevista) : 'Ainda não realizado'}</td>
                <td>${selo(cuidado.situacao)}</td>
                ${ehTutor ? `<td class="acoes">${acaoDoTutor(cuidado)}</td>` : ''}
            </tr>`);

        renderizarTabela(document.querySelector('#historico'), carteira.historico,
            'Nenhum atendimento registrado ainda.',
            ['Data', 'Cuidado', 'Observação da clínica'], atendimento => `
            <tr>
                <td>${formatarData(atendimento.data)}</td>
                <td>${ROTULOS[atendimento.tipo]}</td>
                <td>${escapar(atendimento.observacao)}</td>
            </tr>`);
    }

    carregarNaTela(ehTutor ? `pets/${petId}/carteira` : `pacientes/${petId}/carteira`, exibir);
})();
