package detectors

import Constants.BLOOD_WEB_CENTER_X
import blood_web.ColorRanges
import blood_web.Node
import blood_web.isInRange
import java.awt.Color
import java.awt.Point
import java.awt.image.BufferedImage

class SimpleDetector : Detector {

    override fun analyzeSingleNode(nodePoint: Point, bufferedImage: BufferedImage): Node.State {
        val colorOfPoint = getPointColor(bufferedImage, nodePoint)
        return checkColorState(colorOfPoint)
    }

    override fun analyzeCenterOfBloodWeb(bufferedImage: BufferedImage): Boolean {
        Constants.BLOOD_WEB_CENTER_Y_RANGE.forEach {
            val centerColor = checkPointColor(
                bufferedImage = bufferedImage,
                point = Point(BLOOD_WEB_CENTER_X, it)
            )
            println(centerColor)
            if (centerColor.isInRange(ColorRanges.WHITE)) return true
        }
        return false
    }

    //returns RGB of point
    private fun checkPointColor(
        bufferedImage: BufferedImage, point: Point
    ): Color {
        val intColor = bufferedImage.getRGB(point.x, point.y)
        return Color(intColor)
    }

    private fun getPointColor(
        bufferedImage: BufferedImage, point: Point
    ): Color {
        val intColor = bufferedImage.getRGB(point.x, point.y)
        return Color(intColor)
    }

    private fun checkColorState(
        color: Color
    ): Node.State {
        val isAvailable = color.isInRange(ColorRanges.AVAILABLE_PERK_BORDER)

        return if (isAvailable) Node.State.AVAILABLE
        else Node.State.EMPTY
    }
}