package launch_mode

import blood_web.ColorRanges
import org.opencv.core.*
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgcodecs.Imgcodecs.imread
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.File
import java.io.InputStream
import java.util.*
import javax.imageio.ImageIO


class TestLauncher : AppLauncher {

    override fun run() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//        doTestLaunch()
        colorRanges()
    }

    private fun doTestLaunch() {
        println("Welcome to OpenCV " + Core.VERSION)
        val m = Mat(5, 10, CvType.CV_8UC1, Scalar(0.0))
        println("OpenCV Mat: $m")
        val mr1 = m.row(1)
        mr1.setTo(Scalar(1.0))
        val mc5 = m.col(5)
        mc5.setTo(Scalar(5.0))
        println(
            """OpenCV Mat data:
                    ${m.dump()}
                    """.trimIndent()
        )
    }

    private fun colorRanges() {

        val qualities = arrayOf("brown", "yellow", "green", "purple", "red", "event")
        val states = arrayOf("available", "locked", "taken", "unavailable")

        states.forEach {
            val filePath = "resources/nodes/event/$it.png"
            println(checkNodeState(filePath))
        }

//        qualities.forEach {
//            println("start with: $it")
//            val colorRange = ColorRanges.BROWN
//            val filePath =  "resources/nodes/$it/${states[0]}.png"
//            getPixelsOfColorAmount(filePath = filePath, colorRanges = colorRange)
//        }
    }

    //todo все цвета можно получать за один проход цикла.
    private fun checkNodeState(filePath: String): String {
        val colorDistribution = getPixelsOfColorAmount(filePath)
        if (colorDistribution.availablePx > 400) return "available"
        if (colorDistribution.lockedPx > 600) return "locked"
        if (colorDistribution.boughtPx > 600) return "bought"
        return "unavailable"
    }

    class ColorDistribution(
        val availablePx: Int,
        val lockedPx: Int,
        val boughtPx: Int,
        //unavailable px can't be counted
    )

    private fun getPixelsOfColorAmount(filePath: String): ColorDistribution {
        // parsing file image into OpenCV Mat
        val openCVImage = imread(filePath)

        val imageSize = openCVImage.rows() * openCVImage.cols()
        var availablePx = 0
        var lockedPx = 0
        var boughtPx = 0

        val colorToAmountMap = hashMapOf<String, Int>()
        for (i in 0 until openCVImage.rows()) {
            for (j in 0 until openCVImage.cols()) {
                //perhaps it's BGR, not HSV
                val bgrColor: DoubleArray = openCVImage.get(i, j)
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
                }
            }
        }
        val filteredByColorsPercentageMap =
            colorToAmountMap.filter { it.value > imageSize * 0.01 }//only colors, that take more than 5% of photo
        println("different colors amount: ${colorToAmountMap.size} after 1% filter = [${filteredByColorsPercentageMap.size}]")

        return ColorDistribution(
            availablePx, lockedPx, boughtPx
        )
    }

    private fun Mat.convertIntoBufferImage() {
        //Encoding the image
        val matOfByte = MatOfByte()
        Imgcodecs.imencode(".png", this, matOfByte)
        //Storing the encoded Mat in a byte array
        val byteArray: ByteArray = matOfByte.toArray()
        //Preparing the Buffered Image
        val inputStream: InputStream = ByteArrayInputStream(byteArray)
        val bufferImage: BufferedImage = ImageIO.read(inputStream)
        println(bufferImage)
    }

    private fun File.convertIntoBufferImage() {
        val myInitialImage: BufferedImage = ImageIO.read(this)
        val newImage = BufferedImage(
            myInitialImage.width, myInitialImage.height, BufferedImage.TYPE_INT_ARGB
        )
        val g = newImage.createGraphics()
        g.drawImage(myInitialImage, 0, 0, null)
        g.dispose()
        println(newImage)
    }

    private fun Map<String, Int>.sortByValue(): Map<String, Int> {
        val list = mutableListOf<Int>()
        for (entry in this.entries) {
            list.add(entry.value)
        }
        list.sort()
        val sortedMap: LinkedHashMap<String, Int> = LinkedHashMap()
        for (num in list) {
            for (entry in this.entries) {
                if (entry.value == num) {
                    sortedMap[entry.key] = num
                }
            }
        }
        return sortedMap
    }
}