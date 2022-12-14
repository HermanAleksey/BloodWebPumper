package launch_mode

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

        val testFilePath = "img_2.png"
        val file = File(testFilePath)

        //my initial image preview
        file.convertIntoBufferImage()

        // parsing file image into OpenCV Mat
        val openCVImage = imread(testFilePath)
        openCVImage.convertIntoBufferImage()


        var color_range1_counter = 0
        var color_range2_counter = 0
        println(openCVImage.rows())
        println(openCVImage.cols())
        val colorToAmountMap = hashMapOf<String, Int>()
        for (i in 0 until openCVImage.rows()) {
            for (j in 0 until openCVImage.cols()) {
                val hsv: DoubleArray = openCVImage.get(i, j)
                val hsvName = "${hsv[0]} ${hsv[1]} ${hsv[2]}"
                val repeatedTimes = colorToAmountMap[hsvName] ?: 0
                colorToAmountMap[hsvName] = repeatedTimes + 1

                if (hsv[0] > 0 && hsv[0] < 255 && hsv[1] > 0 && hsv[1] < 255 && hsv[2] > 0 && hsv[2] < 255) color_range1_counter++
                if (hsv[0] > 10 && hsv[0] < 20 && hsv[1] > 100 && hsv[1] < 255 && hsv[2] > 20 && hsv[2] < 200) color_range2_counter++
            }
        }
        println("color_range1_counter:$color_range1_counter     color_range2_counter:$color_range2_counter")
        println(colorToAmountMap)

        println(colorToAmountMap.sortByValue())

//
//        /**
//         * The Mat class of OpenCV library is used to store the values of an image.
//         * It represents an n-dimensional array and is used to store image data of
//         * grayscale or color images, voxel volumes, vector fields, point clouds,
//         * tensors, histograms, etc. This class comprises of two data parts: the header and a pointer.
//         */
//        val hsvImage = Mat()
//        val color_range1 = Mat()
//        val color_range2 = Mat()
//
//        /**
//         * Scalar used to set BGR values - Blue = a, Green = b and Red = c
//         */
//        Imgproc.cvtColor(openCVImage, hsvImage, Imgproc.COLOR_RGB2HSV)
//        Core.inRange(hsvImage, Scalar(25.0, 52.0, 72.0), Scalar(102.0, 255.0, 255.0), color_range1) // Green
//        Core.inRange(hsvImage, Scalar(10.0, 100.0, 20.0), Scalar(20.0, 255.0, 200.0), color_range2) // Brown
//
//        // I have the Green and Brown mask in color_range1 and color_range2 respectively.
//        // Now what
//        color_range1.release()
//        color_range2.release()
//        hsvImage.release()
//        openCVImage.release()
//
//        var color_range1_counter = 0;
//        var color_range2_counter = 0;
//        var iterationsAmount = 0
//
//        for (i in 1..hsvImage.rows()) {
//            for (j in 1..hsvImage.cols()) {
//                iterationsAmount++
//                val hsv = hsvImage.get(i, j)
//                if (hsv[0] > 25 && hsv[0] < 102 && hsv[1] > 52 && hsv[1] < 255 && hsv[2] > 72 && hsv[2] < 255)
//                    color_range1_counter++
//
//                if (hsv[0] > 10 && hsv[0] < 20 && hsv[1] > 100 && hsv[1] < 255 && hsv[2] > 20 && hsv[2] < 200)
//                    color_range2_counter++
//
//            }
//        }
////        val percentage = color_range1_counter /(color_range1_counter + color_range2_counter)
//        println("color_range1_counter:$color_range1_counter, color_range2_counter:$color_range2_counter")
//        println("iterationsAmount:$iterationsAmount")
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

    private fun Map<String, Int>.sortByValue(): Map<String,Int>{
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