package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme =
  darkColorScheme(
    primary = GoldAccentLight,
    onPrimary = ForestGreenDark,
    primaryContainer = ForestGreen,
    onPrimaryContainer = GoldAccentLight,
    secondary = FreshGreenLight,
    onSecondary = Color.Black,
    secondaryContainer = DarkGreenCard,
    onSecondaryContainer = CreamBackground,
    tertiary = GoldAccent,
    background = DarkGreenBackground,
    onBackground = CreamBackground,
    surface = DarkGreenSurface,
    onSurface = CreamBackground,
    surfaceVariant = DarkGreenCard,
    onSurfaceVariant = GoldAccentLight
  )

private val LightColorScheme =
  lightColorScheme(
    primary = ForestGreen,
    onPrimary = Color.White,
    primaryContainer = CreamSurface,
    onPrimaryContainer = ForestGreenDark,
    secondary = FreshGreen,
    onSecondary = Color.White,
    secondaryContainer = CreamSurfaceVariant,
    onSecondaryContainer = ForestGreenDark,
    tertiary = GoldAccentDark,
    background = CreamBackground,
    onBackground = TextPrimaryDark,
    surface = Color.White,
    onSurface = TextPrimaryDark,
    surfaceVariant = CreamSurface,
    onSurfaceVariant = TextSecondaryDark
  )

@Composable
fun DwarkeshTheme(
  darkTheme: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  DwarkeshTheme(darkTheme = darkTheme, content = content)
}

