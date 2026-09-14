(() => {
    if (!exigirPerfil('VETERINARIO')) {
        return;
    }

    const formulario = document.querySelector('#formulario');
    const id = parametro('id');

    carregarNaTela(`agendamentos/${id}`, agendamento => {
        document.querySelector('#resumo').textContent =
            `${ROTULOS[agendamento.tipo]} de ${agendamento.pet.nome} · ${formatarData(agendamento.data)}`;
    });

    formulario.addEventListener('submit', async evento => {
        evento.preventDefault();
        try {
            const agendamento = await chamarApi('PATCH', `agendamentos/${id}/concluir`, lerFormulario(formulario));
            avisarNaProximaPagina('Atendimento registrado. A carteira de cuidados foi atualizada.');
            location.href = `carteira.html?pet=${agendamento.pet.id}`;
        } catch (erro) {
            mostrarErros(formulario, erro);
        }
    });
})();
