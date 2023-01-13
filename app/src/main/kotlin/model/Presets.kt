package model

import org.jgrapht.Graph
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.SimpleGraph
import java.awt.Point

//positions of top-center point of each possible perk
class Presets {

    fun getAllNodes() = innerNodes + middleNodes + outerNodes

    val innerNodes = arrayOf(
        Node(Node.OrderedNumber(BloodWeb.Circle.INNER, 1), Point(677, 422)),
        Node(Node.OrderedNumber(BloodWeb.Circle.INNER, 2), Point(783, 483)),
        Node(Node.OrderedNumber(BloodWeb.Circle.INNER, 3), Point(783, 607)),
        Node(Node.OrderedNumber(BloodWeb.Circle.INNER, 4), Point(678, 667)),
        Node(Node.OrderedNumber(BloodWeb.Circle.INNER, 5), Point(573, 607)),
        Node(Node.OrderedNumber(BloodWeb.Circle.INNER, 6), Point(573, 482)),
    )

    val middleNodes = arrayOf(
        Node(Node.OrderedNumber(BloodWeb.Circle.MIDDLE, 1), Point(742, 311)),
        Node(Node.OrderedNumber(BloodWeb.Circle.MIDDLE, 2), Point(857, 374)),
        Node(Node.OrderedNumber(BloodWeb.Circle.MIDDLE, 3), Point(919, 485)),
        Node(Node.OrderedNumber(BloodWeb.Circle.MIDDLE, 4), Point(918, 606)),
        Node(Node.OrderedNumber(BloodWeb.Circle.MIDDLE, 5), Point(857, 717)),
        Node(Node.OrderedNumber(BloodWeb.Circle.MIDDLE, 6), Point(740, 779)),
        Node(Node.OrderedNumber(BloodWeb.Circle.MIDDLE, 7), Point(616, 779)),
        Node(Node.OrderedNumber(BloodWeb.Circle.MIDDLE, 8), Point(500, 717)),
        Node(Node.OrderedNumber(BloodWeb.Circle.MIDDLE, 9), Point(436, 606)),
        Node(Node.OrderedNumber(BloodWeb.Circle.MIDDLE, 10), Point(437, 485)),
        Node(Node.OrderedNumber(BloodWeb.Circle.MIDDLE, 11), Point(500, 373)),
        Node(Node.OrderedNumber(BloodWeb.Circle.MIDDLE, 12), Point(614, 311)),
    )

    val outerNodes = arrayOf(
        Node(Node.OrderedNumber(BloodWeb.Circle.OUTER, 1), Point(677, 192)),
        Node(Node.OrderedNumber(BloodWeb.Circle.OUTER, 2), Point(859, 239)),
        Node(Node.OrderedNumber(BloodWeb.Circle.OUTER, 3), Point(990, 371)),
        Node(Node.OrderedNumber(BloodWeb.Circle.OUTER, 4), Point(1040, 546)),
        Node(Node.OrderedNumber(BloodWeb.Circle.OUTER, 5), Point(989, 719)),
        Node(Node.OrderedNumber(BloodWeb.Circle.OUTER, 6), Point(856, 850)),
        Node(Node.OrderedNumber(BloodWeb.Circle.OUTER, 7), Point(676, 898)),
        Node(Node.OrderedNumber(BloodWeb.Circle.OUTER, 8), Point(497, 852)),
        Node(Node.OrderedNumber(BloodWeb.Circle.OUTER, 9), Point(366, 719)),
        Node(Node.OrderedNumber(BloodWeb.Circle.OUTER, 10), Point(317, 545)),
        Node(Node.OrderedNumber(BloodWeb.Circle.OUTER, 11), Point(367, 371)),
        Node(Node.OrderedNumber(BloodWeb.Circle.OUTER, 12), Point(497, 240)),
    )
}

fun List<InfoNode>.createGraph(): Graph<InfoNode, DefaultEdge> {
    val graph: Graph<InfoNode, DefaultEdge> = SimpleGraph(DefaultEdge::class.java)
    this.forEach { infoNode ->
        graph.addVertex(infoNode)
    }

    val verticesSet = graph.vertexSet()
    val innerNodes = this.filter {
        it.orderedNumber.circle == BloodWeb.Circle.INNER
    }
    val middleNodes = this.filter {
        it.orderedNumber.circle == BloodWeb.Circle.MIDDLE
    }
    val outerNodes = this.filter {
        it.orderedNumber.circle == BloodWeb.Circle.OUTER
    }

    innerNodes.forEach { innerNode ->
        for (i in 0..3) {
            val index = ((innerNode.orderedNumber.position * 2 + 12 - 3 + i) % 12).let {
                if (it == 0) 12 else it
            }
            val middleNode = middleNodes.find {
                it.orderedNumber.position == index
            }

            if (verticesSet.contains(middleNode)) {
                graph.addEdge(
                    innerNode,
                    verticesSet.find { it == middleNode }
                )
            }
        }
    }

    middleNodes.forEach { middleNode ->
        for (i in 0..1) {
            val index = ((middleNode.orderedNumber.position + 12 + i) % 12).let {
                if (it == 0) 12 else it
            }
            val outerNode = outerNodes.find {
                it.orderedNumber.position == index
            }

            if (verticesSet.contains(outerNode)) {
                graph.addEdge(
                    middleNode,
                    verticesSet.find { it == outerNode }
                )
            }
        }
    }

    return graph
}