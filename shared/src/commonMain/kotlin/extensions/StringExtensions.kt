package extensions

import androidx.compose.ui.graphics.Color

fun String.hexToRgbColor(): Color {
    require(this[0] == '#') { "A valid color value starts with a '#'" }

    if (length != 7 || this[0] != '#') {
        return Color.Black.copy(0.5f)
    }

    val r = substring(1, 3).toInt(16)
    val g = substring(3, 5).toInt(16)
    val b = substring(5, 7).toInt(16)

    return Color(r, g, b)
}