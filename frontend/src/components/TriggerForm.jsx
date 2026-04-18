import { useState } from 'react'
import { triggerEvent } from '../api/client'

function uuid() {
  return crypto.randomUUID()
}

const EVENT_TYPES  = ['ORDER_PLACED', 'USER_SIGNUP', 'PAYMENT_FAILED', 'SYSTEM_ALERT', 'REMINDER']
const CHANNELS     = ['EMAIL', 'SMS']

export const TriggerForm = ({ onSent }) => {
  const [form, setForm]     = useState({ eventType: 'ORDER_PLACED', recipient: '', channel: 'EMAIL' })
  const [status, setStatus] = useState(null)
  const [loading, setLoading] = useState(false)

  function handleChange(e) {
    setForm(f => ({ ...f, [e.target.name]: e.target.value }))
  }

  async function handleSubmit(e) {
    e.preventDefault()
    setLoading(true)
    setStatus(null)
    try {
      await triggerEvent({ ...form, eventId: uuid() })
      setStatus({ ok: true, msg: 'Event triggered successfully!' })
      setForm(f => ({ ...f, recipient: '' }))
      onSent?.()
    } catch {
      setStatus({ ok: false, msg: 'Failed to trigger event. Is the backend running?' })
    } finally {
      setLoading(false)
    }
  }

  return (
    <form className="trigger-form" onSubmit={handleSubmit}>
      <h2>Trigger Notification</h2>
      <div className="form-row">
        <label>Event Type
          <select name="eventType" value={form.eventType} onChange={handleChange}>
            {EVENT_TYPES.map(t => <option key={t}>{t}</option>)}
          </select>
        </label>
        <label>Channel
          <select name="channel" value={form.channel} onChange={handleChange}>
            {CHANNELS.map(c => <option key={c}>{c}</option>)}
          </select>
        </label>
        <label>Recipient
          <input
            name="recipient"
            type="text"
            placeholder={form.channel === 'EMAIL' ? 'email@example.com' : '+1234567890'}
            value={form.recipient}
            onChange={handleChange}
            required
          />
        </label>
      </div>
      <button type="submit" disabled={loading}>{loading ? 'Sending…' : 'Send'}</button>
      {status && (
        <p className={status.ok ? 'msg-ok' : 'error'}>{status.msg}</p>
      )}
    </form>
  )
}
