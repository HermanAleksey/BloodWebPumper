package blood_web

import java.awt.Point

data class Node(
    val orderedNumber: OrderedNumber,
    val size: Int = 76,
    val topCenterCoord: Point,
) {
    val topLeftCoords: Point = Point(
        topCenterCoord.x - size / 2, topCenterCoord.y
    )
    val centerCoords: Point = Point(
        topCenterCoord.x, topCenterCoord.y + size / 2
    )

    override fun toString(): String {
        return "Node(orderedNumber:$orderedNumber, topCenter=Point(${topCenterCoord.x},${topCenterCoord.y}))"
    }

    data class OrderedNumber(
        val circle: BloodWeb.BloodWebCircle,
        val position: Int,
    )

    enum class State {
        AVAILABLE,
        UNAVAILABLE,
        LOCKED,
        BOUGHT,
        EMPTY
    }

    enum class Quality {
        IRIDESCENT,
        PURPLE,
        GREEN,
        YELLOW,
        BROWN, //also event ones
    }
}