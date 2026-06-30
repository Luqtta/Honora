import { useState } from 'react'
import { useForm } from 'react-hook-form'
import api from '../services/api'
import { Cliente } from '../types'

type Props = {
  cliente?: Cliente | null
  onClose: () => void
  onSaved: () => void
}

type Form = {
  nome: string
  valorAReceber: number
  valorRecebido: number
  percentualHonorarios: number
  dataPrevisao: string
}

export default function ClienteModal({ cliente, onClose, onSaved }: Props) {
  const [erro, setErro] = useState('')
  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<Form>({
    defaultValues: {
      nome: cliente?.nome ?? '',
      valorAReceber: cliente?.valorAReceber,
      valorRecebido: cliente?.valorRecebido ?? undefined,
      percentualHonorarios: cliente?.percentualHonorarios ?? undefined,
      dataPrevisao: cliente?.dataPrevisao ?? '',
    },
  })

  const onSubmit = async (data: Form) => {
    setErro('')
    const payload = {
      nome: data.nome,
      valorAReceber: data.valorAReceber,
      valorRecebido: isNaN(data.valorRecebido) ? null : data.valorRecebido,
      percentualHonorarios: isNaN(data.percentualHonorarios) ? null : data.percentualHonorarios,
      dataPrevisao: data.dataPrevisao || null,
    }
    try {
      if (cliente) await api.put(`/api/clientes/${cliente.id}`, payload)
      else await api.post('/api/clientes', payload)
      onSaved()
    } catch {
      setErro('Não foi possível salvar. Verifique os dados.')
    }
  }

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40 p-4">
      <div className="card w-full max-w-md p-6">
        <h2 className="mb-5 text-lg font-semibold text-ink">
          {cliente ? 'Editar Cliente' : 'Novo Cliente'}
        </h2>

        <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
          <div>
            <label className="label">Nome</label>
            <input
              className="input"
              {...register('nome', { required: 'Nome é obrigatório' })}
            />
            {errors.nome && <p className="mt-1 text-xs text-red-600">{errors.nome.message}</p>}
          </div>

          <div>
            <label className="label">Valor a receber (R$)</label>
            <input
              type="number"
              step="0.01"
              className="input"
              {...register('valorAReceber', {
                valueAsNumber: true,
                validate: (v) =>
                  (typeof v === 'number' && !isNaN(v) && v > 0) ||
                  'Informe um valor maior que zero',
              })}
            />
            {errors.valorAReceber && (
              <p className="mt-1 text-xs text-red-600">{errors.valorAReceber.message}</p>
            )}
          </div>

          <div>
            <label className="label">Valor recebido (R$)</label>
            <input
              type="number"
              step="0.01"
              className="input"
              {...register('valorRecebido', {
                valueAsNumber: true,
                validate: (v) => isNaN(v) || v >= 0 || 'Não pode ser negativo',
              })}
            />
            {errors.valorRecebido && (
              <p className="mt-1 text-xs text-red-600">{errors.valorRecebido.message}</p>
            )}
          </div>

          <div>
            <label className="label">% Honorários</label>
            <input
              type="number"
              step="0.01"
              className="input"
              {...register('percentualHonorarios', {
                valueAsNumber: true,
                validate: (v) => isNaN(v) || (v >= 0 && v <= 100) || 'Entre 0 e 100',
              })}
            />
            {errors.percentualHonorarios && (
              <p className="mt-1 text-xs text-red-600">{errors.percentualHonorarios.message}</p>
            )}
          </div>

          <div>
            <label className="label">Data de previsão</label>
            <input type="date" className="input" {...register('dataPrevisao')} />
          </div>

          {erro && <p className="text-sm text-red-600">{erro}</p>}

          <div className="flex justify-end gap-2 pt-2">
            <button type="button" className="btn-ghost" onClick={onClose}>
              Cancelar
            </button>
            <button type="submit" className="btn-primary" disabled={isSubmitting}>
              Salvar
            </button>
          </div>
        </form>
      </div>
    </div>
  )
}
