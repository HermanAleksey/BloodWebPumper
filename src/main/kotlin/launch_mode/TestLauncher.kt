package launch_mode

import blood_web.ColorRanges
import helper.convertIntoBufferImage
import org.opencv.core.Core
import org.opencv.core.Mat
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File


class TestLauncher : AppLauncher {

    init {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    override fun run() {
        colorRanges()
    }

    private fun colorRanges() {

        val qualities = arrayOf("brown", "yellow", "green", "purple", "red", "event")
        val states = arrayOf("available", "locked", "taken", "unavailable")

        states.forEach {
            val filePath = "resources/nodes/yellow/$it.png"
            val file = File(filePath)
            println(checkNodeState(file.convertIntoBufferImage()))
        }
    }

    private fun checkNodeState(bufferedImage: BufferedImage): String {

        // Fill Matrix with image values
//        val pixels: ByteArray? = (bufferedImage.raster.dataBuffer as DataBufferByte).data
//        val mat =  Mat(bufferedImage.height, bufferedImage.width, CvType.CV_8UC3)
//        mat.put(0, 0, pixels);
        getPixelsOfColorAmount(bufferedImage).apply {
            val state = when {
                state.availablePx > 400 -> "available"
                state.lockedPx > 600 -> "locked"
                state.boughtPx > 600 -> "bought"
                else -> "unavailable"
            }
            val quality = when {
                quality.redPx > 200 -> "red"
                quality.purplePx > 80 -> "purple"
                quality.greenPx > 100 -> "green"
                quality.yellowPx > 200 -> "yellow"
//                quality.brownPx > 0 -> "brown"
                else -> "brown"
            }

            println(this.quality)
            return "state:$state quality:$quality"
        }
    }

    //how are colors distributed on image
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

    private fun getPixelsOfColorAmount(nodeImageMat: Mat): ColorDistribution {
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
        for (i in 0 until nodeImageMat.rows()) {
            for (j in 0 until nodeImageMat.cols()) {
                //perhaps it's BGR, not HSV
                val bgrColor: DoubleArray = nodeImageMat.get(i, j)
                val colorName = "${bgrColor[0]} ${bgrColor[1]} ${bgrColor[2]}"
                val repeatedTimes = colorToAmountMap[colorName] ?: 0
                colorToAmountMap[colorName] = repeatedTimes + 1

                when {
                    ColorRanges.AVAILABLE_NODE.isColorInRange(
                        red = bgrColor[2].toInt(),
                        green = bgrColor[1].toInt(),
                        blue = bgrColor[0].toInt()
                    ) -> availablePx++

                    ColorRanges.LOCKED_NODE.isColorInRange(
                        red = bgrColor[2].toInt(),
                        green = bgrColor[1].toInt(),
                        blue = bgrColor[0].toInt()
                    ) -> lockedPx++

                    ColorRanges.BOUGHT_NODE.isColorInRange(
                        red = bgrColor[2].toInt(),
                        green = bgrColor[1].toInt(),
                        blue = bgrColor[0].toInt()
                    ) -> boughtPx++

                    ColorRanges.RED.isColorInRange(
                        red = bgrColor[2].toInt(),
                        green = bgrColor[1].toInt(),
                        blue = bgrColor[0].toInt()
                    ) -> redPx++

                    ColorRanges.PURPLE.isColorInRange(
                        red = bgrColor[2].toInt(),
                        green = bgrColor[1].toInt(),
                        blue = bgrColor[0].toInt()
                    ) -> purplePx++

                    ColorRanges.GREEN.isColorInRange(
                        red = bgrColor[2].toInt(),
                        green = bgrColor[1].toInt(),
                        blue = bgrColor[0].toInt()
                    ) -> greenPx++

                    ColorRanges.YELLOW.isColorInRange(
                        red = bgrColor[2].toInt(),
                        green = bgrColor[1].toInt(),
                        blue = bgrColor[0].toInt()
                    ) -> yellowPx++

                    ColorRanges.BROWN.isColorInRange(
                        red = bgrColor[2].toInt(),
                        green = bgrColor[1].toInt(),
                        blue = bgrColor[0].toInt()
                    ) -> brownPx++

                    ColorRanges.EVENT.isColorInRange(
                        red = bgrColor[2].toInt(),
                        green = bgrColor[1].toInt(),
                        blue = bgrColor[0].toInt()
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