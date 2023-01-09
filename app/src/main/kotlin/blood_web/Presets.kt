package blood_web

import org.jgrapht.Graph
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.SimpleGraph
import java.awt.Point

//positions of top-center point of each possible perk
class Presets {

    fun getAllPoints() = innerPoints + middlePoints + outerPoints

    val innerPoints = arrayOf(
        Node.OrderedNumber(BloodWeb.BloodWebCircle.INNER, 1) to Point(677, 422),//1
        Node.OrderedNumber(BloodWeb.BloodWebCircle.INNER, 2) to Point(783, 483),//2
        Node.OrderedNumber(BloodWeb.BloodWebCircle.INNER, 3) to Point(783, 607),//3
        Node.OrderedNumber(BloodWeb.BloodWebCircle.INNER, 4) to Point(678, 667),//4
        Node.OrderedNumber(BloodWeb.BloodWebCircle.INNER, 5) to Point(573, 607),//5
        Node.OrderedNumber(BloodWeb.BloodWebCircle.INNER, 6) to Point(573, 482),//6
    )

    val middlePoints = arrayOf(
        Node.OrderedNumber(BloodWeb.BloodWebCircle.MIDDLE, 1) to Point(742, 311),//1
        Node.OrderedNumber(BloodWeb.BloodWebCircle.MIDDLE, 2) to Point(857, 374),//2
        Node.OrderedNumber(BloodWeb.BloodWebCircle.MIDDLE, 3) to Point(919, 485),//3
        Node.OrderedNumber(BloodWeb.BloodWebCircle.MIDDLE, 4) to Point(918, 606),//4
        Node.OrderedNumber(BloodWeb.BloodWebCircle.MIDDLE, 5) to Point(857, 717),//5
        Node.OrderedNumber(BloodWeb.BloodWebCircle.MIDDLE, 6) to Point(740, 779),//6
        Node.OrderedNumber(BloodWeb.BloodWebCircle.MIDDLE, 7) to Point(616, 779),//7
        Node.OrderedNumber(BloodWeb.BloodWebCircle.MIDDLE, 8) to Point(500, 717),//8
        Node.OrderedNumber(BloodWeb.BloodWebCircle.MIDDLE, 9) to Point(436, 606),//9
        Node.OrderedNumber(BloodWeb.BloodWebCircle.MIDDLE, 10) to Point(437, 485),//10
        Node.OrderedNumber(BloodWeb.BloodWebCircle.MIDDLE, 11) to Point(500, 373),//11
        Node.OrderedNumber(BloodWeb.BloodWebCircle.MIDDLE, 12) to Point(614, 311),//12
    )

    val outerPoints = arrayOf(
        Node.OrderedNumber(BloodWeb.BloodWebCircle.OUTER, 1) to Point(677, 192),//1
        Node.OrderedNumber(BloodWeb.BloodWebCircle.OUTER, 2) to Point(859, 239),//2
        Node.OrderedNumber(BloodWeb.BloodWebCircle.OUTER, 3) to Point(990, 371),//3
        Node.OrderedNumber(BloodWeb.BloodWebCircle.OUTER, 4) to Point(1040, 546),//4
        Node.OrderedNumber(BloodWeb.BloodWebCircle.OUTER, 5) to Point(989, 719),//5
        Node.OrderedNumber(BloodWeb.BloodWebCircle.OUTER, 6) to Point(856, 850),//6
        Node.OrderedNumber(BloodWeb.BloodWebCircle.OUTER, 7) to Point(676, 898),//7
        Node.OrderedNumber(BloodWeb.BloodWebCircle.OUTER, 8) to Point(497, 852),//8
        Node.OrderedNumber(BloodWeb.BloodWebCircle.OUTER, 9) to Point(366, 719),//9
        Node.OrderedNumber(BloodWeb.BloodWebCircle.OUTER, 10) to Point(317, 545),//10
        Node.OrderedNumber(BloodWeb.BloodWebCircle.OUTER, 11) to Point(367, 371),//11
        Node.OrderedNumber(BloodWeb.BloodWebCircle.OUTER, 12) to Point(497, 240),//12
    )
}

fun createFullGraph(): Graph<Node, DefaultEdge> {
    val graph: Graph<Node, DefaultEdge> = SimpleGraph(DefaultEdge::class.java)
    Presets().getAllPoints().forEach { point ->
        val node = point.parseIntoNode()
        graph.addVertex(node)
    }

    val verticesSet = graph.vertexSet()

    Presets().innerPoints.forEach { point ->
        val node1 = point.parseIntoNode()

        for (i in 0..3) {
            val index = (node1.orderedNumber.position * 2 + 12 - 3 + i - 1) % 12 //
            val preset = Presets().middlePoints[index]
            graph.addEdge(
                verticesSet.find { it == node1 },
                verticesSet.find { node2 ->
                    node2 == Node(preset.first, preset.second)
                }
            )
        }
    }

    Presets().middlePoints.forEach { point ->
        val node1 = point.parseIntoNode()

        for (i in 0..1) {
            val index = (node1.orderedNumber.position - 1 + 12 + i) % 12
            val preset = Presets().outerPoints[index]
            graph.addEdge(
                verticesSet.find { it == node1 },
                verticesSet.find { node2 ->
                    node2 == Node(preset.first, preset.second)
                }
            )
        }

    }

    return graph
}