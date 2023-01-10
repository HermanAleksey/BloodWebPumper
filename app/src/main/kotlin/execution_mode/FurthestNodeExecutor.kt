package execution_mode

import blood_web.BloodWeb
import blood_web.Node
import detector.Detector
import helper.sendLog
import org.jgrapht.Graph
import org.jgrapht.graph.DefaultEdge

class FurthestNodeExecutor(
    delayNewLevelAnimation: Long,
    perkSelectionDuration: Long,
    movementDuration: Long,
    prestigeLevelUpDuration: Long,
    detector: Detector,
    levels: Int
) : GraphExecutionMode(
    delayNewLevelAnimation = delayNewLevelAnimation,
    perkSelectionDuration = perkSelectionDuration,
    movementDuration = movementDuration,
    prestigeLevelUpDuration = prestigeLevelUpDuration,
    detector = detector,
    levels = levels
) {

    override suspend fun getTargetNodeFromGraph(graph: Graph<Node, DefaultEdge>): Node {
        val vertexSet = graph.vertexSet()
        return (vertexSet.find { node ->
            node.orderedNumber.circle == BloodWeb.BloodWebCircle.OUTER &&
                    node.isAccessible()
        } ?: vertexSet.find { node ->
            node.orderedNumber.circle == BloodWeb.BloodWebCircle.MIDDLE &&
                    node.isAccessible()
        } ?: vertexSet.find { node ->
            node.isAccessible()
        } ?: throw Exception("Can't find furthest node")).apply {
            sendLog("Target Node: $this")
        }
    }
}