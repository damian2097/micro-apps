package com.microapps.core.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    background = CiceroneBackground,
    surface = CiceroneSurface,
    primary = CiceronePrimary,
    secondary = CiceroneAccent,
    tertiary = CiceronePrimary,
    onBackground = CiceroneTextPrimary,
    onSurface = CiceroneTextPrimary,
    onPrimary = CiceroneBackground,
    surfaceVariant = CiceroneBackground,
    onSurfaceVariant = CiceroneTextSecondary,
    outline = CiceroneBorder,
    error = CiceroneAccent
)

@Composable
fun ShiftTheme(
    darkTheme: Boolean = false, // Force light theme
    dynamicColor: Boolean = false, // Force custom colors
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = true
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
