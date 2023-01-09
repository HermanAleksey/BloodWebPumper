package execution_mode

import blood_web.*
import com.mxgraph.layout.mxCircleLayout
import com.mxgraph.layout.mxIGraphLayout
import com.mxgraph.util.mxCellRenderer
import detector.Detector
import helper.sendLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jgrapht.ext.JGraphXAdapter
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

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

    private var graph = createFullGraph();

    override suspend fun pumpBloodWeb() {
        sendLog("Вызов pumpBloodWeb")
        for (currentLevel in 1..levels) {
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

    private suspend fun updateGraph() {
        val bloodWebScreenShot = takeScreenShot()
        sendLog("Вызов updateGraph")

        val vertices = graph.vertexSet()
        vertices.forEach { node ->
            detector.processNodeStateQuality(node, bloodWebScreenShot)
        }

        vertices.filter { node ->
            //true if node.state is closed or null
            node.isInaccessible()
        }.forEach { unavailableVertexes ->
//            vertices.remove(vertex)
//            sendLog(vertex.toString())
            val isEqual = graph.removeVertex(unavailableVertexes)
            sendLog("$unavailableVertexes is removed $isEqual")
            sendLog("and graph contain vertex: ${graph.containsVertex(unavailableVertexes)}")
        }
        graph.vertexSet().forEach { println(it) }
//        drawGraph()
    }

    private suspend fun drawGraph() = withContext(Dispatchers.IO) {
        launch{
            sendLog("Вызов drawGraph")

            val imgFile = File("resources/graph1.png")
            imgFile.createNewFile()
            val graphAdapter = JGraphXAdapter(graph)

            val layout: mxIGraphLayout = mxCircleLayout(graphAdapter)

            layout.execute(graphAdapter.defaultParent)

            val image: BufferedImage =
                mxCellRenderer.createBufferedImage(graphAdapter, null, 2.0, Color.WHITE, true, null)


            ImageIO.write(image, "PNG", imgFile)
        }
    }


    private suspend fun findMostExpensiveNode(): Node {
        sendLog("Вызов findExpensiveNode")
        val vertexSet = graph.vertexSet()
        return (vertexSet.find { node ->
            node.quality == Node.Quality.IRIDESCENT
        } ?: vertexSet.find { node ->
            node.quality == Node.Quality.PURPLE
        } ?: vertexSet.find { node ->
            node.quality == Node.Quality.GREEN
        } ?: vertexSet.find { node ->
            node.quality == Node.Quality.YELLOW
        } ?: vertexSet.first()).apply { sendLog("Target Node: $this") }
    }

    private suspend fun levelUpBranchToTargetNode(node: Node) {
        sendLog("Вызов levelUpNode")
        node.state?.let {
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

    private suspend fun changeTargetNode(oldTargetNode: Node): Node? {
        sendLog("Вызов changeTargetNode")
        val adjacentEdges = graph.edgesOf(oldTargetNode)
        adjacentEdges.forEach {
            val v = graph.getEdgeSource(it)
            if (v.state == Node.State.AVAILABLE) {
                return v
            }
        }

        adjacentEdges.forEach {
            return changeTargetNode(graph.getEdgeSource(it))
        }

        sendLog("Null Pointer in changeTargetNode()")
        throw NullPointerException()
    }

    private suspend fun levelEverything() {
        sendLog("Вызов levelEverything")

        val vertices = graph.vertexSet()
        var isLevelFinished = false

        while (!isLevelFinished) {
            levelUpBranchToTargetNode(findMostExpensiveNode())
            updateGraph()
            vertices.any{
                it.isAccessible()
            }.let { isLevelFinished = !it }
        }
    }
}