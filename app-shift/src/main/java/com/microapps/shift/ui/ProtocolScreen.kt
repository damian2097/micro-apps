package com.microapps.shift.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.microapps.core.network.ProtocolStep
import com.microapps.core.network.ShiftProtocol
import com.microapps.core.ui.GlassCard

@Composable
fun ProtocolScreen(
    protocol: ShiftProtocol,
    currentStepIndex: Int,
    onNext: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val totalSteps = protocol.steps.size
    val progress   = (currentStepIndex + 1).toFloat() / totalSteps.toFloat()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Faint top gradient to differentiate the protocol screen
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.07f),
                            Color.Transparent
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // ── Top bar ─────────────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text          = "SHIFT PROTOCOL",
                        style         = MaterialTheme.typography.labelSmall,
                        color         = MaterialTheme.colorScheme.primary,
                        letterSpacing = 2.sp
                    )
                    Text(
                        text       = protocol.title,
                        style      = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color      = MaterialTheme.colorScheme.onBackground
                    )
                }
                IconButton(onClick = onClose) {
                    Icon(
                        imageVector  = Icons.Default.Close,
                        contentDescription = "Close",
                        tint         = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // ── Progress ─────────────────────────────────────────────────────
            LinearProgressIndicator(
                progress  = { progress },
                modifier  = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(3.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color      = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            )
            Text(
                text     = "Step ${currentStepIndex + 1} of $totalSteps",
                style    = MaterialTheme.typography.labelSmall,
                color    = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            // ── Step content ─────────────────────────────────────────────────
            AnimatedContent(
                targetState  = currentStepIndex,
                transitionSpec = {
                    (slideInHorizontally { it } + fadeIn()) togetherWith
                    (slideOutHorizontally { -it } + fadeOut())
                },
                label = "step_content"
            ) { idx ->
                val step = protocol.steps.getOrNull(idx)
                if (step != null) {
                    StepContent(
                        step     = step,
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // ── Next button ──────────────────────────────────────────────────
            Button(
                onClick  = onNext,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 24.dp)
                    .height(56.dp),
                shape  = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text       = if (currentStepIndex < totalSteps - 1) "Next  →" else "Complete Shift",
                    style      = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun StepContent(step: ProtocolStep, modifier: Modifier = Modifier) {
    Column(
        modifier            = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Step type badge
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f)
        ) {
            Text(
                text          = step.type.uppercase().replace("_", " "),
                style         = MaterialTheme.typography.labelMedium,
                color         = MaterialTheme.colorScheme.primary,
                letterSpacing = 2.sp,
                modifier      = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Breathing animation only for breathing steps
        if (step.type == "breathing") {
            BreathingCircle(modifier = Modifier.padding(bottom = 24.dp))
        }

        // Step title
        Text(
            text      = step.title,
            style     = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color     = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Duration badge
        if (step.duration_seconds > 0) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Text(
                    text     = formatDuration(step.duration_seconds),
                    style    = MaterialTheme.typography.labelLarge,
                    color    = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        // Instruction card
        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Text(
                text     = step.instruction,
                style    = MaterialTheme.typography.bodyLarge,
                color    = MaterialTheme.colorScheme.onSurface,
                lineHeight = 28.sp,
                modifier = Modifier.padding(20.dp)
            )
        }
    }
}

private fun formatDuration(seconds: Int): String =
    if (seconds >= 60) "${seconds / 60} min" else "$seconds sec"
