package launch_mode

import detector.AdvancedDetector
import helper.convertIntoBufferImage
import org.opencv.core.Core
import java.awt.Point
import java.io.File


class TestLauncher : AppLauncher {

    init {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    override fun run() {
        val detector = AdvancedDetector()

        val filePath = "resources/nodes/yellow/$it.png"
        val file = File(filePath)
        val bi = file.convertIntoBufferImage()
        val node = detector.analyzeSingleNode(
            nodePoint = Point(),
            bufferedImage = bi,
        )
        colorRanges()
    }

    private fun colorRanges() {

        val qualities = arrayOf("brown", "yellow", "green", "purple", "red", "event")
        val states = arrayOf("available", "locked", "taken", "unavailable")

        states.forEach {
            val filePath = "resources/nodes/yellow/$it.png"
            val file = File(filePath)
            println(checkNodeState(file.convertIntoBufferImage()))
        }
    }
}