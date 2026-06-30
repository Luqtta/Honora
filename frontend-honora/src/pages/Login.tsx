import { useState } from 'react'
import { useForm } from 'react-hook-form'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../hooks/useAuth'

type Form = { email: string; senha: string }

export default function Login() {
  const { register, handleSubmit, formState: { errors, isSubmitting } } = useForm<Form>()
  const { login } = useAuth()
  const navigate = useNavigate()
  const [erro, setErro] = useState('')

  const onSubmit = async (data: Form) => {
    setErro('')
    try {
      await login(data.email, data.senha)
      navigate('/dashboard')
    } catch {
      setErro('Email ou senha inválidos')
    }
  }

  return (
    <div className="flex min-h-screen items-center justify-center px-4">
      <div className="card animate-page w-full max-w-sm p-8">
        <h1 className="text-2xl font-bold text-ink">Honora</h1>
        <p className="mb-6 mt-1 text-sm text-muted">Entre na sua conta</p>

        <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
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
              {...register('senha', { required: 'Senha é obrigatória' })}
            />
            {errors.senha && <p className="mt-1 text-xs text-red-600">{errors.senha.message}</p>}
          </div>

          {erro && <p className="text-sm text-red-600">{erro}</p>}

          <button type="submit" className="btn-primary w-full" disabled={isSubmitting}>
            Entrar
          </button>
        </form>

        <p className="mt-6 text-center text-sm text-muted">
          Não tem conta?{' '}
          <Link to="/registrar" className="font-medium text-accent hover:underline">
            Registrar
          </Link>
        </p>
      </div>
    </div>
  )
}
