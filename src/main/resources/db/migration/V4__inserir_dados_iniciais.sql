INSERT INTO usuario (nome, email, senha, perfil) VALUES
    ('Dra. Ana Souza', 'vet@clyvo.com', '$2a$10$OPwQGJHtUm/ws9fq97OGcekAfNVUcr854ZtAEK.4LwFtu6FjD3OY.', 'VETERINARIO'),
    ('Carlos Lima', 'carlos@clyvo.com', '$2a$10$EfyVv10/utoC8w3fcD/D4uyE9lGiPqiD2jDpad9f6aJFjims1Uo3e', 'TUTOR'),
    ('Mariana Alves', 'mariana@clyvo.com', '$2a$10$C0QmpR6FUd0VznBoE.GH5.hSeG8ZBf94lxU90yMVoxoCLdm/TjDTa', 'TUTOR');

INSERT INTO pet (nome, especie, raca, data_nascimento, tutor_id) VALUES
    ('Thor', 'CAO', 'Golden Retriever', CURRENT_DATE - INTERVAL '8' YEAR,
        (SELECT id FROM usuario WHERE email = 'carlos@clyvo.com')),
    ('Luna', 'GATO', 'Siamês', CURRENT_DATE - INTERVAL '3' YEAR,
        (SELECT id FROM usuario WHERE email = 'carlos@clyvo.com')),
    ('Pipoca', 'CAO', 'SRD', CURRENT_DATE - INTERVAL '5' MONTH,
        (SELECT id FROM usuario WHERE email = 'mariana@clyvo.com'));

INSERT INTO agendamento (pet_id, tipo, data, status, observacao) VALUES
    ((SELECT id FROM pet WHERE nome = 'Thor'), 'VACINA', CURRENT_DATE - INTERVAL '400' DAY, 'REALIZADO',
        'Vacina V10 aplicada, sem reações.'),
    ((SELECT id FROM pet WHERE nome = 'Thor'), 'VERMIFUGO', CURRENT_DATE - INTERVAL '70' DAY, 'REALIZADO',
        'Vermífugo oral na dose para 32 kg.'),
    ((SELECT id FROM pet WHERE nome = 'Thor'), 'CHECKUP', CURRENT_DATE - INTERVAL '100' DAY, 'REALIZADO',
        'Exames de sangue normais para a idade. Manter acompanhamento articular.'),
    ((SELECT id FROM pet WHERE nome = 'Thor'), 'VACINA', CURRENT_DATE, 'CONFIRMADO', NULL),
    ((SELECT id FROM pet WHERE nome = 'Luna'), 'VACINA', CURRENT_DATE - INTERVAL '200' DAY, 'REALIZADO',
        'Vacina quádrupla felina aplicada.'),
    ((SELECT id FROM pet WHERE nome = 'Luna'), 'VERMIFUGO', CURRENT_DATE - INTERVAL '100' DAY, 'REALIZADO',
        'Vermífugo em pasta.'),
    ((SELECT id FROM pet WHERE nome = 'Luna'), 'CHECKUP', CURRENT_DATE + INTERVAL '3' DAY, 'SOLICITADO', NULL),
    ((SELECT id FROM pet WHERE nome = 'Pipoca'), 'VACINA', CURRENT_DATE - INTERVAL '20' DAY, 'REALIZADO',
        'Primeira dose da V10.'),
    ((SELECT id FROM pet WHERE nome = 'Pipoca'), 'VERMIFUGO', CURRENT_DATE - INTERVAL '25' DAY, 'REALIZADO',
        'Vermífugo para filhote.');
