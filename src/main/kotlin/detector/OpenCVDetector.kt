package detector

import blood_web.Node
import java.awt.Point
import java.awt.image.BufferedImage

class OpenCVDetector : Detector{
    override fun analyzeSingleNode(nodePoint: Point, bufferedImage: BufferedImage): Node.State? {
        TODO("Not yet implemented")
    }

    override fun analyzeCenterOfBloodWeb(bufferedImage: BufferedImage): Boolean {
        TODO("Not yet implemented")
    }
}