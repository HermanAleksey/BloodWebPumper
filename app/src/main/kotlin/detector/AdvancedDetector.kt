package detector

import Constants
import Constants.NODE_SIZE_PX
import blood_web.BloodWebPageState
import blood_web.ColorRanges
import blood_web.Node
import java.awt.Color
import java.awt.image.BufferedImage


class AdvancedDetector : Detector {

    override fun analyzeBloodWebPageState(bufferedImage: BufferedImage): BloodWebPageState {
        val isPrestigeLevelState = checkPrestigeLevelState(bufferedImage)
        if (isPrestigeLevelState)
            return BloodWebPageState.PRESTIGE
        else {
            val isSkipableNotification = checkSkipableNotificationState(bufferedImage)
            if (isSkipableNotification)
                return BloodWebPageState.NOTIFICATION
        }

        return BloodWebPageState.LEVEL
    }

    override fun processNodeStateQuality(node: Node, bufferedImage: BufferedImage) = with(node) {
        val nodeBufferedImage = bufferedImage.getSubimage(
            topLeftCoordinates.x,
            topLeftCoordinates.y,
            NODE_SIZE_PX,
            NODE_SIZE_PX
        )
        checkNodeState(node, nodeBufferedImage)
        println(node)
    }

    private fun checkPrestigeLevelState(bufferedImage: BufferedImage): Boolean {
        val centerNodeImage = bufferedImage.getSubimage(
            Constants.BLOOD_WEB_CENTER_X - NODE_SIZE_PX / 2,
            Constants.BLOOD_WEB_CENTER_Y - NODE_SIZE_PX / 2,
            NODE_SIZE_PX,
            NODE_SIZE_PX,
        )
        val whitePrestigePx = getPixelsOfColorAmount(centerNodeImage).state.prestigeWhitePx


        return (whitePrestigePx > THRESHOLD_OF_WHITE_PRESTIGE_PX)
    }

    private fun checkSkipableNotificationState(bufferedImage: BufferedImage): Boolean {
        val notificationRedPx = getPixelsOfColorAmount(bufferedImage).state.notificationRedPx
        return (notificationRedPx > THRESHOLD_OF_RED_NOTIFICATION_PX)
    }

    //fills info about node state and quality, if possible
    private fun checkNodeState(node: Node, bufferedImage: BufferedImage) {
        getPixelsOfColorAmount(bufferedImage).apply {
            val state = when {
                state.availablePx > THRESHOLD_OF_AVAILABLE_PX -> Node.State.AVAILABLE
                state.lockedPx > THRESHOLD_OF_LOCKED_PX -> Node.State.LOCKED
                state.boughtPx > THRESHOLD_OF_BOUGHT_PX -> Node.State.BOUGHT
                else -> Node.State.UNAVAILABLE
            }
            val quality = when {
                quality.redPx > THRESHOLD_OF_RED_PX -> Node.Quality.IRIDESCENT
                quality.purplePx > THRESHOLD_OF_PURPLE_PX -> Node.Quality.PURPLE
                quality.greenPx > THRESHOLD_OF_GREEN_PX -> Node.Quality.GREEN
                quality.yellowPx > THRESHOLD_OF_YELLOW_PX -> Node.Quality.YELLOW
//                quality.brownPx > 0 -> "brown"
                else -> Node.Quality.BROWN
            }

            println(this.quality)

            node.apply {
                this.state = state
                this.quality = quality
            }
        }
    }


    private fun getPixelsOfColorAmount(bufferedImage: BufferedImage): ColorDistribution {
        var availablePx = 0
        var lockedPx = 0
        var boughtPx = 0

        var brownPx = 0
        var yellowPx = 0
        var greenPx = 0
        var purplePx = 0
        var redPx = 0
        var eventPx = 0

        var notificationRedPx = 0
        var prestigeWhite = 0

        val colorToAmountMap = hashMapOf<String, Int>()
        for (i in 0 until bufferedImage.width) {
            for (j in 0 until bufferedImage.height) {
                //perhaps it's BGR, not HSV
                val rgbColor: Int = bufferedImage.getRGB(i, j)
                val colorName = "$rgbColor"
                val repeatedTimes = colorToAmountMap[colorName] ?: 0
                colorToAmountMap[colorName] = repeatedTimes + 1
                val color = Color(rgbColor)

                when {
                    ColorRanges.AVAILABLE_NODE.isColorInRange(
                        red = color.red,
                        green = color.green,
                        blue = color.blue
                    ) -> availablePx++

                    ColorRanges.LOCKED_NODE.isColorInRange(
                        red = color.red,
                        green = color.green,
                        blue = color.blue
                    ) -> lockedPx++

                    ColorRanges.BOUGHT_NODE.isColorInRange(
                        red = color.red,
                        green = color.green,
                        blue = color.blue
                    ) -> boughtPx++

                    ColorRanges.RED.isColorInRange(
                        red = color.red,
                        green = color.green,
                        blue = color.blue
                    ) -> redPx++

                    ColorRanges.PURPLE.isColorInRange(
                        red = color.red,
                        green = color.green,
                        blue = color.blue
                    ) -> purplePx++

                    ColorRanges.GREEN.isColorInRange(
                        red = color.red,
                        green = color.green,
                        blue = color.blue
                    ) -> greenPx++

                    ColorRanges.YELLOW.isColorInRange(
                        red = color.red,
                        green = color.green,
                        blue = color.blue
                    ) -> yellowPx++

                    ColorRanges.BROWN.isColorInRange(
                        red = color.red,
                        green = color.green,
                        blue = color.blue
                    ) -> brownPx++

                    ColorRanges.EVENT.isColorInRange(
                        red = color.red,
                        green = color.green,
                        blue = color.blue
                    ) -> eventPx++

                    ColorRanges.NOTIFICATION_RED.isColorInRange(
                        red = color.red,
                        green = color.green,
                        blue = color.blue
                    ) -> notificationRedPx++

                    ColorRanges.PRESTIGE_WHITE.isColorInRange(
                        red = color.red,
                        green = color.green,
                        blue = color.blue
                    ) -> prestigeWhite++
                }
            }
        }

        return ColorDistribution(
            ColorDistribution.State(
                availablePx, lockedPx, boughtPx, notificationRedPx, prestigeWhite
            ),
            ColorDistribution.Quality(
                brownPx, yellowPx, greenPx, purplePx, redPx
            )
        )
    }


    private data class ColorDistribution(
        val state: State,
        val quality: Quality,
    ) {
        data class State(
            val availablePx: Int,
            val lockedPx: Int,
            val boughtPx: Int,
            //unavailable px can't be counted

            val notificationRedPx: Int,
            val prestigeWhitePx: Int,
        )

        data class Quality(
            val brownPx: Int,
            val yellowPx: Int,
            val greenPx: Int,
            val purplePx: Int,
            val redPx: Int,
            //todo perhaps do event as brown
//            val eventPx: Int,
        )
    }

    companion object {
        const val THRESHOLD_OF_WHITE_PRESTIGE_PX = 150
        const val THRESHOLD_OF_RED_NOTIFICATION_PX = 20_000

        const val THRESHOLD_OF_AVAILABLE_PX = 400
        const val THRESHOLD_OF_LOCKED_PX = 600
        const val THRESHOLD_OF_BOUGHT_PX = 600

        const val THRESHOLD_OF_RED_PX = 200
        const val THRESHOLD_OF_PURPLE_PX = 80
        const val THRESHOLD_OF_GREEN_PX = 100
        const val THRESHOLD_OF_YELLOW_PX = 200
    }
}