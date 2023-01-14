package model

import org.jgrapht.Graph
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.SimpleGraph
import java.awt.Point

//positions of top-center point of each possible perk
class Presets {

    fun getAllNodes() = innerNodes + middleNodes + outerNodes

    val innerNodes = arrayOf(
        Node(Node.OrderedNumber(BloodWeb.Circle.INNER, 1), Point(677, 420)),//proved
        Node(Node.OrderedNumber(BloodWeb.Circle.INNER, 2), Point(783, 481)),//proved
        Node(Node.OrderedNumber(BloodWeb.Circle.INNER, 3), Point(783, 604)),//proved
        Node(Node.OrderedNumber(BloodWeb.Circle.INNER, 4), Point(678, 666)),//proved
        Node(Node.OrderedNumber(BloodWeb.Circle.INNER, 5), Point(573, 604)),//proved
        Node(Node.OrderedNumber(BloodWeb.Circle.INNER, 6), Point(573, 480)),//proved
    )

    val middleNodes = arrayOf(
        Node(Node.OrderedNumber(BloodWeb.Circle.MIDDLE, 1), Point(741, 309)),//proved
        Node(Node.OrderedNumber(BloodWeb.Circle.MIDDLE, 2), Point(856, 371)),//proved
        Node(Node.OrderedNumber(BloodWeb.Circle.MIDDLE, 3), Point(919, 482)),//
        Node(Node.OrderedNumber(BloodWeb.Circle.MIDDLE, 4), Point(919, 604)),//proved
        Node(Node.OrderedNumber(BloodWeb.Circle.MIDDLE, 5), Point(856, 715)),//proved
        Node(Node.OrderedNumber(BloodWeb.Circle.MIDDLE, 6), Point(740, 777)),//proved
        Node(Node.OrderedNumber(BloodWeb.Circle.MIDDLE, 7), Point(617, 777)),//proved
        Node(Node.OrderedNumber(BloodWeb.Circle.MIDDLE, 8), Point(500, 715)),//proved
        Node(Node.OrderedNumber(BloodWeb.Circle.MIDDLE, 9), Point(437, 604)),//proved
        Node(Node.OrderedNumber(BloodWeb.Circle.MIDDLE, 10), Point(436, 482)),//proved
        Node(Node.OrderedNumber(BloodWeb.Circle.MIDDLE, 11), Point(500, 369)),//
        Node(Node.OrderedNumber(BloodWeb.Circle.MIDDLE, 12), Point(616, 309)),//proved
    )

    val outerNodes = arrayOf(
        Node(Node.OrderedNumber(BloodWeb.Circle.OUTER, 1), Point(678, 190)),//
        Node(Node.OrderedNumber(BloodWeb.Circle.OUTER, 2), Point(858, 238)),//proved
        Node(Node.OrderedNumber(BloodWeb.Circle.OUTER, 3), Point(989, 369)),//
        Node(Node.OrderedNumber(BloodWeb.Circle.OUTER, 4), Point(1038, 543)),//proved
        Node(Node.OrderedNumber(BloodWeb.Circle.OUTER, 5), Point(989, 716)),//proved
        Node(Node.OrderedNumber(BloodWeb.Circle.OUTER, 6), Point(858, 848)),//proved
        Node(Node.OrderedNumber(BloodWeb.Circle.OUTER, 7), Point(678, 896)),//proved
        Node(Node.OrderedNumber(BloodWeb.Circle.OUTER, 8), Point(498, 849)),//proved
        Node(Node.OrderedNumber(BloodWeb.Circle.OUTER, 9), Point(367, 717)),//proved
        Node(Node.OrderedNumber(BloodWeb.Circle.OUTER, 10), Point(318, 543)),//proved
        Node(Node.OrderedNumber(BloodWeb.Circle.OUTER, 11), Point(367, 369)),//proved
        Node(Node.OrderedNumber(BloodWeb.Circle.OUTER, 12), Point(498, 238)),//
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