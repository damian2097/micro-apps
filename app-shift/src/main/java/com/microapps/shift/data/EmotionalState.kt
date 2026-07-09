package com.microapps.shift.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Water
import androidx.compose.material.icons.filled.WindPower
import androidx.compose.ui.graphics.vector.ImageVector

data class EmotionalState(
    val id: String,
    val icon: ImageVector,
    val title: String,
    val description: String
)

val defaultStates = listOf(
    EmotionalState("overwhelmed", Icons.Filled.Water, "Overwhelmed", "Too much, all at once"),
    EmotionalState("anxious", Icons.Filled.Warning, "Anxious", "Racing mind, tight chest"),
    EmotionalState("failure", Icons.Filled.Error, "Like a failure", "Nothing is working"),
    EmotionalState("stuck", Icons.Filled.Build, "Stuck", "Can't make myself start"),
    EmotionalState("angry", Icons.Filled.LocalFireDepartment, "Angry", "Frustrated and reactive"),
    EmotionalState("empty", Icons.Filled.Cloud, "Empty", "Numb, nothing matters"),
    EmotionalState("burned_out", Icons.Filled.Nightlight, "Burned out", "Running on empty"),
    EmotionalState("sad", Icons.Filled.Cloud, "Sad", "Heavy and low"),
    EmotionalState("lost", Icons.Filled.Explore, "Lost", "Don't know what to do")
)
