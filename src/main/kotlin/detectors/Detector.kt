package detectors

import blood_web.Node
import java.awt.Point
import java.awt.image.BufferedImage

interface Detector {

    /**
     * @return [Node.State] of Node in given [nodePoint]
     * */
    fun analyzeSingleNode(nodePoint: Point, bufferedImage: BufferedImage): Node.State?

    /**
     * @return true if center - is prestige level
     * @return false otherwise
     * */
    fun analyzeCenterOfBloodWeb(bufferedImage: BufferedImage): Boolean

    fun analyzeBloodPointsAmount() {
        //todo implement in 2024
    }
}