package com.microapps.shift

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.microapps.shift.data.ShiftUiState
import com.microapps.shift.data.ShiftViewModel
import com.microapps.shift.ui.CompletionScreen
import com.microapps.shift.ui.HomeScreen
import com.microapps.shift.ui.ProtocolScreen

@Composable
fun ShiftApp(viewModel: ShiftViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    AnimatedContent(
        targetState = uiState,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "shift_root"
    ) { state ->
        when (state) {
            is ShiftUiState.Idle -> HomeScreen(
                onStateSelected = { viewModel.onStateSelected(it) },
                modifier = Modifier.fillMaxSize()
            )

            is ShiftUiState.Loading -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }

            is ShiftUiState.Active -> ProtocolScreen(
                protocol         = state.protocol,
                currentStepIndex = state.currentStepIndex,
                onNext           = { viewModel.nextStep() },
                onClose          = { viewModel.reset() },
                modifier         = Modifier.fillMaxSize()
            )

            is ShiftUiState.Complete -> CompletionScreen(
                onReturn = { viewModel.reset() },
                modifier = Modifier.fillMaxSize()
            )

            is ShiftUiState.Error -> Box(
                modifier = Modifier.fillMaxSize().padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.foundation.layout.Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Couldn't load your protocol.",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = state.message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
                    )
                    Button(onClick = { viewModel.retry() }) {
                        Text("Try again")
                    }
                }
            }
        }
    }
}
