package execution_mode

import blood_web.*
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
                    sendLog("Вызов pumpOneLevel")
                    pumpOneLevel()
                }
            }
        }
    }

    private suspend fun pumpOneLevel() {
        var isLevelFinished = false

        //cycle to upgrade one full level of BW
        while (!isLevelFinished) {
            //take screenshot and pump one route to the target perk
            val graph = getCurrentBloodWebAsGraph()
            val targetNode = graph.getMostExpensiveNode()
            levelUpBranchToTargetNode(graph, targetNode)

            //take new screenshot and check if graph still has available perks to pump
            //if it has -> while goes for next iteration
            getCurrentBloodWebAsGraph().vertexSet().any {
                it.isAccessible()
            }.let { haveAvailableNodes ->
                isLevelFinished = !haveAvailableNodes
            }
        }
    }

    private suspend fun levelUpBranchToTargetNode(
        graph: Graph<InfoNode, DefaultEdge>,
        targetNode: InfoNode
    ) {
        sendLog("Call levelUpNode: aiming for $targetNode")
        if (isExecutionWasStopped()) return

        //if we can pump our target perk - we do it
        if (targetNode.state == InfoNode.State.AVAILABLE) {
            clickHelper.performClickOnNode(targetNode)
        } else {
            //else we are trying to pump neighbor of the target Node
            levelUpBranchToTargetNode(
                graph,
                changeTargetNode(graph, targetNode)
            )
        }
    }

    private suspend fun changeTargetNode(
        graph: Graph<InfoNode, DefaultEdge>,
        oldTargetNode: InfoNode
    ): InfoNode {
        sendLog("Вызов changeTargetNode")
        val adjacentEdges = graph.edgesOf(oldTargetNode)
        adjacentEdges.forEach {
            val v = graph.getEdgeSource(it)
            if (v.state == InfoNode.State.AVAILABLE) {
                return v
            }
        }

        return changeTargetNode(
            graph,
            graph.getEdgeSource(adjacentEdges.first())
        )
    }

    private suspend fun getCurrentBloodWebAsGraph(): Graph<InfoNode, DefaultEdge> {
        val bloodWebScreenShot = takeScreenShot()
        sendLog("Вызов updateGraph")

        Presets().getAllNodes().let { nodes ->
            val infoNodes = mutableListOf<InfoNode>()
            nodes.forEach { node ->
                detector.processNodeStateQuality(node, bloodWebScreenShot).let { stateAndQuality ->
                    infoNodes.add(
                        node.toInfoNode(
                            stateAndQuality.state,
                            stateAndQuality.quality
                        )
                    )
                }
            }

            return infoNodes.filter { infoNode ->
                //true if node.state is closed or null
                infoNode.isAccessible()
            }.createGraph()
        }
    }
}

private suspend fun Graph<InfoNode, DefaultEdge>.getMostExpensiveNode(): InfoNode {
    val vertexSet = this.vertexSet()
    return (vertexSet.find { infoNode ->
        infoNode.quality == InfoNode.Quality.IRIDESCENT
    } ?: vertexSet.find { node ->
        node.quality == InfoNode.Quality.PURPLE
    } ?: vertexSet.find { node ->
        node.quality == InfoNode.Quality.GREEN
    } ?: vertexSet.find { node ->
        node.quality == InfoNode.Quality.YELLOW
    } ?: vertexSet.first()).apply {
        sendLog("Target Node: $this")
    }
}