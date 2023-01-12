package execution_mode

import detector.Detector
import helper.save
import helper.sendLog
import model.BloodWeb
import model.InfoNode
import model.Presets
import java.awt.image.BufferedImage


class SimpleExecutionMode(
    delayNewLevelAnimation: Long,
    perkSelectionDuration: Long,
    movementDuration: Long,
    prestigeLevelUpDuration: Long,
    detector: Detector,
    levels: Int
) : ExecutionMode(
    delayNewLevelAnimation = delayNewLevelAnimation,
    perkSelectionDuration = perkSelectionDuration,
    movementDuration = movementDuration,
    prestigeLevelUpDuration = prestigeLevelUpDuration,
    detector = detector,
    levels = levels
) {

    private val takeScreenShots = false

    override suspend fun pumpOneLevelOfBloodWeb() {
        BloodWeb.BloodWebCircle.values().forEach {
            pumpOneCircleOfBloodWeb(it)
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