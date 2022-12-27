package helper

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

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