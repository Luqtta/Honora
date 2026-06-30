type Props = {
  titulo: string
  mensagem: string
  textoConfirmar?: string
  variante?: 'primary' | 'danger'
  onConfirmar: () => void
  onCancelar: () => void
}

export default function ConfirmModal({
  titulo,
  mensagem,
  textoConfirmar = 'Confirmar',
  variante = 'primary',
  onConfirmar,
  onCancelar,
}: Props) {
  const confirmarClasse =
    variante === 'danger'
      ? 'inline-flex items-center justify-center rounded-lg bg-red-600 px-4 py-2 text-sm font-medium text-white transition hover:bg-red-700'
      : 'btn-primary'

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40 p-4">
      <div className="card w-full max-w-sm p-6">
        <h2 className="text-lg font-semibold text-ink">{titulo}</h2>
        <p className="mt-2 text-sm text-muted">{mensagem}</p>
        <div className="mt-6 flex justify-end gap-2">
          <button type="button" className="btn-ghost" onClick={onCancelar}>
            Cancelar
          </button>
          <button type="button" className={confirmarClasse} onClick={onConfirmar}>
            {textoConfirmar}
          </button>
        </div>
      </div>
    </div>
  )
}
