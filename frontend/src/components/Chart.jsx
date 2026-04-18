import { useEffect, useState } from 'react';
import { BarChart, Bar, XAxis, YAxis, Tooltip, Legend, ResponsiveContainer, Cell } from 'recharts';
import { getStats } from '../api/client';

const COLORS = { SENT: '#22c55e', FAILED: '#ef4444', DUPLICATE: '#94a3b8' }

export const Chart = ({ refreshKey }) => {
  const [data, setData] = useState([])

  useEffect(() => {
    getStats().then(s => {
      setData([
        { name: 'SENT',      count: s.totalSent },
        { name: 'FAILED',    count: s.totalFailed },
        { name: 'DUPLICATE', count: s.totalDuplicate },
      ])
    })
  }, [refreshKey])

  return (
    <div className="chart-wrap">
      <h2>Delivery Breakdown</h2>
      <ResponsiveContainer width="100%" height={220}>
        <BarChart data={data} margin={{ top: 10, right: 20, left: 0, bottom: 0 }}>
          <XAxis dataKey="name" />
          <YAxis allowDecimals={false} />
          <Tooltip />
          <Bar dataKey="count" radius={[4, 4, 0, 0]}>
            {data.map(entry => (
              <Cell key={entry.name} fill={COLORS[entry.name]} />
            ))}
          </Bar>
        </BarChart>
      </ResponsiveContainer>
    </div>
  )
}
