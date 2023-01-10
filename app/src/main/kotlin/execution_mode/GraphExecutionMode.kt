package execution_mode

import blood_web.*
import detector.Detector
import helper.sendLog
import org.jgrapht.Graph
import org.jgrapht.graph.DefaultEdge
abstract class GraphExecutionMode(
    delayNewLevelAnimation: Long,
    perkSelectionDuration: Long,
    movementDuration: Long,
    prestigeLevelUpDuration: Long,
    detector: Detector,
    levels: Int
) : ExecutionMode(
    delayNewLevelAnimation = delayNewLevelAnimation,
    perkSelectionDuration = perkSelectionDuration,
    movementDuration = movementDuration,
    prestigeLevelUpDuration = prestigeLevelUpDuration,
    detector = detector,
    levels = levels
) {

    abstract suspend fun getTargetNode(graph: Graph<InfoNode, DefaultEdge>): InfoNode

    override suspend fun pumpOneLevelOfBloodWeb() {
        var isLevelFinished = false

        //cycle to upgrade one full level of BW
        while (!isLevelFinished) {
            //take screenshot and pump one route to the target perk
            val targetNode = getTargetNode(getCurrentBloodWebAsGraph())
            levelUpBranchToTargetNode(targetNode)

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
        targetNode: InfoNode
    ) {
        sendLog("Call levelUpNode: aiming for $targetNode")
        if (isExecutionWasStopped()) return

        //if we can pump our target perk - we do it
        if (targetNode.state == InfoNode.State.AVAILABLE) {
            clickHelper.performClickOnNode(targetNode)
        } else {
            val currentGraph = getCurrentBloodWebAsGraph()
            println(currentGraph.vertexSet())
            //else we are trying to pump neighbor of the target Node
            levelUpBranchToTargetNode(
                changeTargetNode(currentGraph, targetNode)
            )
        }
    }

    private suspend fun changeTargetNode(
        graph: Graph<InfoNode, DefaultEdge>,
        oldTargetNode: InfoNode
    ): InfoNode {
        sendLog("Вызов changeTargetNode")
        val adjacentEdges = graph.edgesOf(oldTargetNode)
        println("adjacentEdges: $adjacentEdges")
        adjacentEdges.forEach {
            val infoNode = graph.getEdgeSource(it)
            if (infoNode.state == InfoNode.State.AVAILABLE) {
                return infoNode
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