# diario-treino-api

API REST do **Diário de Treino**, aplicativo móvel para registro e acompanhamento de treinos de musculação.

- Java 17 · Spring Boot 3.5 · Spring Security + JWT · Spring Data JPA · MapStruct
- PostgreSQL em produção, H2 em memória no desenvolvimento e nos testes

## Rodando

```cmd
mvn spring-boot:run
```

- API: http://localhost:8080/api/saude
- Documentação: http://localhost:8080/swagger-ui.html
- Banco de desenvolvimento: H2 em arquivo na pasta `dados/` (os dados ficam salvos entre reinícios; apague a pasta para zerar)
- Console do H2: http://localhost:8080/h2-console (JDBC URL `jdbc:h2:file:./dados/diariotreino;AUTO_SERVER=TRUE`, usuário `sa`)
- Dados fictícios para demonstração: `set APP_DEMO=true` antes de subir (usuária `mariana.rocha@exemplo.com.br` / `treino2026`)
- Preencher a sua conta (sem treinos ainda) com os mesmos dados: `set APP_DEMO_EMAIL=seu@email.com` antes de subir

No Swagger, faça `POST /api/auth/login`, copie o `token` e cole em **Authorize** para testar as outras rotas.

## Testes

```cmd
mvn test
```

## Perfil de produção

```cmd
docker compose up -d
set APP_DB_URL=jdbc:postgresql://localhost:5432/diariotreino
set APP_DB_USUARIO=diariotreino
set APP_DB_SENHA=trocar-esta-senha
set APP_JWT_SECRET=um-segredo-longo-com-pelo-menos-32-caracteres
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

Arquitetura e convenções: [ARQUITETURA.md](ARQUITETURA.md) e [AGENTS.md](AGENTS.md).
