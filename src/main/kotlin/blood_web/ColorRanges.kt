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

    //todo don't work; use else branch is option
    BROWN(
        minRed = 0,
        maxRed = 0,
        minGreen = 0,
        maxGreen = 0,
        minBlue = 0,
        maxBlue = 0,
    ),

    YELLOW(
        minRed = 80,
        maxRed = 120,
        minGreen = 60,
        maxGreen = 120,
        minBlue = 25,
        maxBlue = 40,
    ),

    GREEN(
        minRed = 13,
        maxRed = 25,
        minGreen = 50,
        maxGreen = 75,
        minBlue = 14,
        maxBlue = 24,
    ),

    //locked = 40-44,18-25,48-52
    PURPLE(
        minRed = 50,
        maxRed = 70,
        minGreen = 20,
        maxGreen = 35,
        minBlue = 70,
        maxBlue = 100,
    ),

    RED(
        minRed = 70,
        maxRed = 170,
        minGreen = 10,
        maxGreen = 25,
        minBlue = 25,
        maxBlue = 61,
    ),

    EVENT(
        minRed = 0,
        maxRed = 0,
        minGreen = 0,
        maxGreen = 0,
        minBlue = 0,
        maxBlue = 0,
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