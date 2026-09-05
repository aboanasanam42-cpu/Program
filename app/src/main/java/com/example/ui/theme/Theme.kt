package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = HeaderBlue,
    onPrimary = ClayCardHighlight,
    primaryContainer = TriangleBlueLight,
    onPrimaryContainer = HeaderBlueDark,
    secondary = ProportionWood,
    onSecondary = ClayCardHighlight,
    secondaryContainer = ProportionWoodLight,
    onSecondaryContainer = ProportionWood,
    tertiary = RatioTeal,
    onTertiary = ClayCardHighlight,
    tertiaryContainer = RatioTealLight,
    onTertiaryContainer = RatioTeal,
    background = ClayBackground,
    onBackground = TextNavyDark,
    surface = ClayCardSurface,
    onSurface = TextNavyDark,
    surfaceVariant = ClayCardBorder,
    onSurfaceVariant = TextNavyMedium
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = ClayBackground.toArgb()
            window.navigationBarColor = ClayBackground.toArgb()
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = true
                isAppearanceLightNavigationBars = true
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
