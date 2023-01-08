package execution_mode

import blood_web.*
import detector.Detector
import helper.save
import helper.sendLog
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.jgrapht.Graph
import org.jgrapht.graph.DefaultEdge
import java.awt.image.BufferedImage

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

    private val takeScreenShots = false
    val graph = createFullGraph();


    override suspend fun pumpBloodWeb() {
        sendLog("Running SimpleExecutionMode")
        for (currentLevel in 1..levels) {
            sendLog("Pumping level #$currentLevel")
            pumpOneBloodWebLevel()
            delay(delayNewLevelAnimation)
            clickHelper.moveOutCursor()
        }
        sendLog("SimpleExecutionMode completed")
    }

    private suspend fun pumpOneBloodWebLevel() {
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
                    BloodWeb.BloodWebCircle.values().forEach {
                        pumpOneCircleOfBloodWeb(it)
                    }
                }
            }
        }
    }

    private suspend fun pumpOneCircleOfBloodWeb(circle: BloodWeb.BloodWebCircle) = runBlocking {
        val bloodWebScreenShot = takeScreenShot()
        if (takeScreenShots)
            bloodWebScreenShot.save(
                fileName = "BloodWebScreenShot.png",
            )

        checkPerksInCircle(
            circle = circle,
            bloodWebScreenShot
        ).let { nodes ->
            sendLog("Pumping $circle")
            nodes.forEach {
                sendLog(it.toLogString())
            }

            nodes.filter { node ->
                node.state == Node.State.EMPTY
            }.forEach { perk ->
                graph.removeVertex(perk)
            }
        }

        val vertexSet = graph.vertexSet()
        val targetPerk = vertexSet.find { node ->
            node.quality == Node.Quality.IRIDESCENT
        }?:vertexSet.find { node ->
            node.quality == Node.Quality.PURPLE
        }?:vertexSet.find { node ->
            node.quality == Node.Quality.GREEN
        }?:vertexSet.find { node ->
            node.quality == Node.Quality.YELLOW
        }?:vertexSet.first()
    }

    private fun checkPerksInCircle(
        circle: BloodWeb.BloodWebCircle,
        bufferedImage: BufferedImage
    ): List<Node> {
        val presets = when (circle) {
            BloodWeb.BloodWebCircle.INNER -> Presets().innerPoints
            BloodWeb.BloodWebCircle.MIDDLE -> Presets().middlePoints
            BloodWeb.BloodWebCircle.OUTER -> Presets().outerPoints
        }
        val availableNodes = mutableListOf<Node>()

        presets.forEach { point ->
            val node = point.parseIntoNode()
            detector.processNodeStateQuality(node, bufferedImage).let {
                availableNodes.add(
                    node
                )
            }
        }
        return availableNodes
    }

    private suspend fun levelToTarget(
        graph: Graph<Node, DefaultEdge>,
        targetNode: Node
    ) {
        val bloodWebScreenShot = takeScreenShot()

        when (targetNode.orderedNumber.circle) {
            BloodWeb.BloodWebCircle.INNER -> {
                clickHelper.performClickOnNode(targetNode)
            }
            BloodWeb.BloodWebCircle.MIDDLE -> {
                val innerMiddleEdges = graph.edgesOf(targetNode)
                var innerVertexes: Set<Node> = emptySet()
                innerMiddleEdges.forEach{innerVertexes.plus(graph.getEdgeSource(it))}

                val availableNode = innerVertexes.first { node ->
                    node.state == Node.State.AVAILABLE
                }
                clickHelper.performClickOnNode(availableNode)
            }
            BloodWeb.BloodWebCircle.OUTER -> {

            }
        }
    }
}