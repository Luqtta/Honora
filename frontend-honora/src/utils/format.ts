export const brl = (v: number | null | undefined) =>
  (v ?? 0).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })

export const formatDate = (s: string | null | undefined) =>
  s ? new Date(s + 'T00:00:00').toLocaleDateString('pt-BR') : '—'

export const formatPct = (v: number | null | undefined) =>
  v == null ? '—' : `${v.toLocaleString('pt-BR')}%`
