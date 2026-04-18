import { useEffect, useState } from 'react'
import { getLogs } from '../api/client'

const STATUS_COLORS = {
  SENT:      '#22c55e',
  FAILED:    '#ef4444',
  DUPLICATE: '#94a3b8',
  EXHAUSTED: '#f97316',
}

function fmt(ts) {
  if (!ts) return '—'
  return new Date(ts).toLocaleString()
}

export const DeliveryTable = ({ refreshKey }) => {
  const [page, setPage]       = useState(0)
  const [data, setData]       = useState(null)
  const [error, setError]     = useState(null)

  useEffect(() => {
    getLogs(page, 20)
      .then(setData)
      .catch(() => setError('Failed to load logs'))
  }, [page, refreshKey])

  if (error) return <p className="error">{error}</p>
  if (!data)  return <p>Loading logs...</p>

  return (
    <div className="table-wrap">
      <table>
        <thead>
          <tr>
            <th>Time</th>
            <th>Event Type</th>
            <th>Recipient</th>
            <th>Channel</th>
            <th>Status</th>
            <th>Provider</th>
          </tr>
        </thead>
        <tbody>
          {data.content.length === 0 && (
            <tr><td colSpan={6} style={{ textAlign: 'center' }}>No records yet</td></tr>
          )}
          {data.content.map(row => (
            <tr key={row.id}>
              <td>{fmt(row.createdAt)}</td>
              <td>{row.eventType}</td>
              <td>{row.recipient}</td>
              <td>{row.channel}</td>
              <td>
                <span className="badge" style={{ background: STATUS_COLORS[row.status] ?? '#94a3b8' }}>
                  {row.status}
                </span>
              </td>
              <td>{row.provider ?? '—'}</td>
            </tr>
          ))}
        </tbody>
      </table>
      <div className="pagination">
        <button onClick={() => setPage(p => p - 1)} disabled={data.first}>Prev</button>
        <span>Page {data.number + 1} of {Math.max(data.totalPages, 1)}</span>
        <button onClick={() => setPage(p => p + 1)} disabled={data.last}>Next</button>
      </div>
    </div>
  )
}
