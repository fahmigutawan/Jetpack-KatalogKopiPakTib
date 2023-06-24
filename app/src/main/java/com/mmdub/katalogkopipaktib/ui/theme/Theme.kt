package com.mmdub.katalogkopipaktib.ui.theme

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

private val DarkColorScheme = darkColorScheme(
    primary = AppColor.Primary40,
    onPrimary = AppColor.Primary100,
    primaryContainer = AppColor.Primary90,
    onPrimaryContainer = AppColor.Primary10,
    inversePrimary = AppColor.Primary80,
    secondary = AppColor.Secondary40,
    onSecondary = AppColor.Secondary100,
    secondaryContainer = AppColor.Secondary90,
    onSecondaryContainer = AppColor.Secondary10,
    tertiary = AppColor.Tertiary40,
    onTertiary = AppColor.Tertiary100,
    tertiaryContainer = AppColor.Tertiary90,
    onTertiaryContainer = AppColor.Tertiary10,
    surface = AppColor.Neutral99,
    onSurface = AppColor.Neutral10,
    surfaceVariant =AppColor.NeutralVariant99,
    onSurfaceVariant = AppColor.NeutralVariant30,
    inverseSurface = AppColor.Neutral20,
    inverseOnSurface = AppColor.Neutral95,
    error = AppColor.Error40,
    onError = AppColor.Error100,
    errorContainer = AppColor.Error90,
    onErrorContainer = AppColor.Error10,
    outline = AppColor.NeutralVariant50,
    outlineVariant = AppColor.NeutralVariant80,
    scrim = AppColor.Neutral0
)

private val LightColorScheme = lightColorScheme(
    primary = AppColor.Primary80,
    onPrimary = AppColor.Primary20,
    primaryContainer = AppColor.Primary30,
    onPrimaryContainer = AppColor.Primary90,
    inversePrimary = AppColor.Primary40,
    secondary = AppColor.Secondary80,
    onSecondary = AppColor.Secondary20,
    secondaryContainer = AppColor.Secondary30,
    onSecondaryContainer = AppColor.Secondary90,
    tertiary = AppColor.Tertiary80,
    onTertiary = AppColor.Tertiary20,
    tertiaryContainer = AppColor.Tertiary30,
    onTertiaryContainer = AppColor.Tertiary90,
    surface = AppColor.Neutral10,
    onSurface = AppColor.Neutral90,
    surfaceVariant =AppColor.NeutralVariant10,
    onSurfaceVariant = AppColor.NeutralVariant80,
    inverseSurface = AppColor.Neutral90,
    inverseOnSurface = AppColor.Neutral20,
    error = AppColor.Error80,
    onError = AppColor.Error20,
    errorContainer = AppColor.Error30,
    onErrorContainer = AppColor.Error90,
    outline = AppColor.NeutralVariant60,
    outlineVariant = AppColor.NeutralVariant30,
    scrim = AppColor.Neutral0
)

@Composable
fun KatalogKopiPakTibTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}