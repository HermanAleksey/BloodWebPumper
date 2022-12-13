package blood_web

import java.awt.Color

enum class ColorRanges(
    val minRed: Int,
    val maxRed: Int,
    val minGreen: Int,
    val maxGreen: Int,
    val minBlue: Int,
    val maxBlue: Int,
) {
    AVAILABLE_PERK_BORDER(
        minRed = 120,
        maxRed = 156,
        minGreen = 110,
        maxGreen = 148,
        minBlue = 84,
        maxBlue = 116,
    ),

    WHITE(
        minRed = 250,
        maxRed = 255,
        minGreen = 250,
        maxGreen = 255,
        minBlue = 250,
        maxBlue = 255,
    );
}

fun Color.isInRange(range: ColorRanges): Boolean {
    val colorRedState = this.red in range.minRed..range.maxRed
    val colorGreenState = this.green in range.minGreen..range.maxGreen
    val colorBlueState = this.blue in range.minBlue..range.maxBlue

    return colorRedState && colorGreenState && colorBlueState
}