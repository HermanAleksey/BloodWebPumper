package blood_web

import Constants.NODE_SIZE_PX
import java.awt.Point

open class Node(
    val orderedNumber: OrderedNumber,
    val topCenterCoord: Point,
) {
    val topLeftCoordinates: Point = Point(
        topCenterCoord.x - NODE_SIZE_PX / 2, topCenterCoord.y
    )
    val centerCoordinates: Point = Point(
        topCenterCoord.x, topCenterCoord.y + NODE_SIZE_PX / 2
    )

    override fun equals(other: Any?): Boolean {
        if (other !is Node) return false
        if (this.orderedNumber != other.orderedNumber)
            return false
        return this.topCenterCoord == other.topCenterCoord
    }

    override fun hashCode(): Int {
        return this.orderedNumber.hashCode() + this.topCenterCoord.hashCode()
    }

    data class OrderedNumber(
        val circle: BloodWeb.BloodWebCircle,
        val position: Int,
    ) {
        override fun toString(): String {
            return "$circle: $position"
        }
    }

    fun toInfoNode(
        state: InfoNode.State,
        quality: InfoNode.Quality,
    ) = InfoNode(
        this.orderedNumber,
        this.topCenterCoord,
        state,
        quality
    )
}