import { generateObject } from 'ai';
import { NextResponse } from 'next/server';
import { z } from 'zod';

// ── Schema ───────────────────────────────────────────────────────────────────
const ProtocolSchema = z.object({
  title: z.string().describe('Short, punchy protocol name (3-5 words)'),
  steps: z
    .array(
      z.object({
        type: z
          .enum(['breathing', 'reframe', 'action'])
          .describe('Step category'),
        title: z.string().describe('Short step title (2-4 words)'),
        duration_seconds: z
          .number()
          .int()
          .min(0)
          .describe('Suggested time in seconds, 0 if not time-boxed'),
        instruction: z
          .string()
          .describe('Clear, direct instruction in 2-4 sentences'),
      })
    )
    .min(3)
    .max(5),
});

// ── System prompt ─────────────────────────────────────────────────────────────
const SYSTEM_PROMPT = `You are a concise, no-nonsense state-shift coach.
When given an emotional state, generate a 3-5 step protocol that helps someone
shift out of that state in under 5 minutes.

Rules:
- Step 1 must always be physical or physiological (breathing, body scan, movement).
- Include exactly one cognitive reframe step.
- End with one concrete micro-action the person can do RIGHT NOW.
- Be specific and direct. No platitudes, no filler, no "take a moment to reflect".
- Each instruction should be 2-4 sentences max.
- Duration should be realistic: breathing = 60s, reframe = 90s, action = 60-120s.`;

// ── Route handler ─────────────────────────────────────────────────────────────
export async function POST(req: Request) {
  try {
    const body = await req.json();
    const { state, description } = body as { state?: string; description?: string };

    if (!state) {
      return NextResponse.json({ error: 'state is required' }, { status: 400 });
    }

    const { object } = await generateObject({
      // We pass the model string directly. Under the hood, Vercel AI SDK
      // automatically uses the Vercel OIDC token in production to resolve this.
      model: 'google/gemini-2.5-flash-lite',
      schema: ProtocolSchema,
      system: SYSTEM_PROMPT,
      prompt: `Emotional state: "${state}"\nContext: "${description ?? state}"\n\nGenerate a targeted protocol to shift this state.`,
    });

    return NextResponse.json(object);
  } catch (error) {
    console.error('[/api/shift] Error:', error);
    return NextResponse.json(
      { error: 'Failed to generate protocol. Please try again.' },
      { status: 500 }
    );
  }
}
