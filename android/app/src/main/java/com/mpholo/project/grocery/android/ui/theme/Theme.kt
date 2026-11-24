package com.mpholo.project.grocery.android.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Fresh-grocery brand palette (green + warm amber accent), applied consistently
// in light and dark instead of wallpaper-based dynamic color.
private val BrandGreen = Color(0xFF1E9E57)
private val BrandGreenDark = Color(0xFF14713E)
private val LeafContainer = Color(0xFFCFF2DD)
private val OnLeafContainer = Color(0xFF05351C)
private val Amber = Color(0xFFF2A413)
private val AmberContainer = Color(0xFFFFE7BC)
private val OnAmberContainer = Color(0xFF3B2A00)

private val LightColors = lightColorScheme(
    primary = BrandGreen,
    onPrimary = Color.White,
    primaryContainer = LeafContainer,
    onPrimaryContainer = OnLeafContainer,
    secondary = Color(0xFF4E6B59),
    secondaryContainer = Color(0xFFD6E8DC),
    onSecondaryContainer = Color(0xFF0C1F15),
    tertiary = Amber,
    tertiaryContainer = AmberContainer,
    onTertiaryContainer = OnAmberContainer,
    background = Color(0xFFF6FBF6),
    onBackground = Color(0xFF171D18),
    surface = Color(0xFFF6FBF6),
    onSurface = Color(0xFF171D18),
    surfaceVariant = Color(0xFFE7EDE7),
    onSurfaceVariant = Color(0xFF414941),
    outline = Color(0xFF9AA69C),
    outlineVariant = Color(0xFFC4D1C6),
    error = Color(0xFFBA1A1A)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF7CDCA2),
    onPrimary = Color(0xFF00391D),
    primaryContainer = BrandGreenDark,
    onPrimaryContainer = LeafContainer,
    secondary = Color(0xFFB4CCBC),
    secondaryContainer = Color(0xFF364B3F),
    onSecondaryContainer = Color(0xFFD0E8D8),
    tertiary = Color(0xFFFFD08A),
    tertiaryContainer = Color(0xFF5B4300),
    onTertiaryContainer = AmberContainer,
    background = Color(0xFF0F140F),
    onBackground = Color(0xFFDEE4DD),
    surface = Color(0xFF0F140F),
    onSurface = Color(0xFFDEE4DD),
    surfaceVariant = Color(0xFF414941),
    onSurfaceVariant = Color(0xFFC0C9BF),
    outline = Color(0xFF8A938B),
    outlineVariant = Color(0xFF414B43),
    error = Color(0xFFFFB4AB)
)

@Composable
fun GroceryTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = Typography(),
        content = content
    )
}
