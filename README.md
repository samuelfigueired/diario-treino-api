# diario-treino-api

API REST do **Diário de Treino**, aplicativo móvel para registro e acompanhamento de treinos de musculação.

- Java 17 · Spring Boot 3.5 · Spring Security · Spring Data JPA
- PostgreSQL em produção, H2 em memória no desenvolvimento e nos testes

## Rodando

```cmd
mvn spring-boot:run
```

- API: http://localhost:8080/api/saude
- Documentação: http://localhost:8080/swagger-ui.html
- Console do H2: http://localhost:8080/h2-console (JDBC URL `jdbc:h2:mem:diariotreino`, usuário `sa`)

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
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

Arquitetura e convenções: [ARQUITETURA.md](ARQUITETURA.md) e [AGENTS.md](AGENTS.md).
