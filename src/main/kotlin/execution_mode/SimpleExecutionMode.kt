package execution_mode

import blood_web.BloodWeb
import blood_web.Node
import blood_web.Presets
import blood_web.parseIntoNode
import detector.Detector
import kotlinx.coroutines.runBlocking
import executionLogs
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
        println("Running SimpleExecutionMode")
        for (currentLevel in 1..levels) {
            println("Pumping level #$currentLevel")
            pumpOneBloodWebLevel()
            delay(delayNewLevelAnimation)
            clickHelper.moveOutCursor()
        }
        println("SimpleExecutionMode completed")
    }

    private fun pumpOneBloodWebLevel() {
        val bloodWebScreenShot = clickHelper.takeScreenShot()
        val isPrestigeLevel = detector.analyzeCenterOfBloodWeb(bloodWebScreenShot)

        if (isPrestigeLevel) {
            clickHelper.upgradeAndSkipPrestigeLevel()
        } else {
            BloodWeb.BloodWebCircle.values().forEach {
                pumpOneCircleOfBloodWeb(it)
            }
        }
    }

    private fun pumpOneCircleOfBloodWeb(circle: BloodWeb.BloodWebCircle) = runBlocking {
        val bloodWebScreenShot = clickHelper.takeScreenShot()
        if (takeScreenShots)
            clickHelper.saveScreenShot(
                screenShot = bloodWebScreenShot,
            )

        checkAvailablePerks(
            circle = circle,
            bloodWebScreenShot
        ).let {
            executionLogs.emit("Circle:$circle \n$it")
            println("Circle:$circle \n$it")

            it.forEach { perk ->
                clickHelper.performClickOnPerk(perk)
            }
        }
    }

    //goes through all positions of item in [circle] of bloodWeb
    //If perk available - add it into array
    //return array of available perks
    private fun checkAvailablePerks(
        circle: BloodWeb.BloodWebCircle,
        bufferedImage: BufferedImage
    ): List<Node> {
        val presets = when (circle) {
            BloodWeb.BloodWebCircle.INNER -> Presets().innerPoints
            BloodWeb.BloodWebCircle.SECONDARY -> Presets().secondaryPoints
            BloodWeb.BloodWebCircle.OUTER -> Presets().outerPoints
        }
        val availableNodes = mutableListOf<Node>()

        presets.forEach { point ->
            val initialNode = point.parseIntoNode()
            detector.analyzeSingleNode(initialNode, bufferedImage).let { processedNode ->
                if (processedNode.state == Node.State.AVAILABLE)
                    availableNodes.add(
                        processedNode
                    )
            }
        }
        return availableNodes
    }
}