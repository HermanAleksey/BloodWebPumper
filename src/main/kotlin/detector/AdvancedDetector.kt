package detector

import blood_web.ColorRanges
import blood_web.Node
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File

class AdvancedDetector : Detector{

    data class ColorDistribution(
        val state: State,
        val quality: Quality,
    ) {
        data class State(
            val availablePx: Int,
            val lockedPx: Int,
            val boughtPx: Int,
            //unavailable px can't be counted
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

    override fun processNodeStateQuality(node: Node, bufferedImage: BufferedImage) {
        val filePath = "resources/nodes/yellow/available.png"
        val file = File(filePath)
//        println(checkNodeState(file.convertIntoBufferImage()))


        TODO("Not yet implemented")
    }

    override fun analyzeCenterOfBloodWeb(bufferedImage: BufferedImage): Boolean {
        TODO("Not yet implemented")
    }

    //fills info about node state and quality, if possible
    private fun checkNodeState(node: Node, bufferedImage: BufferedImage): Node {

        // Fill Matrix with image values
//        val pixels: ByteArray? = (bufferedImage.raster.dataBuffer as DataBufferByte).data
//        val mat =  Mat(bufferedImage.height, bufferedImage.width, CvType.CV_8UC3)
//        mat.put(0, 0, pixels);
        getPixelsOfColorAmount(bufferedImage).apply {
            val state = when {
                state.availablePx > 400 -> Node.State.AVAILABLE
                state.lockedPx > 600 -> Node.State.LOCKED
                state.boughtPx > 600 -> Node.State.BOUGHT
                else -> Node.State.UNAVAILABLE
            }
            val quality = when {
                quality.redPx > 200 -> Node.Quality.IRIDESCENT
                quality.purplePx > 80 -> Node.Quality.PURPLE
                quality.greenPx > 100 -> Node.Quality.GREEN
                quality.yellowPx > 200 -> Node.Quality.YELLOW
//                quality.brownPx > 0 -> "brown"
                else -> Node.Quality.BROWN
            }

            println(this.quality)
            return node.apply {
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
                }
            }
        }

        return ColorDistribution(
            ColorDistribution.State(
                availablePx, lockedPx, boughtPx
            ),
            ColorDistribution.Quality(
                brownPx, yellowPx, greenPx, purplePx, redPx
            )
        )
    }

}