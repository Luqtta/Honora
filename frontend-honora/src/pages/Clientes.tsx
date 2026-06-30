import { useEffect, useState } from 'react'
import api from '../services/api'
import { Cliente } from '../types'
import StatusBadge from '../components/StatusBadge'
import ClienteModal from '../components/ClienteModal'
import ConfirmModal from '../components/ConfirmModal'
import { brl, formatPct } from '../utils/format'

type Confirmacao = {
  titulo: string
  mensagem: string
  textoConfirmar: string
  variante: 'primary' | 'danger'
  acao: () => Promise<void>
}

export default function Clientes() {
  const [clientes, setClientes] = useState<Cliente[]>([])
  const [modalAberto, setModalAberto] = useState(false)
  const [editando, setEditando] = useState<Cliente | null>(null)
  const [confirmacao, setConfirmacao] = useState<Confirmacao | null>(null)

  const carregar = () => api.get<Cliente[]>('/api/clientes').then((r) => setClientes(r.data))

  useEffect(() => {
    carregar()
  }, [])

  const novo = () => {
    setEditando(null)
    setModalAberto(true)
  }

  const editar = (c: Cliente) => {
    setEditando(c)
    setModalAberto(true)
  }

  const pedirExcluir = (c: Cliente) =>
    setConfirmacao({
      titulo: 'Excluir cliente',
      mensagem: `Excluir ${c.nome}? Esta ação não pode ser desfeita.`,
      textoConfirmar: 'Excluir',
      variante: 'danger',
      acao: async () => {
        await api.delete(`/api/clientes/${c.id}`)
        carregar()
      },
    })

  const pedirMarcarPago = (c: Cliente) =>
    setConfirmacao({
      titulo: 'Marcar como pago',
      mensagem: `Marcar ${c.nome} como pago? O valor recebido será igualado ao valor a receber.`,
      textoConfirmar: 'Marcar como pago',
      variante: 'primary',
      acao: async () => {
        await api.put(`/api/clientes/${c.id}`, { valorRecebido: c.valorAReceber })
        carregar()
      },
    })

  const confirmar = async () => {
    if (!confirmacao) return
    await confirmacao.acao()
    setConfirmacao(null)
  }

  const aoSalvar = () => {
    setModalAberto(false)
    carregar()
  }

  return (
    <div>
      <div className="mb-6 flex items-center justify-between">
        <h1 className="text-2xl font-bold text-ink">Clientes</h1>
        <button className="btn-primary" onClick={novo}>
          Novo Cliente
        </button>
      </div>

      <div className="card overflow-hidden">
        <table className="w-full text-sm">
          <thead>
            <tr className="border-b border-line text-left text-xs uppercase tracking-wide text-muted">
              <th className="px-5 py-3 font-medium">Nome</th>
              <th className="px-5 py-3 font-medium">Valor a Receber</th>
              <th className="px-5 py-3 font-medium">Valor Recebido</th>
              <th className="px-5 py-3 font-medium">% Honorários</th>
              <th className="px-5 py-3 font-medium">Status</th>
              <th className="px-5 py-3 text-right font-medium">Ações</th>
            </tr>
          </thead>
          <tbody>
            {clientes.length === 0 && (
              <tr>
                <td colSpan={6} className="px-5 py-10 text-center text-muted">
                  Nenhum cliente cadastrado.
                </td>
              </tr>
            )}
            {clientes.map((c) => (
              <tr key={c.id} className="border-b border-line last:border-0 hover:bg-canvas">
                <td className="px-5 py-3 font-medium text-ink">{c.nome}</td>
                <td className="px-5 py-3 text-ink">{brl(c.valorAReceber)}</td>
                <td className="px-5 py-3 text-ink">{brl(c.valorRecebido)}</td>
                <td className="px-5 py-3 text-ink">{formatPct(c.percentualHonorarios)}</td>
                <td className="px-5 py-3">
                  <StatusBadge status={c.status} />
                </td>
                <td className="px-5 py-3">
                  <div className="flex justify-end gap-4">
                    {c.status !== 'RECEBIDO' && (
                      <button
                        className="font-medium text-green-600 hover:underline"
                        onClick={() => pedirMarcarPago(c)}
                      >
                        Marcar como pago
                      </button>
                    )}
                    <button
                      className="font-medium text-accent hover:underline"
                      onClick={() => editar(c)}
                    >
                      Editar
                    </button>
                    <button
                      className="font-medium text-red-600 hover:underline"
                      onClick={() => pedirExcluir(c)}
                    >
                      Excluir
                    </button>
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {modalAberto && (
        <ClienteModal cliente={editando} onClose={() => setModalAberto(false)} onSaved={aoSalvar} />
      )}

      {confirmacao && (
        <ConfirmModal
          titulo={confirmacao.titulo}
          mensagem={confirmacao.mensagem}
          textoConfirmar={confirmacao.textoConfirmar}
          variante={confirmacao.variante}
          onConfirmar={confirmar}
          onCancelar={() => setConfirmacao(null)}
        />
      )}
    </div>
  )
}
