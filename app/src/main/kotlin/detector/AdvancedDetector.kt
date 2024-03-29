package detector

import Constants
import Constants.NODE_SIZE_PX
import model.BloodWeb
import model.ColorRanges
import model.InfoNode
import model.Node
import java.awt.Color
import java.awt.geom.Ellipse2D
import java.awt.image.BufferedImage


class AdvancedDetector : Detector {

    data class NodeStateQuality(
        val state: InfoNode.State,
        val quality: InfoNode.Quality,
    )

    override fun analyzeBloodWebPageState(bufferedImage: BufferedImage): BloodWeb.PageState {
        val isPrestigeLevelState = checkPrestigeLevelState(bufferedImage)
        if (isPrestigeLevelState)
            return BloodWeb.PageState.PRESTIGE
        else {
            val isSkipableNotification = checkSkipableNotificationState(bufferedImage)
            if (isSkipableNotification)
                return BloodWeb.PageState.NOTIFICATION
        }

        return BloodWeb.PageState.LEVEL
    }

    override fun processNodeStateQuality(
        node: Node,
        bufferedImage: BufferedImage
    ): NodeStateQuality {
        val nodeBufferedImage = bufferedImage.getSubimage(
            node.topLeftCoordinates.x,
            node.topLeftCoordinates.y,
            NODE_SIZE_PX,
            NODE_SIZE_PX
        ).clipCircle()

        return checkNodeState(nodeBufferedImage)
    }

    private fun BufferedImage.clipCircle(): BufferedImage {
        val size = this.width
        val circleBuffer = BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB)
        val g2 = circleBuffer.createGraphics()
        g2.clip = Ellipse2D.Float(0f, 0f, size.toFloat(), size.toFloat())
        g2.drawImage(this, 0, 0, size, size, null)
        println("new")

        return circleBuffer
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
        val centerNodeImage = bufferedImage.getSubimage(
            Constants.BLOOD_WEB_CENTER_X - NODE_SIZE_PX / 2,
            Constants.BLOOD_WEB_CENTER_Y - NODE_SIZE_PX / 2,
            NODE_SIZE_PX,
            NODE_SIZE_PX,
        )
        val notificationRedPx = getPixelsOfColorAmount(centerNodeImage).state.notificationRedPx
        println("redPx: $notificationRedPx")
        return (notificationRedPx > THRESHOLD_OF_RED_NOTIFICATION_PX)
    }

    var i = 0

    //fills info about node state and quality, if possible
    private fun checkNodeState(bufferedImage: BufferedImage): NodeStateQuality {
        getPixelsOfColorAmount(bufferedImage).apply {
            i++
            println("$i = ${this.state}")
            println("$i = ${this.quality}")

            var nodeState = when {
                state.boughtPx > THRESHOLD_OF_BOUGHT_PX -> InfoNode.State.BOUGHT
                state.lockedPx > THRESHOLD_OF_LOCKED_PX -> InfoNode.State.LOCKED
                else -> null
            }
            if (nodeState != null)
                return NodeStateQuality(nodeState, quality = InfoNode.Quality.BROWN)

            var qualityState = when {
                quality.redPx > THRESHOLD_OF_RED_PX -> InfoNode.Quality.IRIDESCENT
                quality.purplePx > THRESHOLD_OF_PURPLE_PX -> InfoNode.Quality.PURPLE
                quality.greenPx > THRESHOLD_OF_GREEN_PX -> InfoNode.Quality.GREEN
                quality.yellowPx > THRESHOLD_OF_YELLOW_PX -> InfoNode.Quality.YELLOW
                else -> null
            }
            if (qualityState != null) {
                //we have colored perk for sure
                nodeState = when {
                    state.availablePx > THRESHOLD_OF_AVAILABLE_PX -> InfoNode.State.AVAILABLE
                    state.unavailableWhitePx > THRESHOLD_OF_UNAVAILABLE_PX -> InfoNode.State.UNAVAILABLE
                    else -> InfoNode.State.EMPTY
                }
                return NodeStateQuality(state = nodeState, quality = qualityState)
            }

            //here we have to define brown and event perks of available and unavailable state
            if (quality.brownPx > THRESHOLD_OF_BROWN_PX) {
                //enough brown pixels
                qualityState = InfoNode.Quality.BROWN
                nodeState = InfoNode.State.AVAILABLE

                return NodeStateQuality(state = nodeState, quality = qualityState)
            }
            nodeState = InfoNode.State.EMPTY
            qualityState = InfoNode.Quality.BROWN

            println("state: $nodeState")
            println("quality:$quality")

            return NodeStateQuality(state = nodeState, quality = qualityState)
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
        var unavailableWhite = 0

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

                    ColorRanges.WHITE.isColorInRange(
                        red = color.red,
                        green = color.green,
                        blue = color.blue
                    ) -> prestigeWhite++

                    ColorRanges.UNAVAILABLE_WHITE.isColorInRange(
                        red = color.red,
                        green = color.green,
                        blue = color.blue
                    ) -> unavailableWhite++
                }
            }
        }

        return ColorDistribution(
            ColorDistribution.State(
                availablePx, lockedPx, boughtPx, notificationRedPx, prestigeWhite, unavailableWhite,
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

            val notificationRedPx: Int,
            val prestigeWhitePx: Int,
            val unavailableWhitePx: Int,
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

        private const val AMOUNT_OF_CUTTER_OUT_PX = 1451
        const val THRESHOLD_OF_AVAILABLE_PX = 200
        const val THRESHOLD_OF_LOCKED_PX = AMOUNT_OF_CUTTER_OUT_PX + 600
        const val THRESHOLD_OF_BOUGHT_PX = 420

        //if bitmap have less than this white pixels - node is empty
        const val THRESHOLD_OF_UNAVAILABLE_PX = 30

        const val THRESHOLD_OF_RED_PX = 200
        const val THRESHOLD_OF_PURPLE_PX = 80
        const val THRESHOLD_OF_GREEN_PX = 100
        const val THRESHOLD_OF_YELLOW_PX = 200
        const val THRESHOLD_OF_BROWN_PX = 100
    }
}