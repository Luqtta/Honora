# Honora

Gestão de clientes e honorários para advocacia previdenciária. Monorepo:

- `honorarios-api/` — backend Spring Boot 3.5 / Java 21 / PostgreSQL / JWT
- `frontend-honora/` — frontend React + TypeScript + Vite + Tailwind

## Rodar local

```bash
# 1. Postgres
cd honorarios-api && docker compose up -d

# 2. Backend  (http://localhost:8080)
#    JWT_SECRET é obrigatório (mínimo 32 caracteres) — a app falha no startup sem ele.
set JWT_SECRET=dev-secret-local-com-no-minimo-32-caracteres-0123456789   & mvnw.cmd spring-boot:run   # Windows
JWT_SECRET=dev-secret-local-com-no-minimo-32-caracteres-0123456789 ./mvnw spring-boot:run             # Linux/Mac

# 3. Frontend (http://localhost:5173)
cd ../frontend-honora && npm install && npm run dev
```

O `DATABASE_URL` já tem default apontando pro Postgres do Docker. Só o `JWT_SECRET`
precisa ser setado (não há segredo commitado no repo, de propósito).

---

## Deploy

Frontend na **Vercel**, backend + Postgres na **Railway**. Um repositório só.

### 1. Subir pro GitHub

Crie um repositório **vazio** no GitHub (sem README/license), depois:

```bash
git remote add origin https://github.com/SEU_USUARIO/SEU_REPO.git
git branch -M main
git push -u origin main
```

### 2. Railway (backend)

1. **New Project → Deploy from GitHub repo** → escolha este repo.
2. No serviço criado: **Settings → Root Directory = `honorarios-api`** (ele usa o `Dockerfile`).
3. **New → Database → PostgreSQL** no mesmo projeto.
4. No serviço do backend, **Variables**, adicione:
   - `DATABASE_URL` = `jdbc:postgresql://${{Postgres.PGHOST}}:${{Postgres.PGPORT}}/${{Postgres.PGDATABASE}}`
   - `DATABASE_USERNAME` = `${{Postgres.PGUSER}}`
   - `DATABASE_PASSWORD` = `${{Postgres.PGPASSWORD}}`
   - `JWT_SECRET` = um segredo aleatório (≥ 32 caracteres)
   - `JWT_EXPIRATION` = `86400000`
   - `CORS_ORIGIN` = a URL da Vercel (passo 3) — ex: `https://honora.vercel.app`
5. Em **Settings → Networking**, gere o domínio público. Essa é a URL da API.

`PORT` a Railway injeta sozinha; a app já lê dela.

### 3. Vercel (frontend)

1. **Add New → Project** → importe o mesmo repo.
2. **Root Directory = `frontend-honora`** (framework Vite é detectado sozinho).
3. **Environment Variables**: `VITE_API_URL` = a URL pública da Railway (passo 2.5).
4. Deploy. Pegue a URL final (`https://...vercel.app`) e coloque em `CORS_ORIGIN` na Railway.

> Ordem prática: deploy na Vercel → pega a URL → seta `CORS_ORIGIN` na Railway → deploy do backend → seta `VITE_API_URL` na Vercel se ainda não tiver. Frontend e backend só conversam com o `CORS_ORIGIN` certo.
