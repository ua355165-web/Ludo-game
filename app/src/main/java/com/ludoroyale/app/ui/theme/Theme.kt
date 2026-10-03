package com.ludoroyale.app.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.ludoroyale.app.settings.ThemeChoice

private val Ink = Color(0xFF19182B)
private val Cream = Color(0xFFFFFBF5)
private val Coral = Color(0xFFFF6B5F)
private val Purple = Color(0xFF6C5CE7)
private val Gold = Color(0xFFF4B942)

private val LightColors = lightColorScheme(primary = Purple, secondary = Coral, tertiary = Gold, background = Cream, surface = Color.White, onBackground = Ink, onSurface = Ink)
private val DarkColors = darkColorScheme(primary = Color(0xFFB9AEFF), secondary = Color(0xFFFF9A8F), tertiary = Color(0xFFFFD477), background = Color(0xFF151420), surface = Color(0xFF211F30))

@Composable fun LudoRoyaleTheme(themeChoice: ThemeChoice = ThemeChoice.SYSTEM, content: @Composable () -> Unit) {
    val dark = when (themeChoice) {
        ThemeChoice.DARK -> true
        ThemeChoice.LIGHT -> false
        ThemeChoice.SYSTEM -> androidx.compose.foundation.isSystemInDarkTheme()
    }
    MaterialTheme(colorScheme = if (dark) DarkColors else LightColors, typography = Typography(), content = content)
}
