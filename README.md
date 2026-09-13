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
- Spring Security (login por formulário e senhas com BCrypt)
- Thymeleaf e Thymeleaf Extras Spring Security
- Flyway
- Banco H2 em arquivo
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

Páginas renderizadas no servidor com Thymeleaf: login, lista e cadastro de pets, carteira de cuidados, solicitação e acompanhamento de agendamentos, agenda da clínica, registro de atendimento e lista de pacientes. O menu e as ações mudam conforme o perfil logado.

### 2. Flyway

As migrações ficam em `src/main/resources/db/migration`:

| Versão | Arquivo                            | O que faz                                        |
|--------|------------------------------------|--------------------------------------------------|
| V1     | `V1__criar_tabela_usuario.sql`     | cria a tabela `usuario`                          |
| V2     | `V2__criar_tabela_pet.sql`         | cria a tabela `pet`                              |
| V3     | `V3__criar_tabela_agendamento.sql` | cria a tabela `agendamento`                      |
| V4     | `V4__inserir_dados_iniciais.sql`   | insere usuários, pets e histórico de demonstração |

O Hibernate apenas valida o esquema (`spring.jpa.hibernate.ddl-auto=validate`); quem cria e altera as tabelas é o Flyway.

### 3. Spring Security

Dois perfis de usuário com permissões diferentes:

| Perfil        | Rotas             | O que pode fazer                                                              |
|---------------|-------------------|-------------------------------------------------------------------------------|
| `TUTOR`       | `/tutor/**`       | cadastrar os próprios pets, ver a carteira de cuidados e solicitar agendamentos |
| `VETERINARIO` | `/veterinario/**` | confirmar ou recusar solicitações, registrar atendimentos e consultar pacientes |

- As rotas são protegidas por perfil em `SecurityConfig`. Um tutor que tenta abrir `/veterinario/agendamentos` recebe a página de acesso negado (403).
- O tutor só enxerga os próprios pets: abrir pela URL o pet de outro tutor retorna página não encontrada (404).
- As senhas são armazenadas com BCrypt.

### 4. Fluxos completos

**Fluxo 1 — Solicitação e confirmação de agendamento**

1. O tutor abre a carteira do pet, vê um cuidado atrasado ou próximo e clica em **Agendar** (ou usa **Solicitar agendamento** no menu Agendamentos).
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

- Formulários validados com Bean Validation e mensagens em português: campos obrigatórios, tamanho máximo, data de nascimento que não pode estar no futuro e data de agendamento que não pode estar no passado.
- Regras de negócio no domínio: só é possível confirmar ou recusar um agendamento **Solicitado**, só é possível registrar atendimento de um agendamento **Confirmado** com data até hoje e não se abre uma segunda solicitação do mesmo cuidado para o mesmo pet.

## Estrutura do projeto

```text
src/main/java/br/com/fiap/clyvovet
├── controller   rotas de login, do tutor e do veterinário
├── dto          formulários e dados exibidos nas telas
├── exception    exceções de regra de negócio e de recurso não encontrado
├── model        entidades JPA e enums do domínio
├── repository   repositórios Spring Data JPA
├── security     configuração do Spring Security e usuário autenticado
└── service      regras de negócio

src/main/resources
├── db/migration migrações Flyway
├── static/css   estilos
└── templates    páginas Thymeleaf
```
