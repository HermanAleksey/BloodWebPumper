package model

import java.awt.Point


class InfoNode(
    orderedNumber: OrderedNumber,
    topCenterCoord: Point,
    var state: State,
    var quality: Quality,
) : Node(
    orderedNumber = orderedNumber,
    topCenterCoord = topCenterCoord
) {

    override fun toString(): String {
        return "Node(${orderedNumber}, $state, $quality)"
    }

    //return true if node is still can be picked
    fun isAccessible() = this.state == State.AVAILABLE || this.state == State.UNAVAILABLE

    //return true if node already can't be picked
    fun isInaccessible() =
        this.state == State.LOCKED || this.state == State.BOUGHT || this.state == State.EMPTY

    enum class State {
        AVAILABLE,
        UNAVAILABLE,
        LOCKED,
        BOUGHT,
        EMPTY;
    }

    enum class Quality {
        IRIDESCENT,
        PURPLE,
        GREEN,
        YELLOW,
        BROWN, //also event ones
    }
}