import { useNavigate } from 'react-router-dom'
import api from '../services/api'

export function useAuth() {
  const navigate = useNavigate()

  const login = async (email: string, senha: string) => {
    const { data } = await api.post('/api/auth/login', { email, senha })
    localStorage.setItem('token', data.token)
  }

  const registrar = async (nome: string, email: string, senha: string) => {
    const { data } = await api.post('/api/auth/registrar', { nome, email, senha })
    localStorage.setItem('token', data.token)
  }

  const logout = () => {
    localStorage.removeItem('token')
    navigate('/login')
  }

  return { login, registrar, logout }
}
