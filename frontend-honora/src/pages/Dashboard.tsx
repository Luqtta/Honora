import { useEffect, useState } from 'react'
import api from '../services/api'
import { Estimativa, Resumo } from '../types'
import { brl } from '../utils/format'

const MESES = [
  'Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho',
  'Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro', 'Dezembro',
]

const anoAtual = new Date().getFullYear()
const ANOS = Array.from({ length: 6 }, (_, i) => anoAtual - 1 + i)

function Card({ titulo, valor }: { titulo: string; valor: number }) {
  return (
    <div className="card p-5">
      <p className="text-sm text-muted">{titulo}</p>
      <p className="mt-2 text-2xl font-semibold text-ink">{brl(valor)}</p>
    </div>
  )
}

export default function Dashboard() {
  const [resumo, setResumo] = useState<Resumo | null>(null)
  const [ano, setAno] = useState(anoAtual)
  const [mes, setMes] = useState('')
  const [estimativa, setEstimativa] = useState<Estimativa | null>(null)

  useEffect(() => {
    api.get<Resumo>('/api/dashboard/resumo').then((r) => setResumo(r.data))
  }, [])

  useEffect(() => {
    const params: Record<string, string | number> = { ano }
    if (mes) params.mes = mes
    api.get<Estimativa>('/api/dashboard/estimativa', { params }).then((r) => setEstimativa(r.data))
  }, [ano, mes])

  return (
    <div>
      <h1 className="mb-6 text-2xl font-bold text-ink">Dashboard</h1>

      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 xl:grid-cols-4">
        <Card titulo="Total a Receber" valor={resumo?.totalAReceber ?? 0} />
        <Card titulo="Total Recebido" valor={resumo?.totalRecebido ?? 0} />
        <Card titulo="Honorários a Receber" valor={resumo?.honorariosAReceber ?? 0} />
        <Card titulo="Honorários Recebidos" valor={resumo?.honorariosRecebidos ?? 0} />
      </div>

      <div className="card mt-6 p-6">
        <h2 className="text-lg font-semibold text-ink">Estimativa de honorários</h2>
        <p className="mt-1 text-sm text-muted">Honorários previstos pela data de previsão no período.</p>

        <div className="mt-4 flex flex-wrap items-end gap-4">
          <div>
            <label className="label">Ano</label>
            <select className="input w-36" value={ano} onChange={(e) => setAno(Number(e.target.value))}>
              {ANOS.map((a) => (
                <option key={a} value={a}>{a}</option>
              ))}
            </select>
          </div>
          <div>
            <label className="label">Mês</label>
            <select className="input w-44" value={mes} onChange={(e) => setMes(e.target.value)}>
              <option value="">Todos</option>
              {MESES.map((nome, i) => (
                <option key={i} value={i + 1}>{nome}</option>
              ))}
            </select>
          </div>
        </div>

        <p className="mt-6 text-3xl font-semibold text-accent">
          {brl(estimativa?.honorariosEstimados ?? 0)}
        </p>
      </div>
    </div>
  )
}
