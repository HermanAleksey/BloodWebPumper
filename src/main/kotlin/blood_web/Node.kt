package blood_web

import java.awt.Point

data class Node(
    val position: Int = -1,
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
        return "Node(position:$position, topCenter=Point(${topCenterCoord.x},${topCenterCoord.y}))"
    }

    enum class State {
        AVAILABLE,
        INACTIVE,
        EMPTY
    }
}