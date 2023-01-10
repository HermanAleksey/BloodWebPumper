package execution_mode

import blood_web.*
import detector.Detector
import helper.save
import helper.sendLog
import kotlinx.coroutines.delay
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
            if (isExecutionWasStopped()) return

            sendLog("Pumping level #$currentLevel")
            pumpOneBloodWebLevel()
            delay(delayNewLevelAnimation)
            clickHelper.moveOutCursor()
        }
        sendLog("SimpleExecutionMode completed")
    }

    private suspend fun pumpOneBloodWebLevel() {
        if (isExecutionWasStopped()) return

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

    private suspend fun pumpOneCircleOfBloodWeb(circle: BloodWeb.BloodWebCircle) {
        if (isExecutionWasStopped()) return

        val bloodWebScreenShot = takeScreenShot()
        if (takeScreenShots)
            bloodWebScreenShot.save(
                fileName = "BloodWebScreenShot.png",
            )

        checkPerksInCircle(
            circle = circle,
            bloodWebScreenShot
        ).let { infoNodes ->
            sendLog("Pumping $circle")
            infoNodes.forEach {
                sendLog(it.toString())
            }

            infoNodes.filter { infoNode ->
                infoNode.state == InfoNode.State.AVAILABLE
            }.forEach { infoNode ->
                if (isExecutionWasStopped()) return

                clickHelper.performClickOnNode(infoNode)
            }
        }
    }

    //goes through all positions of item in [circle] of bloodWeb
    //If perk available - add it into array
    //return array of available perks
    private fun checkPerksInCircle(
        circle: BloodWeb.BloodWebCircle,
        bufferedImage: BufferedImage
    ): List<InfoNode> {
        val presets = when (circle) {
            BloodWeb.BloodWebCircle.INNER -> Presets().innerNodes
            BloodWeb.BloodWebCircle.MIDDLE -> Presets().middleNodes
            BloodWeb.BloodWebCircle.OUTER -> Presets().outerNodes
        }
        val availableNodes = mutableListOf<InfoNode>()

        presets.forEach { node ->
            detector.processNodeStateQuality(node, bufferedImage).let {
                availableNodes.add(
                    node.toInfoNode(
                        state = it.state,
                        quality = it.quality
                    )
                )
            }
        }
        return availableNodes
    }
}