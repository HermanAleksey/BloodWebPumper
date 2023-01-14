package presentation.compose.theming

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

//source for material themes
//https://material.io/design/color/the-color-system.html#color-theme-creation
private val DarkColorPalette = darkColors(
    primary = Purple500,
    //used to provide accents
    secondary = Green300,
    //screen background uses it
    surface = ColorPlatinum,
    onSurface = Green700,
    //app background uses it
    background = Green700,
    onBackground = ColorPlatinum
)

@Composable
fun BloodWebAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors =         DarkColorPalette

    MaterialTheme(
        colors = colors,
        shapes = Shapes,
        content = content
    )
}