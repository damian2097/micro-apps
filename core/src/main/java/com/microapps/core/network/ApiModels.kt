package com.microapps.core.network

import kotlinx.serialization.Serializable

@Serializable
data class ShiftRequest(
    val state: String,
    val description: String
)

@Serializable
data class ProtocolStep(
    val type: String,           // "breathing", "reframe", "action"
    val title: String,
    val duration_seconds: Int,
    val instruction: String
)

@Serializable
data class ShiftProtocol(
    val title: String,
    val steps: List<ProtocolStep>
)
