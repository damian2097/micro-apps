export async function GET() {
  return Response.json({ ok: true, service: 'micro-apps-backend', version: '0.1.0' });
}
