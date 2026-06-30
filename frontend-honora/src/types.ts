export type Status = 'PENDENTE' | 'PARCIAL' | 'RECEBIDO'

export interface Cliente {
  id: string
  nome: string
  valorAReceber: number
  valorRecebido: number | null
  percentualHonorarios: number | null
  dataPrevisao: string | null
  status: Status
  createdAt: string
  updatedAt: string
}

export interface Resumo {
  totalAReceber: number
  totalRecebido: number
  honorariosRecebidos: number
  honorariosAReceber: number
}

export interface Estimativa {
  ano: number
  mes: number | null
  honorariosEstimados: number
}
