(() => {
    if (!exigirPerfil('VETERINARIO')) {
        return;
    }

    carregarNaTela('pacientes', pets => renderizarTabela(document.querySelector('#lista'), pets,
        'Nenhum pet cadastrado.',
        ['Nome', 'Espécie', 'Raça', 'Fase de vida', 'Tutor'], pet => `
        <tr>
            <td><a href="carteira.html?pet=${pet.id}">${escapar(pet.nome)}</a></td>
            <td>${ROTULOS[pet.especie]}</td>
            <td>${escapar(pet.raca)}</td>
            <td>${ROTULOS[pet.faseVida]}</td>
            <td>${escapar(pet.tutor.nome)}</td>
        </tr>`));
})();
