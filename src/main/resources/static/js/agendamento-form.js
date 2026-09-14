(() => {
    if (!exigirPerfil('TUTOR')) {
        return;
    }

    const formulario = document.querySelector('#formulario');
    preencherOpcoes(formulario);

    carregarNaTela('pets', pets => {
        if (pets.length === 0) {
            document.querySelector('#sem-pets').hidden = false;
            return;
        }
        const opcoesDePets = pets.map(pet => `<option value="${pet.id}">${escapar(pet.nome)}</option>`).join('');
        formulario.elements.petId.innerHTML = `<option value="">Selecione</option>${opcoesDePets}`;
        formulario.elements.petId.value = parametro('pet') ?? '';
        formulario.elements.tipo.value = parametro('tipo') ?? '';
        formulario.elements.data.value = hojeIso();
        formulario.hidden = false;
    });

    formulario.addEventListener('submit', async evento => {
        evento.preventDefault();
        try {
            await chamarApi('POST', 'agendamentos', lerFormulario(formulario));
            avisarNaProximaPagina('Solicitação enviada! Assim que a clínica confirmar, a situação muda aqui.');
            location.href = 'agendamentos.html';
        } catch (erro) {
            mostrarErros(formulario, erro);
        }
    });
})();
