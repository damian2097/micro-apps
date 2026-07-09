package com.microapps.shift.data

import androidx.compose.ui.graphics.Color

data class EmotionalState(
    val id: String,
    val emoji: String,
    val label: String,
    val description: String,
    val tint: Color
)

val emotionalStates = listOf(
    EmotionalState(
        id = "overwhelmed",
        emoji = "🌊",
        label = "Overwhelmed",
        description = "Too much, all at once",
        tint = Color(0xFF4A90D9)
    ),
    EmotionalState(
        id = "anxious",
        emoji = "⚡",
        label = "Anxious",
        description = "Racing mind, tight chest",
        tint = Color(0xFFE8A838)
    ),
    EmotionalState(
        id = "failure",
        emoji = "💔",
        label = "Like a failure",
        description = "Nothing is working",
        tint = Color(0xFFE85555)
    ),
    EmotionalState(
        id = "stuck",
        emoji = "🧱",
        label = "Stuck",
        description = "Can't make myself start",
        tint = Color(0xFF7C3AED)
    ),
    EmotionalState(
        id = "angry",
        emoji = "🔥",
        label = "Angry",
        description = "Frustrated and reactive",
        tint = Color(0xFFE86C2E)
    ),
    EmotionalState(
        id = "empty",
        emoji = "🌑",
        label = "Empty",
        description = "Numb, nothing matters",
        tint = Color(0xFF5A6A7A)
    ),
    EmotionalState(
        id = "burned_out",
        emoji = "🕯️",
        label = "Burned out",
        description = "Running on empty",
        tint = Color(0xFFA0522D)
    ),
    EmotionalState(
        id = "sad",
        emoji = "🌧️",
        label = "Sad",
        description = "Heavy and low",
        tint = Color(0xFF5B86A8)
    ),
    EmotionalState(
        id = "lost",
        emoji = "🧭",
        label = "Lost",
        description = "Don't know what to do",
        tint = Color(0xFF3A8A6E)
    )
)
