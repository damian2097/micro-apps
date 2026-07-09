package com.microapps.shift.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.microapps.core.network.ShiftApiClient
import com.microapps.core.network.ShiftProtocol
import com.microapps.shift.BuildConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class ShiftUiState {
    data object Idle    : ShiftUiState()
    data object Loading : ShiftUiState()
    data class  Active(val protocol: ShiftProtocol, val currentStepIndex: Int = 0) : ShiftUiState()
    data object Complete : ShiftUiState()
    data class  Error(val message: String) : ShiftUiState()
}

class ShiftViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<ShiftUiState>(ShiftUiState.Idle)
    val uiState: StateFlow<ShiftUiState> = _uiState.asStateFlow()

    var selectedState: EmotionalState? = null
        private set

    fun onStateSelected(state: EmotionalState) {
        selectedState = state
        _uiState.value = ShiftUiState.Loading
        viewModelScope.launch {
            ShiftApiClient.getProtocol(
                state       = state.id,
                description = state.description,
                useMock     = BuildConfig.USE_MOCK_API
            ).fold(
                onSuccess = { protocol ->
                    _uiState.value = ShiftUiState.Active(protocol = protocol)
                },
                onFailure = { error ->
                    _uiState.value = ShiftUiState.Error(
                        error.message ?: "Something went wrong. Please try again."
                    )
                }
            )
        }
    }

    fun nextStep() {
        val current = _uiState.value as? ShiftUiState.Active ?: return
        val next = current.currentStepIndex + 1
        _uiState.value = if (next >= current.protocol.steps.size) {
            ShiftUiState.Complete
        } else {
            current.copy(currentStepIndex = next)
        }
    }

    fun reset() {
        selectedState = null
        _uiState.value = ShiftUiState.Idle
    }

    fun retry() {
        selectedState?.let { onStateSelected(it) }
    }
}
