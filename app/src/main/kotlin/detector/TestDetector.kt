package detector

import blood_web.BloodWebPageState
import blood_web.Node
import java.awt.image.BufferedImage

class TestDetector: Detector {
    override fun processNodeStateQuality(node: Node, bufferedImage: BufferedImage) {
        TODO("Not yet implemented")
    }

    override fun analyzeBloodWebPageState(bufferedImage: BufferedImage): BloodWebPageState {
        TODO("Not yet implemented")
    }
}