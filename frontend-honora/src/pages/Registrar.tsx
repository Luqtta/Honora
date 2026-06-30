import { useState } from 'react'
import { useForm } from 'react-hook-form'
import { Link, useNavigate } from 'react-router-dom'
import { AxiosError } from 'axios'
import { useAuth } from '../hooks/useAuth'

type Form = { nome: string; email: string; senha: string }

export default function Registrar() {
  const { register, handleSubmit, formState: { errors, isSubmitting } } = useForm<Form>()
  const { registrar } = useAuth()
  const navigate = useNavigate()
  const [erro, setErro] = useState('')

  const onSubmit = async (data: Form) => {
    setErro('')
    try {
      await registrar(data.nome, data.email, data.senha)
      navigate('/dashboard')
    } catch (e) {
      const msg = (e as AxiosError<{ erro?: string }>).response?.data?.erro
      setErro(msg || 'Não foi possível registrar')
    }
  }

  return (
    <div className="flex min-h-screen items-center justify-center px-4">
      <div className="card animate-page w-full max-w-sm p-8">
        <h1 className="text-2xl font-bold text-ink">Criar conta</h1>
        <p className="mb-6 mt-1 text-sm text-muted">Comece a usar o Honora</p>

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
            <label className="label">Email</label>
            <input
              type="email"
              className="input"
              {...register('email', { required: 'Email é obrigatório' })}
            />
            {errors.email && <p className="mt-1 text-xs text-red-600">{errors.email.message}</p>}
          </div>

          <div>
            <label className="label">Senha</label>
            <input
              type="password"
              className="input"
              {...register('senha', {
                required: 'Senha é obrigatória',
                minLength: { value: 6, message: 'Mínimo de 6 caracteres' },
              })}
            />
            {errors.senha && <p className="mt-1 text-xs text-red-600">{errors.senha.message}</p>}
          </div>

          {erro && <p className="text-sm text-red-600">{erro}</p>}

          <button type="submit" className="btn-primary w-full" disabled={isSubmitting}>
            Registrar
          </button>
        </form>

        <p className="mt-6 text-center text-sm text-muted">
          Já tem conta?{' '}
          <Link to="/login" className="font-medium text-accent hover:underline">
            Entrar
          </Link>
        </p>
      </div>
    </div>
  )
}
