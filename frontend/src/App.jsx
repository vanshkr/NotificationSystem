import { useState } from 'react'
import {StatsCards}    from './components/StatsCards'
import {Chart}         from './components/Chart'
import {TriggerForm}   from './components/TriggerForm.jsx'
import {DeliveryTable} from './components/DeliveryTable'
import './App.css'

export default function App() {
  const [refreshKey, setRefreshKey] = useState(0)

  function handleSent() {
    setRefreshKey(k => k + 1)
  }

  return (
    <div className="app">
      <header className="app-header">
        <h1>Notification Dashboard</h1>
        <p>Kafka · Brevo · Redis · PostgreSQL</p>
      </header>
      <main className="app-main">
        <StatsCards />
        <Chart refreshKey={refreshKey} />
        <TriggerForm onSent={handleSent} />
        <DeliveryTable refreshKey={refreshKey} />
      </main>
    </div>
  )
}
