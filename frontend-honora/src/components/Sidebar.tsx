import { NavLink } from 'react-router-dom'
import { useAuth } from '../hooks/useAuth'

const iconProps = {
  width: 20,
  height: 20,
  viewBox: '0 0 24 24',
  fill: 'none',
  stroke: 'currentColor',
  strokeWidth: 2,
  strokeLinecap: 'round' as const,
  strokeLinejoin: 'round' as const,
}

function DashboardIcon() {
  return (
    <svg {...iconProps}>
      <rect x="3" y="3" width="7" height="9" />
      <rect x="14" y="3" width="7" height="5" />
      <rect x="14" y="12" width="7" height="9" />
      <rect x="3" y="16" width="7" height="5" />
    </svg>
  )
}

function ClientesIcon() {
  return (
    <svg {...iconProps}>
      <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2" />
      <circle cx="9" cy="7" r="4" />
      <path d="M23 21v-2a4 4 0 0 0-3-3.87" />
      <path d="M16 3.13a4 4 0 0 1 0 7.75" />
    </svg>
  )
}

function LogoutIcon() {
  return (
    <svg {...iconProps}>
      <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4" />
      <polyline points="16 17 21 12 16 7" />
      <line x1="21" y1="12" x2="9" y2="12" />
    </svg>
  )
}

const nav = [
  { to: '/dashboard', label: 'Dashboard', icon: <DashboardIcon /> },
  { to: '/clientes', label: 'Clientes', icon: <ClientesIcon /> },
]

export default function Sidebar({ aberta, onFechar }: { aberta: boolean; onFechar: () => void }) {
  const { logout } = useAuth()

  return (
    <>
      {aberta && <div className="fixed inset-0 z-30 bg-black/40 md:hidden" onClick={onFechar} />}
      <aside
        className={`fixed left-0 top-0 z-40 flex h-screen w-60 flex-col bg-sidebar px-4 py-6 text-gray-300 transition-transform md:translate-x-0 ${
          aberta ? 'translate-x-0' : '-translate-x-full'
        }`}
      >
        <div className="px-2 pb-8 text-xl font-bold text-white">Honora</div>

        <nav className="flex flex-1 flex-col gap-1">
          {nav.map((item) => (
            <NavLink
              key={item.to}
              to={item.to}
              onClick={onFechar}
              className={({ isActive }) =>
                `flex items-center gap-3 rounded-lg px-3 py-2.5 text-sm font-medium transition ${
                  isActive ? 'bg-accent text-white' : 'text-gray-400 hover:bg-white/5 hover:text-white'
                }`
              }
            >
              {item.icon}
              {item.label}
            </NavLink>
          ))}
        </nav>

        <button
          onClick={logout}
          className="flex items-center gap-3 rounded-lg px-3 py-2.5 text-sm font-medium text-gray-400 transition hover:bg-white/5 hover:text-white"
        >
          <LogoutIcon />
          Sair
        </button>
      </aside>
    </>
  )
}
