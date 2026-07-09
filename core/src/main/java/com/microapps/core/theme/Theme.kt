package com.microapps.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private fun buildColorScheme(
    accent: Color,
    accentLight: Color,
    accentDim: Color
) = darkColorScheme(
    primary             = accent,
    onPrimary           = Color.White,
    primaryContainer    = accentDim,
    onPrimaryContainer  = accentLight,
    secondary           = accentLight,
    onSecondary         = BaseBackground,
    secondaryContainer  = BaseSurfaceVar,
    onSecondaryContainer = BaseOnBackground,
    tertiary            = accentLight,
    onTertiary          = BaseBackground,
    background          = BaseBackground,
    onBackground        = BaseOnBackground,
    surface             = BaseSurface,
    onSurface           = BaseOnSurface,
    surfaceVariant      = BaseSurfaceVar,
    onSurfaceVariant    = BaseSubtle,
    outline             = BaseOutline,
    error               = BaseError,
    onError             = Color.White
)

val ShiftColorScheme = buildColorScheme(
    accent      = ShiftViolet,
    accentLight = ShiftVioletLight,
    accentDim   = ShiftVioletDim
)

@Composable
fun ShiftTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ShiftColorScheme,
        typography  = MicroAppTypography,
        content     = content
    )
}
