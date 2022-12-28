package detector

import blood_web.Node
import java.awt.image.BufferedImage

interface Detector {

    /**
     * Takes [node] and apple [Node.State] and [Node.Quality] params to it
     * */
    fun processNodeStateQuality(node: Node, bufferedImage: BufferedImage)

    /**
     * @return true if center - is prestige level
     * @return false otherwise
     * */
    fun analyzeCenterOfBloodWeb(bufferedImage: BufferedImage): Boolean

    /**
     * @return true if screen has skipable notification (e.g. lvl 0,5,10, prestige 1,2,3)
     * @return false otherwise
     * */
    fun checkSkipableNotification(bufferedImage: BufferedImage): Boolean

    fun analyzeBloodPointsAmount() {
        //todo implement in 2024
    }
}