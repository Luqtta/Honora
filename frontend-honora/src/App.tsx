import { Navigate, Route, Routes } from 'react-router-dom'
import Login from './pages/Login'
import Registrar from './pages/Registrar'
import Dashboard from './pages/Dashboard'
import Clientes from './pages/Clientes'
import PrivateRoute from './components/PrivateRoute'
import Layout from './components/Layout'

export default function App() {
  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route path="/registrar" element={<Registrar />} />
      <Route element={<PrivateRoute />}>
        <Route element={<Layout />}>
          <Route path="/dashboard" element={<Dashboard />} />
          <Route path="/clientes" element={<Clientes />} />
        </Route>
      </Route>
      <Route path="*" element={<Navigate to="/dashboard" replace />} />
    </Routes>
  )
}
