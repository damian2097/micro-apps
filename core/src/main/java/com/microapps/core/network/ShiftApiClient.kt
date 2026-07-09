package com.microapps.core.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit

object ShiftApiClient {

    // TODO: Replace with your deployed Vercel backend URL after running `vercel deploy` in backend/
    private const val BASE_URL = "https://micro-apps-backend.vercel.app"

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .build()

    suspend fun getProtocol(
        state: String,
        description: String = "",
        useMock: Boolean = false
    ): Result<ShiftProtocol> {
        if (useMock) return Result.success(getMockProtocol(state))

        return withContext(Dispatchers.IO) {
            try {
                val body = json.encodeToString(ShiftRequest(state = state, description = description))
                    .toRequestBody("application/json".toMediaType())

                val request = Request.Builder()
                    .url("$BASE_URL/api/shift")
                    .post(body)
                    .header("Content-Type", "application/json")
                    .build()

                client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) {
                        return@withContext Result.failure(
                            Exception("API error ${response.code}: ${response.message}")
                        )
                    }
                    val responseBody = response.body?.string()
                        ?: return@withContext Result.failure(Exception("Empty response from server"))
                    Result.success(json.decodeFromString<ShiftProtocol>(responseBody))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    /**
     * Fallback protocol used when the backend is not yet deployed (debug builds).
     * Returns a solid, general-purpose 3-step reset protocol.
     */
    private fun getMockProtocol(state: String) = ShiftProtocol(
        title = "Rapid Reset",
        steps = listOf(
            ProtocolStep(
                type = "breathing",
                title = "Physiological Sigh",
                duration_seconds = 60,
                instruction = "Double inhale through your nose — breathe in fully, then sniff once more at the top to fully inflate your lungs. Now exhale slowly and completely through your mouth. Repeat 3 times. This is the fastest known method to lower your heart rate in real time."
            ),
            ProtocolStep(
                type = "reframe",
                title = "Name the Story",
                duration_seconds = 90,
                instruction = "Say out loud or write down the exact thought looping in your head right now. Don't edit it. Now ask: is this a fact, or an interpretation? What would you tell a close friend who came to you with this exact thought? Give yourself that answer."
            ),
            ProtocolStep(
                type = "action",
                title = "One Physical Step",
                duration_seconds = 120,
                instruction = "Identify the single smallest physical action you can take in the next 5 minutes — not a plan, not a decision, one action. Stand up, open the document, send the message, drink a glass of water. Choose it. Do it now."
            )
        )
    )
}
