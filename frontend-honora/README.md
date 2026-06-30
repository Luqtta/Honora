# Honora — frontend

React + TypeScript + Vite + Tailwind. Interface da advogada pra clientes e honorários.

## Rodar

```bash
npm install
npm run dev          # http://localhost:5173
```

Aponta pro backend via `VITE_API_URL` (padrão `http://localhost:8080`). Pra mudar,
crie um `.env` a partir do `.env.example`.

Build de produção: `npm run build` (saída em `dist/`).

## Segurança — token no localStorage

O token JWT é guardado no `localStorage`. O ideal em produção seria um cookie
`httpOnly` + `Secure` + `SameSite` (imune a roubo por XSS), mas isso exige o
backend setar/ler cookie e tratar CSRF. Para **uso pessoal de uma única usuária**,
guardar no `localStorage` é um risco aceitável: não há outros usuários pra atacar e
a superfície de XSS é mínima (sem conteúdo de terceiros renderizado). Se o app um dia
abrir pra mais usuários, migrar para cookie `httpOnly`.
