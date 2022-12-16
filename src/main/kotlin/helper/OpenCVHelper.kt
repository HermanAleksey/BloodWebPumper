package helper

import org.opencv.core.Mat
import org.opencv.core.MatOfByte
import org.opencv.imgcodecs.Imgcodecs
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.File
import java.io.InputStream
import java.util.LinkedHashMap
import javax.imageio.ImageIO

fun Mat.convertIntoBufferImage(): BufferedImage {
    //Encoding the image
    val matOfByte = MatOfByte()
    Imgcodecs.imencode(".png", this, matOfByte)
    //Storing the encoded Mat in a byte array
    val byteArray: ByteArray = matOfByte.toArray()
    //Preparing the Buffered Image
    val inputStream: InputStream = ByteArrayInputStream(byteArray)
    return ImageIO.read(inputStream)
}

fun File.convertIntoBufferImage(): BufferedImage {
    val myInitialImage: BufferedImage = ImageIO.read(this)
    val newImage = BufferedImage(
        myInitialImage.width, myInitialImage.height, BufferedImage.TYPE_INT_ARGB
    )
    val g = newImage.createGraphics()
    g.drawImage(myInitialImage, 0, 0, null)
    g.dispose()
    return newImage
}

fun Map<String, Int>.sortByValue(): Map<String, Int> {
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