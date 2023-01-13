package detector

import model.BloodWeb
import model.Node
import java.awt.image.BufferedImage

interface Detector {

    /**
     * Takes [node] and screenshot of screen as[bufferedImage]
     * @return State and Quality of this node
     * */
    fun processNodeStateQuality(node: Node, bufferedImage: BufferedImage): AdvancedDetector.NodeStateQuality

    fun analyzeBloodWebPageState(bufferedImage: BufferedImage): BloodWeb.PageState

    fun analyzeBloodPointsAmount() {
        //todo implement in 2024
    }
}