import 'dotenv/config';
import { streamText } from 'ai';

async function main() {
  try {
    const result = await streamText({
      model: 'openai/gpt-5.4',
      prompt: 'Write a short poem about coding.',
    });

    console.log('--- AI Response ---');
    for await (const textPart of result.textStream) {
      process.stdout.write(textPart);
    }
    console.log('\n\n--- Token Usage ---');
    const usage = await result.usage;
    console.log(`Prompt Tokens: ${usage.promptTokens}`);
    console.log(`Completion Tokens: ${usage.completionTokens}`);
    console.log(`Total Tokens: ${usage.totalTokens}`);
  } catch (error) {
    console.error('\nError:', error);
  }
}

main();
