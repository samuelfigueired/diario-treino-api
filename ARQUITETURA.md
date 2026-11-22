# Arquitetura do backend

API REST do Diário de Treino em Spring Boot 3.5 e Java 17. Este documento vale sobre o `AGENTS.md` em caso de conflito.

## Pacotes

```
br.com.diariotreino
├── DiarioTreinoApplication
├── comum/                 código compartilhado entre módulos
│   ├── Fusos              fuso America/Sao_Paulo e "hoje"
│   ├── entidade/          EntidadeBase, EntidadeDoUsuario
│   ├── excecao/           BadRequestException, NotFoundException, GlobalExceptionHandler
│   └── repositorio/       RepositorioDoUsuario
├── config/                SecurityConfig e demais configurações
├── saude/                 GET /api/saude
├── usuario/               RF01  cadastro e perfil
├── autenticacao/          RF01  login e token JWT
├── exercicio/             RF03  catálogo e exercícios personalizados
├── treino/                RF02, RF03  treinos e exercícios do treino
├── execucao/              RF04  sessão em andamento e séries registradas
├── historico/             RF05  sessões realizadas
└── evolucao/              RF06  carga máxima, volume e indicadores
```

## Um módulo por pasta

Cada módulo tem, no mesmo pacote:

| Arquivo | Papel |
|---|---|
| `<Modulo>Controller` | Recebe a requisição, valida com `@Valid` e delega. Sem regra de negócio. |
| `<Modulo>Service` | Interface com as operações do módulo. O controller depende dela. |
| `<Modulo>ServicePadrao` | Implementação com as regras de negócio e as transações. |
| `<Entidade>` | Entidade JPA. Se pertence a um usuário, estende `EntidadeDoUsuario`. |
| `<Entidade>Repository` | Estende `RepositorioDoUsuario<Entidade>` (ou `JpaRepository` para dados globais, como o catálogo de exercícios). |
| `dto/` | `record`s de entrada (`<Algo>Request`) e saída (`<Algo>Response`) com Bean Validation. |
| Regras puras | Classes `final` com métodos estáticos (ex.: `CalculoVolume`), testadas sem Spring. |

## Modelo de dados previsto

```
Usuario 1──* Treino 1──* TreinoExercicio *──1 Exercicio
   │                           (ordem, séries e repetições planejadas)
   └──* SessaoTreino 1──* SerieRegistrada *──1 Exercicio
          (início, fim, treino de origem)   (número, carga em kg, repetições, concluída)
```

- `Exercicio` sem `usuarioId` é do catálogo padrão; com `usuarioId`, é personalizado.
- O histórico (RF05) e a evolução (RF06) são consultas sobre `SessaoTreino` e `SerieRegistrada`; não têm tabelas próprias.
- Apagar um treino não apaga as sessões já feitas: o histórico guarda o nome do treino no momento da execução.

## Rotas

- Prefixo `/api`, recursos no plural e em português: `/api/treinos`, `/api/treinos/{id}/exercicios`, `/api/sessoes`, `/api/sessoes/{id}/series`, `/api/evolucao/exercicios/{id}`.
- O `usuarioId` sai do token. Nenhuma rota recebe `usuarioId` no corpo, na query ou no caminho.
- Respostas de erro em `ProblemDetail`; validação devolve o mapa `campos` com a mensagem de cada campo.

## Convenções

- Datas: `Instant` para início e fim de sessão; `LocalDate` para o dia do treino; cálculos de "hoje" e semana com `Fusos.SAO_PAULO`.
- Cargas em `BigDecimal` com escala 2.
- Chamadas lentas ou externas ficam fora de `@Transactional`.
- Testes conforme o `AGENTS.md`: `@DisplayName` em português e blocos `// given`, `// when`, `// then`.
