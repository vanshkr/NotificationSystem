import {useEffect, useState} from 'react';
import {getStats} from '../api/client';

export const StatsCards = () => {
    const [stats, setStats] = useState(null);
    const [error, setError] = useState(null);

    useEffect(()=>{
        getStats()
        .then(setStats)
        .catch(()=>setError('Failed to load stats'))},[])

    if(error) return <p className="error">{error}</p>
    if(!stats) return <p> Loading stats...</p>

      const cards = [
        { label: 'Total Sent',    value: stats.totalSent,      color: '#22c55e' },
        { label: 'Total Failed',  value: stats.totalFailed,    color: '#ef4444' },
        { label: 'Duplicates',    value: stats.totalDuplicate, color: '#94a3b8' },
        { label: 'Sent Today',    value: stats.sentToday,      color: '#3b82f6' },
      ]

      return (
        <div className="stats-grid">
          {cards.map(c => (
            <div key={c.label} className="stat-card" style={{ borderTop: `4px solid ${c.color}` }}>
              <p className="stat-label">{c.label}</p>
              <p className="stat-value">{c.value}</p>
            </div>
          ))}
        </div>
      )
  }

