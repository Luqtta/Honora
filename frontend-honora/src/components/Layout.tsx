import { useState } from 'react'
import { Outlet, useLocation } from 'react-router-dom'
import Sidebar from './Sidebar'

function SunIcon() {
  return (
    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <circle cx="12" cy="12" r="5" />
      <line x1="12" y1="1" x2="12" y2="3" />
      <line x1="12" y1="21" x2="12" y2="23" />
      <line x1="4.22" y1="4.22" x2="5.64" y2="5.64" />
      <line x1="18.36" y1="18.36" x2="19.78" y2="19.78" />
      <line x1="1" y1="12" x2="3" y2="12" />
      <line x1="21" y1="12" x2="23" y2="12" />
      <line x1="4.22" y1="19.78" x2="5.64" y2="18.36" />
      <line x1="18.36" y1="5.64" x2="19.78" y2="4.22" />
    </svg>
  )
}

function MoonIcon() {
  return (
    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z" />
    </svg>
  )
}

function MenuIcon() {
  return (
    <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <line x1="3" y1="6" x2="21" y2="6" />
      <line x1="3" y1="12" x2="21" y2="12" />
      <line x1="3" y1="18" x2="21" y2="18" />
    </svg>
  )
}

export default function Layout() {
  const location = useLocation()
  const [dark, setDark] = useState(() => document.documentElement.classList.contains('dark'))
  const [menuAberto, setMenuAberto] = useState(false)

  const toggle = () => {
    const next = !dark
    setDark(next)
    document.documentElement.classList.toggle('dark', next)
    localStorage.setItem('theme', next ? 'dark' : 'light')
  }

  return (
    <div className="min-h-screen bg-canvas">
      <Sidebar aberta={menuAberto} onFechar={() => setMenuAberto(false)} />
      <main className="min-h-screen md:ml-60">
        <header className="flex items-center px-4 pt-6 sm:px-8">
          <button
            onClick={() => setMenuAberto(true)}
            aria-label="Abrir menu"
            className="rounded-lg border border-line bg-surface p-2 text-ink md:hidden"
          >
            <MenuIcon />
          </button>
          <button
            onClick={toggle}
            aria-label="Alternar tema"
            className="ml-auto rounded-lg border border-line bg-surface p-2 text-ink transition hover:bg-canvas"
          >
            {dark ? <SunIcon /> : <MoonIcon />}
          </button>
        </header>
        <div key={location.pathname} className="animate-page px-4 pb-8 pt-4 sm:px-8">
          <Outlet />
        </div>
      </main>
    </div>
  )
}
