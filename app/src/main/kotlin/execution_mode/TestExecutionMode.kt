package execution_mode

import blood_web.Node
import blood_web.Presets
import blood_web.parseIntoNode
import detector.Detector
import helper.sendLog

class TestExecutionMode(
    delayNewLevelAnimation: Long,
    perkSelectionDuration: Long,
    movementDuration: Long,
    prestigeLevelUpDuration: Long,
    detector: Detector,
) : ExecutionMode(
    delayNewLevelAnimation = delayNewLevelAnimation,
    perkSelectionDuration = perkSelectionDuration,
    movementDuration = movementDuration,
    prestigeLevelUpDuration = prestigeLevelUpDuration,
    detector = detector
) {

    override suspend fun pumpBloodWeb() {
        sendLog("Analyzing current blood web state")
        val bloodWebScreenShot = takeScreenShot()
        val availableNodes = mutableListOf<Node>()

        Presets().getAllPoints().forEach { point ->
            val node = point.parseIntoNode()
            detector.processNodeStateQuality(node, bloodWebScreenShot).let {
                availableNodes.add(
                    node
                )
            }
        }

        availableNodes.filter {
            it.state != Node.State.EMPTY
        }.forEach {
            sendLog(it.toLogString())
        }
        sendLog("End of test run")
    }
}