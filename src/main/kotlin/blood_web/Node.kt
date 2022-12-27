package blood_web

import Constants.NODE_SIZE_PX
import java.awt.Point

data class Node(
    val orderedNumber: OrderedNumber,
    val topCenterCoord: Point,

    var state: State? = null,
    var quality: Quality? = null,
) {
    val topLeftCoords: Point = Point(
        topCenterCoord.x - NODE_SIZE_PX / 2, topCenterCoord.y
    )
    val centerCoords: Point = Point(
        topCenterCoord.x, topCenterCoord.y + NODE_SIZE_PX / 2
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

fun Pair<Node.OrderedNumber, Point>.parseIntoNode()= Node(
    orderedNumber = this.first,
    topCenterCoord = this.second,
    state = null,
    quality = null
)