package execution_mode

import blood_web.Node
import detector.Detector
import helper.sendLog
import org.jgrapht.Graph
import org.jgrapht.graph.DefaultEdge

class NewNameExecutor(
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
//        val vertexSet = graph.vertexSet()
//        return (vertexSet.find { node ->
//            node.quality == Node.Quality.IRIDESCENT
//        } ?: vertexSet.find { node ->
//            node.quality == Node.Quality.PURPLE
//        } ?: vertexSet.find { node ->
//            node.quality == Node.Quality.GREEN
//        } ?: vertexSet.find { node ->
//            node.quality == Node.Quality.YELLOW
//        } ?: vertexSet.first()).apply {
//            sendLog("Target Node: $this")
//        }
    }
}