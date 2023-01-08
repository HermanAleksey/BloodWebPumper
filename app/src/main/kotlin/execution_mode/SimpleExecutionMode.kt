package execution_mode

import blood_web.*
import detector.Detector
import helper.save
import helper.sendLog
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.awt.image.BufferedImage


class SimpleExecutionMode(
    delayNewLevelAnimation: Long,
    perkSelectionDuration: Long,
    movementDuration: Long,
    prestigeLevelUpDuration: Long,
    detector: Detector,
    private val levels: Int
) : ExecutionMode(
    delayNewLevelAnimation = delayNewLevelAnimation,
    perkSelectionDuration = perkSelectionDuration,
    movementDuration = movementDuration,
    prestigeLevelUpDuration = prestigeLevelUpDuration,
    detector = detector
) {

    private val takeScreenShots = false

    override suspend fun pumpBloodWeb() {
        sendLog("Running SimpleExecutionMode")
        for (currentLevel in 1..levels) {
            sendLog("Pumping level #$currentLevel")
            pumpOneBloodWebLevel()
            delay(delayNewLevelAnimation)
            clickHelper.moveOutCursor()
        }
        sendLog("SimpleExecutionMode completed")
    }

    private suspend fun pumpOneBloodWebLevel() {
        val bloodWebScreenShot = takeScreenShot()
        detector.analyzeBloodWebPageState(bloodWebScreenShot).let { pageState ->
            when (pageState) {
                BloodWebPageState.NOTIFICATION -> {
                    sendLog("Is skipable notification level")
                    clickHelper.skipNotification()
                }
                BloodWebPageState.PRESTIGE -> {
                    sendLog("Is prestige level")
                    clickHelper.upgradePrestigeLevel()
                }
                BloodWebPageState.LEVEL -> {
                    BloodWeb.BloodWebCircle.values().forEach {
                        pumpOneCircleOfBloodWeb(it)
                    }
                }
            }
        }
    }

    private suspend fun pumpOneCircleOfBloodWeb(circle: BloodWeb.BloodWebCircle) = runBlocking {
        val bloodWebScreenShot = takeScreenShot()
        if (takeScreenShots)
            bloodWebScreenShot.save(
                fileName = "BloodWebScreenShot.png",
            )

        checkPerksInCircle(
            circle = circle,
            bloodWebScreenShot
        ).let { nodes ->
            sendLog("Pumping $circle")
            nodes.forEach {
                sendLog(it.toLogString())
            }

            nodes.filter { node ->
                node.state == Node.State.AVAILABLE
            }.forEach { node ->
                clickHelper.performClickOnNode(node)
            }
        }
    }

    //goes through all positions of item in [circle] of bloodWeb
    //If perk available - add it into array
    //return array of available perks
    private fun checkPerksInCircle(
        circle: BloodWeb.BloodWebCircle,
        bufferedImage: BufferedImage
    ): List<Node> {
        val presets = when (circle) {
            BloodWeb.BloodWebCircle.INNER -> Presets().innerPoints
            BloodWeb.BloodWebCircle.MIDDLE -> Presets().middlePoints
            BloodWeb.BloodWebCircle.OUTER -> Presets().outerPoints
        }
        val availableNodes = mutableListOf<Node>()

        presets.forEach { point ->
            val node = point.parseIntoNode()
            detector.processNodeStateQuality(node, bufferedImage).let {
                availableNodes.add(
                    node
                )
            }
        }
        return availableNodes
    }
}