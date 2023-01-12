package execution_mode

import detector.Detector
import helper.sendLog
import model.BloodWeb
import model.InfoNode
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
    override suspend fun getTargetNode(graph: Graph<InfoNode, DefaultEdge>): InfoNode {
        val vertexSet = graph.vertexSet()
        return (vertexSet.find { node ->
            node.orderedNumber.circle == BloodWeb.BloodWebCircle.OUTER &&
                    node.quality != InfoNode.Quality.BROWN &&
                    node.isAccessible()
        } ?: vertexSet.find { node ->
            node.orderedNumber.circle == BloodWeb.BloodWebCircle.MIDDLE &&
                    node.quality != InfoNode.Quality.BROWN &&
                    node.isAccessible()
        } ?: vertexSet.find { node ->
            node.isAccessible()
        } ?: throw Exception("Can't find furthest node")).apply {
            sendLog("Target Node: $this")
        }
    }
}