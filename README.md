# Clyvo Vet — Jornada contínua de cuidados do pet

Aplicação web em Spring Boot desenvolvida para a disciplina **Java Advanced** (Sprint 3) do Challenge FIAP 2026, proposto pela **CLYVO VET**.

## O problema

O tutor costuma procurar a clínica só em urgências ou em gatilhos óbvios, como a vacinação. Vermífugos, check-ups e retornos acabam esquecidos, e a clínica perde o acompanhamento do animal ao longo da vida.

## A solução

O Clyvo Vet organiza a vida clínica do pet em uma **carteira de cuidados**:

- cada atendimento realizado entra no histórico do pet;
- a próxima data de cada cuidado (vacinação, vermífugo e check-up) é calculada automaticamente conforme a **espécie** e a **fase de vida** do animal (filhote, adulto ou sênior);
- o tutor vê o que está **em dia**, **próximo** ou **atrasado** e já solicita o agendamento;
- a clínica confirma as solicitações e registra os atendimentos, fechando o ciclo de cuidado.

## Tecnologias

- Java 17
- Spring Boot 4.0.6 (Spring MVC, Spring Data JPA e Bean Validation)
- Spring Security (HTTP Basic e senhas com BCrypt)
- Flyway
- Banco H2 em arquivo
- Lombok
- HTML, CSS e JavaScript no frontend
- Maven (com Maven Wrapper)

## Como executar

### Pré-requisito

- JDK 17 ou superior, com a variável `JAVA_HOME` apontando para ele.

Não é preciso instalar o Maven: o projeto usa o Maven Wrapper.

### Passos

1. Clone o repositório e entre na pasta:

   ```bash
   git clone https://github.com/Vinicius-R-Cardozo/CH-2semestre.git
   cd CH-2semestre
   ```

2. Inicie a aplicação.

   Windows:

   ```bash
   .\mvnw.cmd spring-boot:run
   ```

   Linux ou macOS:

   ```bash
   ./mvnw spring-boot:run
   ```

3. Acesse **http://localhost:8080** e entre com um dos usuários de teste abaixo.

Na primeira execução o Flyway cria as tabelas e insere os dados de demonstração. O banco fica na pasta `data/`; para recomeçar do zero, pare a aplicação e apague essa pasta.

## Usuários de teste

| Perfil      | E-mail            | Senha    |
|-------------|-------------------|----------|
| Veterinário | vet@clyvo.com     | vet123   |
| Tutor       | carlos@clyvo.com  | tutor123 |
| Tutor       | mariana@clyvo.com | tutor123 |

Carlos é tutor do Thor e da Luna; Mariana é tutora da Pipoca.

## Requisitos da Sprint 3

### 1. Frontend

As telas ficam em `src/main/resources/static`. São páginas HTML com JavaScript que consomem a API do próprio projeto com `fetch`:

| Página                  | Perfil              | O que mostra                                     |
|-------------------------|---------------------|--------------------------------------------------|
| `index.html`            | todos               | login                                            |
| `pets.html`             | tutor               | lista de pets, com editar e excluir              |
| `pet-form.html`         | tutor               | cadastro e edição de pet                         |
| `carteira.html`         | tutor e veterinário | próximos cuidados e histórico do pet             |
| `agendamentos.html`     | tutor               | agendamentos solicitados e situação de cada um   |
| `agendamento-form.html` | tutor               | solicitação de agendamento                       |
| `agenda.html`           | veterinário         | solicitações para confirmar e atendimentos       |
| `conclusao.html`        | veterinário         | registro do atendimento                          |
| `pacientes.html`        | veterinário         | todos os pets da clínica                         |

O menu muda conforme o perfil logado.

### 2. Flyway

As migrações ficam em `src/main/resources/db/migration`:

| Versão | Arquivo                            | O que faz                                         |
|--------|------------------------------------|---------------------------------------------------|
| V1     | `V1__criar_tabela_usuario.sql`     | cria a tabela `usuario`                           |
| V2     | `V2__criar_tabela_pet.sql`         | cria a tabela `pet`                               |
| V3     | `V3__criar_tabela_agendamento.sql` | cria a tabela `agendamento`                       |
| V4     | `V4__inserir_dados_iniciais.sql`   | insere usuários, pets e histórico de demonstração |

O Hibernate apenas valida o esquema (`spring.jpa.hibernate.ddl-auto=validate`); quem cria e altera as tabelas é o Flyway.

### 3. Spring Security

A autenticação é **HTTP Basic**, sem sessão. A tela de login envia e-mail e senha para `POST /login`, e as demais chamadas da API levam o mesmo cabeçalho `Authorization`.

Dois perfis com permissões diferentes, configurados em `SecurityConfig`:

| Perfil        | Rotas liberadas                                                              |
|---------------|------------------------------------------------------------------------------|
| `TUTOR`       | `/pets/**`, `GET /agendamentos` e `POST /agendamentos`                       |
| `VETERINARIO` | `GET /agendamentos/abertos`, `GET /agendamentos/{id}`, os `PATCH` de agendamento e `/pacientes/**` |

- Sem login a API responde **401**; com o perfil errado, **403**.
- Nas telas, quem abre uma página de outro perfil vê a mensagem de acesso negado.
- O tutor só enxerga os próprios pets: pedir o pet de outro tutor retorna **404**.
- As senhas são armazenadas com BCrypt.

### 4. Fluxos completos

**Fluxo 1 — Solicitação e confirmação de agendamento**

