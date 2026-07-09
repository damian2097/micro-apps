export default function Home() {
  return (
    <main style={{ fontFamily: 'monospace', padding: '2rem' }}>
      <h1>micro-apps API</h1>
      <p>Available endpoints:</p>
      <ul>
        <li><code>POST /api/shift</code> — generate a state-shift protocol</li>
        <li><code>GET /api/health</code> — uptime check</li>
      </ul>
    </main>
  );
}
