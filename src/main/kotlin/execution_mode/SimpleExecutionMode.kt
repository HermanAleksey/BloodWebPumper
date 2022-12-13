package execution_mode

class SimpleExecutionMode(
    delayNewLevelAnimation: Long,
    perkSelectionDuration: Long,
    movementDuration: Long,
    private val levels: Int
) : ExecutionMode(
    delayNewLevelAnimation = delayNewLevelAnimation,
    perkSelectionDuration = perkSelectionDuration,
    movementDuration = movementDuration
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
        val isPrestigeLevel = bloodWeb.checkPrestigeUpgrade(bloodWebScreenShot)

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

        bloodWeb.checkAvailablePerks(
            circle = circle,
            bloodWebScreenShot
        ).let {
            println("Circle:$circle $it")
            fileHelper.appendFileLine("Circle:$circle \n$it")

            it.forEach { perk ->
                clickHelper.performClickOnPerk(perk)
            }
        }
    }
}