package blood_web

import androidx.compose.ui.text.font.createFontFamilyResolver
import org.jgrapht.Graph
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.SimpleGraph
import org.jgrapht.traverse.DepthFirstIterator
import java.awt.Point

//positions of top-center point of each possible perk
class Presets {

    val innerPoints = arrayOf(
        Node.OrderedNumber(BloodWeb.BloodWebCircle.INNER, 1) to Point(677, 422),//1
        Node.OrderedNumber(BloodWeb.BloodWebCircle.INNER, 2) to Point(783, 483),//2
        Node.OrderedNumber(BloodWeb.BloodWebCircle.INNER, 3) to Point(783, 607),//3
        Node.OrderedNumber(BloodWeb.BloodWebCircle.INNER, 4) to Point(678, 667),//4
        Node.OrderedNumber(BloodWeb.BloodWebCircle.INNER, 5) to Point(573, 607),//5
        Node.OrderedNumber(BloodWeb.BloodWebCircle.INNER, 6) to Point(573, 482),//6
    )

    val secondaryPoints = arrayOf(
        Node.OrderedNumber(BloodWeb.BloodWebCircle.SECONDARY, 1) to Point(742, 311),//1
        Node.OrderedNumber(BloodWeb.BloodWebCircle.SECONDARY, 2) to Point(857, 374),//2
        Node.OrderedNumber(BloodWeb.BloodWebCircle.SECONDARY, 3) to Point(919, 485),//3
        Node.OrderedNumber(BloodWeb.BloodWebCircle.SECONDARY, 4) to Point(918, 606),//4
        Node.OrderedNumber(BloodWeb.BloodWebCircle.SECONDARY, 5) to Point(857, 717),//5
        Node.OrderedNumber(BloodWeb.BloodWebCircle.SECONDARY, 6) to Point(740, 779),//6
        Node.OrderedNumber(BloodWeb.BloodWebCircle.SECONDARY, 7) to Point(616, 779),//7
        Node.OrderedNumber(BloodWeb.BloodWebCircle.SECONDARY, 8) to Point(500, 717),//8
        Node.OrderedNumber(BloodWeb.BloodWebCircle.SECONDARY, 9) to Point(436, 606),//9
        Node.OrderedNumber(BloodWeb.BloodWebCircle.SECONDARY, 10) to Point(437, 485),//10
        Node.OrderedNumber(BloodWeb.BloodWebCircle.SECONDARY, 11) to Point(500, 373),//11
        Node.OrderedNumber(BloodWeb.BloodWebCircle.SECONDARY, 12) to Point(614, 311),//12
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
    val graph: Graph<Node, DefaultEdge> = SimpleGraph<Node, DefaultEdge>(DefaultEdge::class.java)
    Presets().innerPoints.forEach {point ->
        val node = point.parseIntoNode()
        graph.addVertex(node)
    }
    Presets().secondaryPoints.forEach {point ->
        val node = point.parseIntoNode()
        graph.addVertex(node)
    }
    Presets().outerPoints.forEach {point ->
        val node = point.parseIntoNode()
        graph.addVertex(node)
    }

    val vertexesSet = graph.vertexSet()

    Presets().innerPoints.forEach { point ->
        val node1 = point.parseIntoNode()

        for (i in 0..3) {
            var index = (node1.orderedNumber.position * 2 + 12 - 3 + i - 1) % 12 //
            var preset = Presets().secondaryPoints[index]
            graph.addEdge(
                vertexesSet.find { it == node1 },
                vertexesSet.find { node2 -> node2 == Node(preset.first, preset.second) })
        }
    }



    Presets().secondaryPoints.forEach {point ->
        val node1 = point.parseIntoNode()

        for (i in 0..1) {
            var index = (node1.orderedNumber.position - 1 + 12 + i) % 12 //
            var preset = Presets().outerPoints[index]
            graph.addEdge(vertexesSet.find { it == node1 }, vertexesSet.find { node2 -> node2 == Node(preset.first, preset.second) })
        }

    }

    return graph;
}