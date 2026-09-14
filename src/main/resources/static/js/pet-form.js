(() => {
    if (!exigirPerfil('TUTOR')) {
        return;
    }

    const formulario = document.querySelector('#formulario');
    const id = parametro('id');
    preencherOpcoes(formulario);

    if (id) {
        document.querySelector('#titulo').textContent = 'Editar pet';
        document.title = 'Editar pet · Clyvo Vet';
        carregarNaTela(`pets/${id}`, pet => {
            ['nome', 'especie', 'raca', 'dataNascimento'].forEach(campo => formulario.elements[campo].value = pet[campo]);
        });
    }

    formulario.addEventListener('submit', async evento => {
        evento.preventDefault();
        try {
            if (id) {
                await chamarApi('PUT', `pets/${id}`, lerFormulario(formulario));
                avisarNaProximaPagina('Dados do pet atualizados.');
            } else {
                await chamarApi('POST', 'pets', lerFormulario(formulario));
                avisarNaProximaPagina('Pet cadastrado com sucesso.');
            }
            location.href = 'pets.html';
        } catch (erro) {
            mostrarErros(formulario, erro);
        }
    });
})();
