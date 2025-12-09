package com.example.template_app_comp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorSchemeLevelUp = darkColorScheme(
    primary = AzulElectrico,
    secondary = VerdeNeon,
    tertiary = AzulElectrico,
    background = FondoNegro,
    surface = FondoNegro,
    onPrimary = FondoNegro,
    onSecondary = FondoNegro,
    onTertiary = FondoNegro,
    onBackground = TextoPrincipal,
    onSurface = TextoPrincipal,
    onSurfaceVariant = TextoSecundario,
    error = androidx.compose.ui.graphics.Color(0xFFFF5252),
    onError = TextoPrincipal
)

@Composable
fun TemaLevelUp(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorSchemeLevelUp,
        typography = TypographyLevelUp,
        content = content
    )
}










