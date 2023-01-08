package blood_web

import Constants.NODE_SIZE_PX
import java.awt.Point

data class Node(
    val orderedNumber: OrderedNumber,
    val topCenterCoord: Point,

    var state: State? = null,
    var quality: Quality? = null,
) {
    val topLeftCoordinates: Point = Point(
        topCenterCoord.x - NODE_SIZE_PX / 2, topCenterCoord.y
    )
    val centerCoordinates: Point = Point(
        topCenterCoord.x, topCenterCoord.y + NODE_SIZE_PX / 2
    )

    override fun toString(): String {
        return "Node($orderedNumber), $state, $quality"
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Node) return false
        var isEqual = false
        if (this.orderedNumber != other.orderedNumber)
            isEqual = false
        isEqual = (this.topCenterCoord == other.topCenterCoord)
//        println("$this eq $other: $isEqual")
        return isEqual
    }

    override fun hashCode(): Int {
        return this.orderedNumber.hashCode() + this.topCenterCoord.hashCode()
    }

    fun toLogString() = "Node(orderedNumber:$orderedNumber, state=$state, quality=$quality)"

    data class OrderedNumber(
        val circle: BloodWeb.BloodWebCircle,
        val position: Int,
    ){
        override fun toString(): String {
            return "$circle: $position"
        }
    }

    enum class State {
        AVAILABLE,
        UNAVAILABLE,
        LOCKED,
        BOUGHT,
        EMPTY;

        //return true if node is still can be picked
        fun isOpen() = this == AVAILABLE || this == UNAVAILABLE

        //return true if node already can't be picked
        fun isClosed() = this == LOCKED || this == BOUGHT || this == EMPTY
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