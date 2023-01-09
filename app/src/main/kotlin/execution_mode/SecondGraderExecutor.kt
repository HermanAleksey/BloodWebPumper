package execution_mode

import blood_web.BloodWebPageState
import blood_web.Node
import blood_web.createFullGraph
import detector.Detector
import helper.sendLog
import kotlinx.coroutines.delay
import org.jgrapht.Graph
import org.jgrapht.graph.DefaultEdge

class SecondGraderExecutor(
    delayNewLevelAnimation: Long,
    perkSelectionDuration: Long,
    movementDuration: Long,
    prestigeLevelUpDuration: Long,
    detector: Detector,
    private val levels: Int
) : ExecutionMode(
    delayNewLevelAnimation = delayNewLevelAnimation,
    perkSelectionDuration = perkSelectionDuration,
    movementDuration = movementDuration,
    prestigeLevelUpDuration = prestigeLevelUpDuration,
    detector = detector
) {

    private var graph = createFullGraph()

    override suspend fun pumpBloodWeb() {
        sendLog("Вызов pumpBloodWeb")
        for (currentLevel in 1..levels) {
            if (isExecutionWasStopped()) return

            sendLog("Pumping level #$currentLevel")
            pumpOneBloodWebLevel()
            delay(delayNewLevelAnimation)
            clickHelper.moveOutCursor()
        }
        sendLog("SecondGraderExecutor completed")
    }

    private suspend fun pumpOneBloodWebLevel() {
        sendLog("Вызов pumpOneBloodWebLevel")
        val bloodWebScreenShot = takeScreenShot()
        detector.analyzeBloodWebPageState(bloodWebScreenShot).let { pageState ->
            when (pageState) {
                BloodWebPageState.NOTIFICATION -> {
                    sendLog("Is skipable notification level")
                    clickHelper.skipNotification()
                }
                BloodWebPageState.PRESTIGE -> {
                    sendLog("Is prestige level")
                    clickHelper.upgradePrestigeLevel()
                }
                BloodWebPageState.LEVEL -> {
                    graph = createFullGraph()
                    updateGraph()
                    levelEverything()
                }
            }
        }
    }

    private suspend fun levelEverything() {
        sendLog("Вызов levelEverything")

        val vertices = graph.vertexSet()
        var isLevelFinished = false

        while (!isLevelFinished) {
            val targetNode = graph.getMostExpensiveNode()
            levelUpBranchToTargetNode(targetNode)
            updateGraph()
            vertices.any {
                it.isAccessible()
            }.let { isLevelFinished = !it }
        }
    }

    private suspend fun levelUpBranchToTargetNode(node: Node) {
        sendLog("Вызов levelUpNode")
        node.state?.let {
            if (isExecutionWasStopped()) return

            if (it == Node.State.AVAILABLE) {
                clickHelper.performClickOnNode(node)
                updateGraph()
            } else {
                changeTargetNode(node)?.let { newTargetNode ->
                    levelUpBranchToTargetNode(newTargetNode)
                }
            }
        }
    }

    private suspend fun updateGraph() {
        val bloodWebScreenShot = takeScreenShot()
        sendLog("Вызов updateGraph")

        with(graph.vertexSet()) {
            forEach { node ->
                detector.processNodeStateQuality(node, bloodWebScreenShot)
            }

            filter { node ->
                //true if node.state is closed or null
                node.isInaccessible()
            }.forEach { unavailableVertexes ->
                graph.removeVertex(unavailableVertexes)
//                sendLog("$unavailableVertexes is removed $isEqual")
//                sendLog("and graph contain vertex: ${graph.containsVertex(unavailableVertexes)}")
            }
        }
    }

    private suspend fun changeTargetNode(oldTargetNode: Node): Node? {
        sendLog("Вызов changeTargetNode")
        val adjacentEdges = graph.edgesOf(oldTargetNode)
        adjacentEdges.forEach {
            val v = graph.getEdgeSource(it)
            if (v.state == Node.State.AVAILABLE) {
                return v
            }
        }

        return changeTargetNode(
            graph.getEdgeSource(adjacentEdges.first())
        )
    }
}

private suspend fun Graph<Node, DefaultEdge>.getMostExpensiveNode(): Node {
    val vertexSet = this.vertexSet()
    return (vertexSet.find { node ->
        node.quality == Node.Quality.IRIDESCENT
    } ?: vertexSet.find { node ->
        node.quality == Node.Quality.PURPLE
    } ?: vertexSet.find { node ->
        node.quality == Node.Quality.GREEN
    } ?: vertexSet.find { node ->
        node.quality == Node.Quality.YELLOW
    } ?: vertexSet.first()).apply {
        sendLog("Target Node: $this")
    }
}