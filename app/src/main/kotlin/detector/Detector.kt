package detector

import blood_web.BloodWebPageState
import blood_web.Node
import java.awt.image.BufferedImage

interface Detector {

    /**
     * Takes [node] and screenshot of screen as[bufferedImage]
     * @return State and Quality of this node
     * */
    fun processNodeStateQuality(node: Node, bufferedImage: BufferedImage): AdvancedDetector.NodeStateQuality

    fun analyzeBloodWebPageState(bufferedImage: BufferedImage): BloodWebPageState

    fun analyzeBloodPointsAmount() {
        //todo implement in 2024
    }
}