1. O tutor abre a carteira do pet, vê um cuidado atrasado ou próximo e clica em **Agendar** (ou usa **Solicitar agendamento** em Agendamentos).
2. O agendamento é criado com a situação **Solicitado**. Não é possível ter duas solicitações em aberto do mesmo cuidado para o mesmo pet.
3. O veterinário vê a solicitação na **Agenda** e clica em **Confirmar** ou **Recusar**.
4. O tutor acompanha a nova situação em **Agendamentos**.

**Fluxo 2 — Registro do atendimento e cálculo do próximo cuidado**

1. Com o agendamento confirmado e a data alcançada, o veterinário clica em **Registrar atendimento** e descreve o que foi feito.
2. O agendamento passa a **Realizado** e entra no histórico do pet.
3. A carteira recalcula a próxima data daquele cuidado conforme a fase de vida do pet e atualiza a situação (**Em dia**, **Próximo** ou **Atrasado**).
4. O tutor vê a nova data prevista na carteira do pet.

Intervalos usados no cálculo, em dias:

| Cuidado   | Filhote | Adulto | Sênior |
|-----------|---------|--------|--------|
| Vacinação | 30      | 365    | 365    |
| Vermífugo | 30      | 90     | 90     |
| Check-up  | 90      | 365    | 180    |

O pet é filhote até completar 1 ano. Cães passam a sêniores aos 7 anos e gatos aos 10. Um cuidado fica **Próximo** quando a data prevista cai nos próximos 30 dias.

### 5. Validações

- Bean Validation com mensagens em português na entidade `Pet` e nos records de requisição do `AgendamentoController`: campos obrigatórios, tamanho máximo, data de nascimento que não pode estar no futuro e data de agendamento que não pode estar no passado.
- Os erros voltam com status **400** e aparecem embaixo de cada campo do formulário.
- Regras de negócio: só é possível confirmar ou recusar um agendamento **Solicitado**, só é possível registrar atendimento de um agendamento **Confirmado** com data até hoje e não se abre uma segunda solicitação do mesmo cuidado para o mesmo pet.

## Rotas da API

| Método | Rota                           | Perfil      | O que faz                                    | Status        |
|--------|--------------------------------|-------------|----------------------------------------------|---------------|
| POST   | `/login`                       | todos       | confere o login e devolve nome e perfil      | 200, 401      |
| GET    | `/pets`                        | tutor       | lista os pets do tutor                       | 200           |
| GET    | `/pets/{id}`                   | tutor       | busca um pet                                 | 200, 404      |
| POST   | `/pets`                        | tutor       | cadastra um pet                              | 201, 400      |
| PUT    | `/pets/{id}`                   | tutor       | atualiza um pet                              | 200, 400, 404 |
| DELETE | `/pets/{id}`                   | tutor       | exclui um pet                                | 204, 404      |
| GET    | `/pets/{id}/carteira`          | tutor       | próximos cuidados e histórico                | 200, 404      |
| GET    | `/agendamentos`                | tutor       | lista os agendamentos do tutor               | 200           |
| POST   | `/agendamentos`                | tutor       | solicita um agendamento                      | 201, 400, 404 |
| GET    | `/agendamentos/abertos`        | veterinário | agendamentos solicitados e confirmados       | 200           |
| GET    | `/agendamentos/{id}`           | veterinário | busca um agendamento                         | 200, 404      |
| PATCH  | `/agendamentos/{id}/confirmar` | veterinário | confirma uma solicitação                     | 200, 400, 404 |
| PATCH  | `/agendamentos/{id}/recusar`   | veterinário | recusa uma solicitação                       | 200, 400, 404 |
| PATCH  | `/agendamentos/{id}/concluir`  | veterinário | registra o atendimento                       | 200, 400, 404 |
| GET    | `/pacientes`                   | veterinário | lista todos os pets                          | 200           |
| GET    | `/pacientes/{id}/carteira`     | veterinário | carteira de qualquer pet                     | 200, 404      |

Para testar no Postman ou no Insomnia, use *Auth → Basic Auth* com um dos usuários de teste.

### Corpos das requisições

Cadastrar ou atualizar pet (`especie`: `CAO` ou `GATO`):

```json
{
  "nome": "Bidu",
  "especie": "CAO",
  "raca": "SRD",
  "dataNascimento": "2024-02-10"
}
```

Solicitar agendamento (`tipo`: `VACINA`, `VERMIFUGO` ou `CHECKUP`; `data`: hoje ou uma data futura):

```json
{
  "petId": 2,
  "tipo": "VERMIFUGO",
  "data": "2026-12-01"
}
```

Registrar atendimento:

```json
{
  "observacao": "Vermífugo aplicado, sem reações."
}
```

### Erros

Dados inválidos voltam com **400** e a lista de campos:

```json
[
  { "campo": "nome", "mensagem": "Informe o nome do pet." }
]
```

Regras de negócio voltam com **400** e recursos não encontrados com **404**, no formato:

```json
{ "mensagem": "Luna já tem um agendamento de vermífugo em aberto." }
```

## Estrutura do projeto

```text
src/main/java/br/com/fiap/clyvovet
├── controller     AuthController, PetController, AgendamentoController e PacienteController
├── exception      exceções e tratamento dos erros da API
├── model          entidades JPA, enums e records do domínio
├── repository     repositórios Spring Data JPA
├── security       configuração do Spring Security e usuário autenticado
└── service        regras de negócio

src/main/resources
├── db/migration   migrações Flyway
└── static         páginas HTML, CSS e JavaScript
```
