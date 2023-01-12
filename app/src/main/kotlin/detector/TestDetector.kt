package detector

import model.BloodWebPageState
import model.Node
import java.awt.image.BufferedImage

class TestDetector: Detector {
    override fun processNodeStateQuality(node: Node, bufferedImage: BufferedImage): AdvancedDetector.NodeStateQuality {
        TODO("Not yet implemented")
    }

    override fun analyzeBloodWebPageState(bufferedImage: BufferedImage): BloodWebPageState {
        TODO("Not yet implemented")
    }
}