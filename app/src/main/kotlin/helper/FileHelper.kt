package helper

import Constants
import java.awt.Rectangle
import java.awt.Robot
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO
import javax.swing.JFileChooser


fun Robot.takeScreenShot(
    x: Int,
    y: Int,
    width: Int,
    height: Int,
): BufferedImage {
    val rect = Rectangle(x, y, width, height)
    return this.createScreenCapture(rect)
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

fun BufferedImage.save(fileName: String) {
    try {
        val docsDirectory = JFileChooser().fileSystemView.defaultDirectory.toString()
        val fileSeparator = System.getProperty(Constants.FILE_SEPARATOR_PROPERTY)
        val absoluteFilePath = docsDirectory + fileSeparator + fileName
        val file = File(absoluteFilePath)
        ImageIO.write(this, "png", file)
    } catch (e: IOException) {
        // handle exception
    }
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