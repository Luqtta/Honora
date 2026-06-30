# Honora — honorarios-api

Backend de gestao de clientes e honorarios. Spring Boot 3.5 / Java 21 / PostgreSQL / JWT.

## Rodar local

```bash
docker compose up -d        # sobe o Postgres (porta 5432, volume persistente)
./mvnw spring-boot:run      # app em http://localhost:8080
```

O `application.yml` ja tem defaults apontando pro Postgres do Docker, entao roda sem
configurar nada. Pra sobrescrever, exporte as vars de `.env.local.example`.

## Deploy (Railway)

Configure as variaveis de `.env.example` no painel: `DATABASE_URL` (JDBC, com
`?user=...&password=...`), `JWT_SECRET` (>= 32 chars) e `JWT_EXPIRATION` (ms).

## Endpoints

Auth (publico): `POST /api/auth/registrar`, `POST /api/auth/login` -> `{token}`.
Demais endpoints exigem header `Authorization: Bearer <token>`.

- `GET/POST /api/clientes`, `GET/PUT/DELETE /api/clientes/{id}`
- `GET /api/dashboard/resumo`
- `GET /api/dashboard/estimativa?ano=2026&mes=7` (mes opcional)
