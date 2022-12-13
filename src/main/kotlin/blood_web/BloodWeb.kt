import Constants.BLOOD_WEB_CENTER_X
import blood_web.ColorRanges
import blood_web.Node
import blood_web.Presets
import blood_web.isInRange
import java.awt.Color
import java.awt.Point
import java.awt.image.BufferedImage

class BloodWeb {
    enum class BloodWebCircle {
        INNER, SECONDARY, OUTER
    }

    //goes through all positions of item in [circle] of bloodWeb
    //If perk available - add it into array
    //return array of available perks
    fun checkAvailablePerks(
        circle: BloodWebCircle, bufferedImage: BufferedImage
    ): List<Node> {
        val presets = when (circle) {
            BloodWebCircle.INNER -> Presets().innerPoints
            BloodWebCircle.SECONDARY -> Presets().secondaryPoints
            BloodWebCircle.OUTER -> Presets().outerPoints
        }
        val availableNodes = mutableListOf<Node>()

        presets.forEachIndexed { position, point ->
            val colorOfPoint = checkPointColor(bufferedImage, point)
            val perkState = checkColorState(colorOfPoint)

            if (perkState == Node.State.AVAILABLE)
                availableNodes.add(
                    Node(
                        position = position,
                        topCenterCoord = point
                    )
                )
        }
        return availableNodes
    }

    //returns RGB of point
    private fun checkPointColor(
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

    /**
     * Checks image
     * White color in the center of blood web only presented in prestige upgrades
     * If center line has White colors - return true
     * If not - return false
     * */
    fun checkPrestigeUpgrade(bufferedImage: BufferedImage): Boolean {
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
}
