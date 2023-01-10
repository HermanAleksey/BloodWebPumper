import blood_web.*
import detector.Detector
import helper.convertIntoBufferImage
import java.awt.Point
import java.awt.image.BufferedImage
import java.io.File

class NodeHelper {

    fun getBufferedImageByFileName(fileName: String): BufferedImage {
        val file = File("src/test/resources/blood_web/$fileName")
        return file.convertIntoBufferImage()
    }

    fun createTestNode(
        circle: BloodWeb.BloodWebCircle,
        position: Int,
        state: InfoNode.State,
        quality: InfoNode.Quality,
    ): InfoNode = InfoNode(
        orderedNumber = Node.OrderedNumber(circle, position),
        topCenterCoord = Point(0, 0),
        state = state,
        quality = quality
    )
}

fun Detector.generateListOfPerksFromImage(bufferedImage: BufferedImage): List<InfoNode> {
    val allNodes = mutableListOf<InfoNode>()
    Presets().getAllNodes().forEach { node ->
        processNodeStateQuality(
            node = node,
            bufferedImage = bufferedImage
        ).let {
            node.toInfoNode(
                state = it.state,
                quality = it.quality
            ).let { infoNode ->
                allNodes.add(infoNode)
            }
        }
    }
    return allNodes
}

fun InfoNode.isEqualsNode(infoNode: InfoNode): Boolean {
    if (this.orderedNumber.circle != infoNode.orderedNumber.circle)
        return false
    if (this.orderedNumber.position != infoNode.orderedNumber.position)
        return false
    if (this.state != infoNode.state)
        return false
    //quality can be determined only for available and unavailable perks.
    if (this.state == InfoNode.State.AVAILABLE || this.state == InfoNode.State.AVAILABLE)
        if (this.quality != infoNode.quality)
            return false

    return true
}