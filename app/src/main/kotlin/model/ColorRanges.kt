package model

enum class ColorRanges(
    val minRed: Int,
    val maxRed: Int,
    val minGreen: Int,
    val maxGreen: Int,
    val minBlue: Int,
    val maxBlue: Int,
) {
    //when user have to click somewhere to skip
    //0,5,10,15 lvl's and prestige 1,2,3
    NOTIFICATION_RED(
        minRed = 100,
        maxRed = 180,
        minGreen = 0,
        maxGreen = 10,
        minBlue = 0,
        maxBlue = 10,
    ),

    //Prestige and pictures on available perks
    WHITE(
        minRed = 240,
        maxRed = 255,
        minGreen = 240,
        maxGreen = 255,
        minBlue = 240,
        maxBlue = 255,
    ),

    //white color on unavailable perks
    //used in order to check if node exist or empty
    //if bitmap doesn't have white - don't have node.
    UNAVAILABLE_WHITE(
        minRed = 136,
        maxRed = 195,
        minGreen = 132,
        maxGreen = 180,
        minBlue = 139,
        maxBlue = 158,
    ),

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
        minBlue = 16,
        maxBlue = 40,
    ),

    GREEN(
        minRed = 13,
        maxRed = 25,
        minGreen = 47,
        maxGreen = 75,
        minBlue = 14,
        maxBlue = 30,
    ),

    //locked = 40-44,18-25,48-52
    PURPLE(
        minRed = 50,
        maxRed = 70,
        minGreen = 20,
        maxGreen = 35,
        minBlue = 55,
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
    );

    fun isColorInRange(blue: Int, green: Int, red: Int): Boolean {
        val colorRedState = red in minRed..maxRed
        val colorGreenState = green in minGreen..maxGreen
        val colorBlueState = blue in minBlue..maxBlue

        return colorRedState && colorGreenState && colorBlueState
    }
}