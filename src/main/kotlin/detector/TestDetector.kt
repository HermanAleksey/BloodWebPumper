package detector

import blood_web.Node
import java.awt.Point
import java.awt.image.BufferedImage

class TestDetector: Detector {
    override fun analyzeSingleNode(nodePoint: Node, bufferedImage: BufferedImage): Node {
        TODO("Not yet implemented")
    }

    override fun analyzeCenterOfBloodWeb(bufferedImage: BufferedImage): Boolean {
        TODO("Not yet implemented")
    }
}