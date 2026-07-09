import { NextResponse } from 'next/server';

interface ProtocolStep {
  type: 'breathing' | 'reframe' | 'action';
  title: string;
  duration_seconds: number;
  instruction: string;
}

interface ShiftProtocol {
  title: string;
  steps: ProtocolStep[];
}

// ── Static Cicerone Blueprint Mapping ─────────────────────────────────────────
const PROTOCOLS: Record<string, ShiftProtocol> = {
  overwhelmed: {
    title: "Overwhelmed Protocol",
    steps: [
      {
        type: "reframe",
        title: "Brain Dump",
        duration_seconds: 90,
        instruction: "Write down everything currently cluttering your mind (tasks, worries, ideas). Separate them into things you can control and things you cannot control. Draw a line through everything you cannot control."
      },
      {
        type: "action",
        title: "Smallest Possible Step",
        duration_seconds: 60,
        instruction: "Identify the task you are procrastinating on. Break it down into the absolute smallest possible action (takes less than 2 minutes, like opening the doc or typing one line). Do that single action right now to break the paralysis."
      },
      {
        type: "action",
        title: "Focus Sprint",
        duration_seconds: 120,
        instruction: "Define a single concrete task to work on. Set a timer for 20 minutes. Close all tabs, put your phone on silent, and work until the timer rings."
      }
    ]
  },
  anxious: {
    title: "Anxiety Protocol",
    steps: [
      {
        type: "breathing",
        title: "Physiological Sigh",
        duration_seconds: 60,
        instruction: "Inhale deeply through your nose. At the peak of that inhale, take a sharp, short second inhale to fully inflate your lungs. Release a long, slow, sighing exhale through your mouth. Repeat this cycle 3 times."
      },
      {
        type: "action",
        title: "Smallest Possible Step",
        duration_seconds: 60,
        instruction: "Identify the task you are avoiding. Break it down into the absolute smallest possible action (takes less than 2 minutes). Do that single action right now to break the paralysis."
      },
      {
        type: "action",
        title: "Focus Sprint",
        duration_seconds: 120,
        instruction: "Define a single task. Set a timer for 20 minutes. Close all tabs, put your phone on silent, and execute without distraction to reduce autonomic anxiety."
      }
    ]
  },
  failure: {
    title: "Feeling Like A Failure Protocol",
    steps: [
      {
        type: "reframe",
        title: "Proof Review",
        duration_seconds: 90,
        instruction: "Open your 'Source of Proof' tab. Read 3 historical entries where you faced adversity but did not quit. Reflect on the fact that you have overcome hard things before."
      },
      {
        type: "reframe",
        title: "Identity Shift",
        duration_seconds: 90,
        instruction: "Write down your current feeling of identity. Ask: 'Who am I becoming?' Write down your desired identity. Ask: 'What would that version of me do next?'"
      },
      {
        type: "action",
        title: "Focus Sprint",
        duration_seconds: 120,
        instruction: "Define one task from your desired identity. Set a timer for 20 minutes and complete that single task without distraction."
      }
    ]
  },
  stuck: {
    title: "Stuck Protocol",
    steps: [
      {
        type: "action",
        title: "Smallest Possible Step",
        duration_seconds: 60,
        instruction: "Identify the task you are procrastinating on. Break it down into the absolute smallest possible action (takes less than 2 minutes, like opening the doc or typing one line). Do that single action right now to break the paralysis."
      }
    ]
  },
  angry: {
    title: "Emotion To Action Protocol",
    steps: [
      {
        type: "reframe",
        title: "Emotional Audit",
        duration_seconds: 90,
        instruction: "Identify current emotion and rate intensity from 1-10. Ask: 'What is this emotion trying to tell me?' and 'What action still makes sense regardless?'"
      },
      {
        type: "action",
        title: "Smallest Possible Step",
        duration_seconds: 60,
        instruction: "Identify the single smallest constructive action you can take right now regardless of emotional discomfort. Execute it immediately."
      }
    ]
  },
  empty: {
    title: "Lost Sight Of My Why Protocol",
    steps: [
      {
        type: "reframe",
        title: "Mission Review",
        duration_seconds: 90,
        instruction: "Read your active Mission and Strategy statement. Evaluate if your current planned actions support this direction. Adjust your focus if you've drifted."
      },
      {
        type: "reframe",
        title: "Future Self Visualisation",
        duration_seconds: 90,
        instruction: "Imagine your life 5 years from now, having successfully achieved your goals. Look back at your current self. Write down the single piece of advice your future self would give you right now."
      }
    ]
  },
  burned_out: {
    title: "Energy Boost Protocol",
    steps: [
      {
        type: "action",
        title: "Delaying Caffeine",
        duration_seconds: 60,
        instruction: "Avoid caffeine intake for the first 90-120 minutes after waking. Drink water and seek bright light instead. Let your body clear out adenosine naturally."
      },
      {
        type: "action",
        title: "Morning Sunlight",
        duration_seconds: 60,
        instruction: "Step outside within 30-60 minutes of waking. Look towards the east (do not look directly at the sun) without sunglasses for 5-10 minutes."
      },
      {
        type: "action",
        title: "Deliberate Cold Exposure",
        duration_seconds: 120,
        instruction: "Take a cold shower (10-15°C). Focus on deep, steady exhales to control the initial gasp response. Stay in the cold for 2 to 4 minutes to boost dopamine."
      }
    ]
  },
  sad: {
    title: "Perspective Reset Protocol",
    steps: [
      {
        type: "reframe",
        title: "Progress Review",
        duration_seconds: 90,
        instruction: "Review your last 10 Proof I Didn't Quit entries. Identify wins, growth, and lessons. Record observations to restore perspective."
      },
      {
        type: "reframe",
        title: "Mission Review",
        duration_seconds: 90,
        instruction: "Read your active Mission and Strategy statement to realign daily actions with your overall compass and restore motivation."
      }
    ]
  },
  lost: {
    title: "Agency Reset Protocol",
    steps: [
      {
        type: "reframe",
        title: "Sphere Of Control",
        duration_seconds: 90,
        instruction: "List your current problem. Separate it into what you cannot control and what you can influence. Draw a line through everything you cannot control."
      },
      {
        type: "action",
        title: "Smallest Possible Step",
        duration_seconds: 60,
        instruction: "Select one controllable action from your list. Execute it immediately to restore agency and momentum."
      }
    ]
  }
};

// ── Route handler ─────────────────────────────────────────────────────────────
export async function POST(req: Request) {
  try {
    const body = await req.json();
    const { state } = body as { state?: string };

    if (!state) {
      return NextResponse.json({ error: 'state is required' }, { status: 400 });
    }

    const protocol = PROTOCOLS[state.toLowerCase()];
    if (!protocol) {
      return NextResponse.json({ error: 'state not found' }, { status: 404 });
    }

    return NextResponse.json(protocol);
  } catch (error) {
    console.error('[/api/shift] Error:', error);
    return NextResponse.json(
      { error: 'Failed to retrieve protocol.' },
      { status: 500 }
    );
  }
}
