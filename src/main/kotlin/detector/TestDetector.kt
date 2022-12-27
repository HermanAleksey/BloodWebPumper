package detector

import blood_web.Node
import java.awt.Point
import java.awt.image.BufferedImage

class TestDetector: Detector {
    override fun processNodeStateQuality(node: Node, bufferedImage: BufferedImage) {
        TODO("Not yet implemented")
    }

    override fun analyzeCenterOfBloodWeb(bufferedImage: BufferedImage): Boolean {
        TODO("Not yet implemented")
    }
}