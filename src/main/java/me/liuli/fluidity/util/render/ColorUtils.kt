package me.liuli.fluidity.util.render

import java.awt.Color
import kotlin.math.abs

private val startTime = System.currentTimeMillis()

fun rainbow(index: Int, lowest: Float = 0.07f, bigest: Float = 0.6f, indexOffset: Int = 300): Color {
    return Color.getHSBColor((abs(((((System.currentTimeMillis() - startTime).toInt() + index * indexOffset) / 5000f) % 2) - 1) * (bigest - lowest)) + lowest, 0.7f, 1f)
}

fun Color.reAlpha(alpha: Int): Color {
    return Color(this.red, this.green, this.blue, alpha)
}

fun Color.toHexString(): String {
    return colorToHexString(this.rgb)
}

fun colorToHexString(hex: Int): String {
    val alpha = hex shr 24 and 0xFF
    val red = hex shr 16 and 0xFF
    val green = hex shr 8 and 0xFF
    val blue = hex and 0xFF
    return "#" + String.format("%02x", red) + String.format("%02x", green) + String.format("%02x", blue) + if(alpha != 255) { String.format("%02x", alpha) } else { "" }
}