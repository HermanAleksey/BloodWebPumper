package execution_mode

import model.InfoNode
import model.Presets
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
    detector = detector,
    levels = 1
) {

    override suspend fun pumpOneLevelOfBloodWeb() {
        sendLog("Test: Analyzing current blood web state")
        val bloodWebScreenShot = takeScreenShot()
        val availableNodes = mutableListOf<InfoNode>()

        Presets().getAllNodes().forEach { node ->
            detector.processNodeStateQuality(node, bloodWebScreenShot).let {
                availableNodes.add(
                    node.toInfoNode(
                        state = it.state,
                        quality = it.quality
                    )
                )
            }
        }

        availableNodes.filter {
            it.state != InfoNode.State.EMPTY
        }.forEach {
            sendLog(it.toString())
        }
        sendLog("End of test run")
    }
}