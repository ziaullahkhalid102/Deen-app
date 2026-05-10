package com.deenapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DeenLightColorScheme = lightColorScheme(
    primary = DeenGreenPrimary,
    onPrimary = DeenWhite,
    primaryContainer = DeenGreenContainer,
    onPrimaryContainer = DeenGreenDark,
    secondary = DeenGreenLight,
    onSecondary = DeenWhite,
    secondaryContainer = DeenGreenSurface,
    onSecondaryContainer = DeenGreenDark,
    tertiary = DeenGold,
    onTertiary = DeenWhite,
    tertiaryContainer = DeenGoldLight,
    onTertiaryContainer = DeenGreenDark,
    background = DeenWhite,
    onBackground = DeenGray900,
    surface = DeenWhite,
    onSurface = DeenGray900,
    surfaceVariant = DeenGray100,
    onSurfaceVariant = DeenGray700,
    outline = DeenGray300,
    outlineVariant = DeenGray200,
    error = DeenError,
    onError = DeenWhite
)

private val DeenDarkColorScheme = darkColorScheme(
    primary = DeenGreenLight,
    onPrimary = DeenWhite,
    primaryContainer = DeenGreenDark,
    onPrimaryContainer = DeenGreenSurface,
    secondary = DeenGreenContainer,
    onSecondary = DeenGreenDark,
    secondaryContainer = DeenDarkSurfaceVariant,
    onSecondaryContainer = DeenGreenSurface,
    tertiary = DeenGoldLight,
    onTertiary = DeenGreenDark,
    tertiaryContainer = DeenGold,
    onTertiaryContainer = DeenWhite,
    background = DeenDarkBackground,
    onBackground = DeenGray200,
    surface = DeenDarkSurface,
    onSurface = DeenGray200,
    surfaceVariant = DeenDarkSurfaceVariant,
    onSurfaceVariant = DeenGray400,
    outline = DeenGray700,
    outlineVariant = DeenGray800,
    error = DeenError,
    onError = DeenWhite
)

@Composable
fun DeenAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DeenDarkColorScheme
        else -> DeenLightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = DeenTypography,
        shapes = DeenShapes,
        content = content
    )
}
