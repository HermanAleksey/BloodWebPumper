package execution_mode

import blood_web.BloodWeb
import blood_web.Node
import blood_web.Presets
import detectors.Detector
import java.awt.image.BufferedImage

class SimpleExecutionMode(
    delayNewLevelAnimation: Long,
    perkSelectionDuration: Long,
    movementDuration: Long,
    detector: Detector,
    private val levels: Int
) : ExecutionMode(
    delayNewLevelAnimation = delayNewLevelAnimation,
    perkSelectionDuration = perkSelectionDuration,
    movementDuration = movementDuration,
    detector = detector
) {

    private val takeScreenShots = false

    override fun pumpBloodWeb() {
        fileHelper.appendFileLine("Running SimpleExecutionMode")
        for (currentLevel in 1..levels) {
            pumpOneBloodWebLevel()
            Thread.sleep(delayNewLevelAnimation)
            clickHelper.moveOutCursor()
        }
        fileHelper.appendFileLine("SimpleExecutionMode completed")
    }

    private fun pumpOneBloodWebLevel() {
        val bloodWebScreenShot = clickHelper.takeScreenShot()
        val isPrestigeLevel = detector.analyzeCenterOfBloodWeb(bloodWebScreenShot)

        if (isPrestigeLevel) {
            clickHelper.performClickOnCenterOfBloodWeb()
        } else {
            BloodWeb.BloodWebCircle.values().forEach {
                pumpOneCircleOfBloodWeb(it)
            }
        }
    }

    private fun pumpOneCircleOfBloodWeb(circle: BloodWeb.BloodWebCircle) {
        val bloodWebScreenShot = clickHelper.takeScreenShot()
        if (takeScreenShots)
            clickHelper.saveScreenShot(
                screenShot = bloodWebScreenShot,
            )

        checkAvailablePerks(
            circle = circle,
            bloodWebScreenShot
        ).let {
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
        circle: BloodWeb.BloodWebCircle, bufferedImage: BufferedImage
    ): List<Node> {
        val presets = when (circle) {
            BloodWeb.BloodWebCircle.INNER -> Presets().innerPoints
            BloodWeb.BloodWebCircle.SECONDARY -> Presets().secondaryPoints
            BloodWeb.BloodWebCircle.OUTER -> Presets().outerPoints
        }
        val availableNodes = mutableListOf<Node>()

        presets.forEachIndexed { position, point ->
            detector.analyzeSingleNode(point, bufferedImage)?.let { nodeState ->
                if (nodeState == Node.State.AVAILABLE)
                    availableNodes.add(
                        Node(
                            orderedNumber = Node.OrderedNumber(circle, position),
                            topCenterCoord = point
                        )
                    )
            }
        }
        return availableNodes
    }
}