# Arquitetura do backend

API REST do Diário de Treino em Spring Boot 3.5, Java 17, MapStruct e JWT. Mesmo padrão de pastas do backend Wezt.
Este documento vale sobre o `AGENTS.md` em caso de conflito.

## Estrutura de um módulo

```
core/<modulo>/
├── controllers/            # REST: só recebe, valida (@Valid) e delega ao service
├── services/               # interface (o contrato: <Modulo>Service)
│   └── impl/               # implementação (<Modulo>ServicePadrao) com as regras de negócio
├── domains/
│   ├── dtos/               # <Nome>InDTO (entrada) e <Nome>OutDTO (saída): classes com Lombok @Data, sem records
│   └── mappers/            # MapStruct: <Nome>Mapper extends BaseMapper<Entidade, InDTO, OutDTO>
├── repositorys/            # Spring Data (estende RepositorioDoUsuario quando a entidade é do usuário)
└── util/                   # regras puras, sem Spring (opcional)
```

## Compartilhado em `core/`

| Pasta | O que tem |
|---|---|
| `entidades/` e `entidades/enums/` | todas as entidades JPA e enums. `EntidadeDoUsuario` é a base das entidades que pertencem a alguém (id, usuarioId, criadoEm) |
| `domains/mappers/` | `BaseMapper<E, IN, OUT>` (entrada e saída), `SaidaMapper<E, OUT>` (só saída) e `MapperConfigPadrao` (componentModel spring, ignora campos sem par, nulos não apagam na atualização) |
| `repositorys/` | `RepositorioDoUsuario<E>`: `findByIdAndUsuarioId` para todo repositório de entidade do usuário |
| `services/` | `CrudService<IN, OUT>` e `CrudServicePadrao` (Template Method: buscar, criar, atualizar, excluir já filtrando pelo usuário; ganchos `validar` e `paraSaida`) |
| `exception/` | `NotFoundException` (404), `BadRequestException` (400), `MuitasTentativasException` (429) e o `GlobalExceptionHandler` (ProblemDetail) |
| `util/` | `CurrentUser` (usuário do JWT), `Fusos` (America/Sao_Paulo) |
| `auth/security/`, `config/` | JWT (`JwtService`, `JwtAuthFilter`, `UsuarioPrincipal`), `LimitadorTentativas`, `SecurityConfig`, `OpenApiConfig`, catálogo de exercícios e dados de demonstração |

## Módulos

| Módulo | Rotas | Services |
|---|---|---|
| `core/auth` | `POST /api/auth/login`, `POST /api/auth/cadastro`, `GET /api/auth/me` | `AuthService` (RF01) |
| `core/usuario` | (usado pelo auth) | `UsuarioMapper` (só saída), `UsuarioRepository` |
| `core/exercicio` | `GET/POST /api/exercicios` | `ExercicioService` (RF03: catálogo + personalizados) |
| `core/treino` | `GET/POST /api/treinos`, `GET/PUT/DELETE /api/treinos/{id}` | `TreinoService` (RF02/RF03, usa `CrudServicePadrao`) |
| `core/execucao` | `GET/POST /api/sessoes`, `GET/DELETE /api/sessoes/{id}` | `SessaoService` (RF04/RF05) + `util/CalculoVolume` |
| `core/saude` | `GET /api/saude` | (só controller) |

Histórico (RF05) e evolução (RF06) são calculados no app a partir de `GET /api/sessoes`. Se o volume de dados crescer,
crie os módulos `historico` e `evolucao` com consultas agregadas no banco.

## Modelo de dados

```
Usuario 1──* Treino 1──* TreinoExercicio *──1 Exercicio (catálogo: usuarioId nulo; personalizado: usuarioId)
   └──* SessaoTreino 1──* SerieRegistrada (exercicioId, número, carga kg, repetições, concluída)
```

- A sessão guarda `nomeTreino` e `letraTreino` do momento da execução; excluir um treino é lógico (`ativo = false`).
- `ultimaCargaKg` do treino vem da última série concluída do exercício (`SerieRegistradaRepository.ultimaSerie`).

## Regras

- Controller nunca recebe nem devolve entidade: sempre InDTO/OutDTO.
- DTOs são classes (`@Data @NoArgsConstructor @AllArgsConstructor`), não records.
- Controller depende da **interface** do service; a implementação `...ServicePadrao` é o bean.
- `usuarioId` nunca vem do DTO: é preenchido pela `EntidadeDoUsuario` a partir do JWT (`CurrentUser`).
- Cadastro simples: estenda `CrudServicePadrao` e sobrescreva só `validar()`, `paraSaida()` ou `excluir()` (exemplo: Treinos).
- Mapeamento que precisa de dados de outras tabelas (última carga): método `default` no mapper, chamado pelo service
  (exemplo: `TreinoMapper.toDto(treino, ultimasCargas)`).
- Filhos que dependem de validação (exercícios do treino) são ignorados no mapper (`@Mapping(ignore = true)`) e montados no `validar()`.
- Recurso que a API só devolve (sem InDTO): mapper estende `SaidaMapper<Entidade, OutDTO>` (exemplo: Usuário).
- Erro de regra de negócio: `throw new BadRequestException("mensagem para o usuário")`. Recurso de outro usuário: 404.
- Um método `@Transactional` chamado de dentro da própria classe não abre transação (proxy do Spring).

## Criando um módulo novo (passo a passo)

1. Entidade em `core/entidades` (estende `EntidadeDoUsuario` se for do usuário; campos novos com tipos wrapper).
2. `core/<modulo>/domains/dtos`: `<Nome>InDTO` com Bean Validation e mensagens em português; `<Nome>OutDTO`.
3. `core/<modulo>/domains/mappers/<Nome>Mapper` com `@Mapper(config = MapperConfigPadrao.class)` estendendo `BaseMapper`.
4. `core/<modulo>/repositorys/<Nome>Repository` estendendo `RepositorioDoUsuario<Entidade>`.
5. `core/<modulo>/services/<Nome>Service` (interface) e `services/impl/<Nome>ServicePadrao`.
6. `core/<modulo>/controllers/<Nome>Controller` com `@Tag`, `@Valid` e status corretos (201 no POST, 204 no DELETE).
7. Testes: `ApiTestBase` (login demo e usuário novo), cobrindo sucesso, validação, sem login e outro usuário (404).

## MapStruct no IntelliJ

O código dos mappers é gerado na compilação (`target/generated-sources/annotations`). Depois de abrir o projeto:
*Maven › Reload All Maven Projects* e, se o IntelliJ reclamar de `TreinoMapperImpl`, ative
*Settings › Build › Compiler › Annotation Processors › Enable annotation processing*.
