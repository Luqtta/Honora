import { Status } from '../types'

const styles: Record<Status, string> = {
  PENDENTE: 'bg-yellow-100 text-yellow-800',
  PARCIAL: 'bg-blue-100 text-blue-800',
  RECEBIDO: 'bg-green-100 text-green-800',
}

export default function StatusBadge({ status }: { status: Status }) {
  return (
    <span className={`inline-flex rounded-full px-2.5 py-0.5 text-xs font-medium ${styles[status]}`}>
      {status}
    </span>
  )
}
