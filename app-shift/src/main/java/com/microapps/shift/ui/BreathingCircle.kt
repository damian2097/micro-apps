package com.microapps.shift.ui

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Box breathing visualization: 4s inhale → 4s hold → 4s exhale → 4s hold.
 * Circle expands on inhale, contracts on exhale, holds steady during holds.
 */
@Composable
fun BreathingCircle(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "breathing")

    // Phase 0..1 over 16s represents the full box breathing cycle
    val phase by infiniteTransition.animateFloat(
        initialValue  = 0f,
        targetValue   = 1f,
        animationSpec = infiniteRepeatable(
            animation  = tween(16_000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )

    val breathLabel = when {
        phase < 0.25f -> "Breathe in..."
        phase < 0.50f -> "Hold..."
        phase < 0.75f -> "Breathe out..."
        else          -> "Hold..."
    }

    // Circle size: expands 0→1 over 4s, holds at 1 for 4s, contracts 1→0 over 4s, holds at 0 for 4s
    val circleScale by infiniteTransition.animateFloat(
        initialValue  = 0.55f,
        targetValue   = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 16_000
                val smooth = CubicBezierEasing(0.4f, 0f, 0.6f, 1f)
                0.55f at 0         using smooth
                1.00f at 4_000     using LinearEasing
                1.00f at 8_000     using smooth
                0.55f at 12_000    using LinearEasing
                0.55f at 16_000
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "circle_scale"
    )

    val primaryColor = MaterialTheme.colorScheme.primary

    Box(
        modifier         = modifier.size(200.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val maxRadius     = size.minDimension / 2f
            val currentRadius = maxRadius * circleScale

            // Static outer rim
            drawCircle(
                color  = primaryColor.copy(alpha = 0.08f),
                radius = maxRadius
            )
            // Animated fill
            drawCircle(
                color  = primaryColor.copy(alpha = 0.18f),
                radius = currentRadius
            )
            // Animated border
            drawCircle(
                color  = primaryColor.copy(alpha = 0.65f),
                radius = currentRadius,
                style  = Stroke(width = 2.dp.toPx())
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text       = breathLabel,
                style      = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Medium,
                color      = MaterialTheme.colorScheme.onBackground,
                fontSize   = 13.sp
            )
        }
    }
}
