package execution_mode

import blood_web.InfoNode
import blood_web.Node
import detector.Detector
import helper.sendLog
import org.jgrapht.Graph
import org.jgrapht.graph.DefaultEdge

class MostExpensiveExecutor(
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
            node.quality == InfoNode.Quality.IRIDESCENT &&
                    node.isAccessible()
        } ?: vertexSet.find { node ->
            node.quality == InfoNode.Quality.PURPLE &&
                    node.isAccessible()
        } ?: vertexSet.find { node ->
            node.quality == InfoNode.Quality.GREEN &&
                    node.isAccessible()
        } ?: vertexSet.find { node ->
            node.quality == InfoNode.Quality.YELLOW &&
                    node.isAccessible()
        } ?: vertexSet.find { node ->
            node.isAccessible()
        } ?: throw Exception("Can't find most expensive node")).apply {
            sendLog("Target Node: $this")
        }
    }
}