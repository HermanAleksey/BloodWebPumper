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
    AVAILABLE_NODE(
        minRed = 120,
        maxRed = 156,
        minGreen = 112,
        maxGreen = 148,
        minBlue = 84,
        maxBlue = 116,
    ),

    LOCKED_NODE(
        minRed = 0,
        maxRed = 5,
        minGreen = 0,
        maxGreen = 5,
        minBlue = 0,
        maxBlue = 5,
    ),

    BOUGHT_NODE(
        minRed = 140,
        maxRed = 220,
        minGreen = 0,
        maxGreen = 20,
        minBlue = 0,
        maxBlue = 20,
    ),

    //работает через раз :)
    //Могут быть кейсы где взятый принимается за него.
    //Нужно проверять на него в последнюю очередь
    UNAVAILABLE_NODE(
        minRed = 45,
        maxRed = 53,
        minGreen = 45,
        maxGreen = 54,
        minBlue = 56,
        maxBlue = 63,
    ),

    //todo don't work
    BROWN(
        minRed = 68,
        maxRed = 74,
        minGreen = 52,
        maxGreen = 65,
        minBlue = 42,
        maxBlue = 52,
    ),



    WHITE(
        minRed = 245,
        maxRed = 255,
        minGreen = 245,
        maxGreen = 255,
        minBlue = 245,
        maxBlue = 255,
    );

    fun isColorInRange(blue: Int, green: Int, red: Int): Boolean {
        val colorRedState = red in minRed..maxRed
        val colorGreenState = green in minGreen..maxGreen
        val colorBlueState = blue in minBlue..maxBlue

        return colorRedState && colorGreenState && colorBlueState
    }
}

fun Color.isInRange(range: ColorRanges): Boolean {
    val colorRedState = this.red in range.minRed..range.maxRed
    val colorGreenState = this.green in range.minGreen..range.maxGreen
    val colorBlueState = this.blue in range.minBlue..range.maxBlue

    return colorRedState && colorGreenState && colorBlueState
}