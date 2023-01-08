package execution_mode

import blood_web.*
import com.mxgraph.layout.mxCircleLayout
import com.mxgraph.layout.mxIGraphLayout
import com.mxgraph.util.mxCellRenderer
import detector.Detector
import helper.sendLog
import kotlinx.coroutines.delay
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

    private val takeScreenShots = false
    private val graph = createFullGraph();


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
                    updateGraph()
                    levelToTarget(findExpensiveNode())
                }
            }
        }
    }

    private suspend fun updateGraph() {
        val bloodWebScreenShot = takeScreenShot()
        sendLog("Вызов updateGraph")

        var vertices = graph.vertexSet()
        vertices.forEach(){node ->
            detector.processNodeStateQuality(node, bloodWebScreenShot)
        }



        vertices.filter {node ->
            node.state == Node.State.EMPTY || node.state == Node.State.LOCKED
        }.forEach { vertex ->
//            vertices.remove(vertex)
//            sendLog(vertex.toString())
            val isEqual = graph.removeVertex(vertex)
            sendLog("$vertex is removed $isEqual")
            sendLog("and graph contain vertex: ${graph.containsVertex(vertex)}")
        }
        graph.vertexSet().forEach{ println(it) }
//        drawGraph()
    }

    private suspend fun drawGraph(){
        sendLog("Вызов drawGraph")

        val imgFile = File("resources/graph1.png")
        imgFile.createNewFile()
        val graphAdapter = JGraphXAdapter(graph)

        val layout: mxIGraphLayout = mxCircleLayout(graphAdapter)

        layout.execute(graphAdapter.defaultParent)

        val image: BufferedImage = mxCellRenderer.createBufferedImage(graphAdapter, null, 2.0, Color.WHITE, true, null)

        ImageIO.write(image, "PNG", imgFile)
    }


    private suspend fun findExpensiveNode(): Node{
        sendLog("Вызов findExpensiveNode")
        val vertexSet = graph.vertexSet()
        return (vertexSet.find { node ->
            node.quality == Node.Quality.IRIDESCENT
        }?:vertexSet.find { node ->
            node.quality == Node.Quality.PURPLE
        }?:vertexSet.find { node ->
            node.quality == Node.Quality.GREEN
        }?:vertexSet.find { node ->
            node.quality == Node.Quality.YELLOW
        }?:vertexSet.first()).apply { sendLog("Target Node: $this") }
    }

    private suspend fun changeTargetNode(oldTargetNode: Node): Node? {
        sendLog("Вызов changeTargetNode")
        val adjacentEdges = graph.edgesOf(oldTargetNode)
        adjacentEdges.forEach{
            val v = graph.getEdgeSource(it)
            if (v.state == Node.State.AVAILABLE){
                return v
            }
        }
        
        adjacentEdges.forEach{
            return graph.getEdgeSource(it)
        }
        
        return null
    }

    private suspend fun levelUpNode(node: Node){
        sendLog("Вызов levelUpNode")
        node.state?.let {
            when (it) {
                Node.State.AVAILABLE -> clickHelper.performClickOnNode(node)
                Node.State.UNAVAILABLE, Node.State.LOCKED, Node.State.BOUGHT, Node.State.EMPTY -> 
                    changeTargetNode(node)?.let {newTargetNode -> levelUpNode(newTargetNode) }                    
            }
        }
    }

    private fun setNodesParametersInCircle(
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
                targetNode: Node
    ) {
        sendLog("Вызов levelToTarget")
        while (targetNode.state != Node.State.BOUGHT || targetNode.state != Node.State.LOCKED) {
            levelUpNode(targetNode)
            updateGraph()
        }
    }
}