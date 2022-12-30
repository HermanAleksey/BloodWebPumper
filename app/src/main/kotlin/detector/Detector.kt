package detector

import blood_web.BloodWebPageState
import blood_web.Node
import java.awt.image.BufferedImage

interface Detector {

    /**
     * Takes [node] and apple [Node.State] and [Node.Quality] params to it
     * */
    fun processNodeStateQuality(node: Node, bufferedImage: BufferedImage)

    fun analyzeBloodWebPageState(bufferedImage: BufferedImage): BloodWebPageState

    fun analyzeBloodPointsAmount() {
        //todo implement in 2024
    }
}