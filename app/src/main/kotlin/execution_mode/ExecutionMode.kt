package execution_mode

import Constants.IN_BETWEEN_MOVEMENT_DURATION
import Constants.NEW_LEVEL_ANIMATION_DURATION
import Constants.PERK_SELECTION_ANIMATION_DURATION
import Constants.PRESTIGE_LEVEL_UP_DURATION
import Constants.SCREEN_SHOT_HEIGHT
import Constants.SCREEN_SHOT_WIDTH
import detector.AdvancedDetector
import detector.Detector
import helper.ClickHelper
import helper.sendLog
import helper.takeScreenShot
import kotlinx.coroutines.*
import model.BloodWeb
import model.Command
import java.awt.Robot
import java.awt.image.BufferedImage

abstract class ExecutionMode(
    protected val levels: Int,
    protected val detector: Detector,
    protected val delayNewLevelAnimation: Long,
    perkSelectionDuration: Long,
    movementDuration: Long,
    prestigeLevelUpDuration: Long,
) {

    private val robot = Robot()
    protected val clickHelper = ClickHelper(
        robot = robot,
        perkSelectionDuration = perkSelectionDuration,
        delayBetweenPerksSelection = movementDuration,
        prestigeLevelUpDuration = prestigeLevelUpDuration
    )

    private var executionScope = CoroutineScope(Dispatchers.Default)

    protected fun isExecutionWasStopped() = !executionScope.isActive

    fun run() {
        try {
            executionScope.launch {
                pumpBloodWeb()
            }
        } catch (e: java.lang.Exception) {
            executionScope.cancel()
            e.printStackTrace()
        }
    }

    fun stop() {
        try {
            executionScope.cancel()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    protected fun takeScreenShot(
        x: Int = 0,
        y: Int = 0,
        width: Int = SCREEN_SHOT_WIDTH,
        height: Int = SCREEN_SHOT_HEIGHT,
    ): BufferedImage {
        return robot.takeScreenShot(x, y, width, height)
    }
    
    private suspend fun pumpBloodWeb() {
        sendLog("Running ${this.javaClass.simpleName} mode")
        for (currentLevel in 1..levels) {
            if (isExecutionWasStopped()) return

            pumpOneBloodWebState()
            delay(delayNewLevelAnimation)
            clickHelper.moveOutCursor()
        }
        sendLog("Execution completed")
    }

    private suspend fun pumpOneBloodWebState() {
        sendLog("Pumping one BloodWeb state...")
        if (isExecutionWasStopped()) return

        val bloodWebScreenShot = takeScreenShot()
        detector.analyzeBloodWebPageState(bloodWebScreenShot).let { pageState ->
            sendLog("Current state is: ${pageState.name}")
            when (pageState) {
                BloodWeb.PageState.NOTIFICATION -> {
                    clickHelper.skipNotification()
                }
                BloodWeb.PageState.PRESTIGE -> {
                    clickHelper.upgradePrestigeLevel()
                }
                BloodWeb.PageState.LEVEL -> {
                    pumpOneLevelOfBloodWeb()
                }
            }
        }
    }
    
    abstract suspend fun pumpOneLevelOfBloodWeb()

    companion object {
        fun fromCommand(
            command: Command,
            delayNewLevelAnimation: Long = NEW_LEVEL_ANIMATION_DURATION,
            perkSelectionDuration: Long = PERK_SELECTION_ANIMATION_DURATION,
            movementDuration: Long = IN_BETWEEN_MOVEMENT_DURATION,
            prestigeLevelUpDuration: Long = PRESTIGE_LEVEL_UP_DURATION,
        ): ExecutionMode {
            return when (command.mode) {
                Command.Mode.SIMPLE -> SimpleExecutionMode(
                    levels = command.levels,
                    delayNewLevelAnimation = delayNewLevelAnimation,
                    perkSelectionDuration = perkSelectionDuration,
                    movementDuration = movementDuration,
                    prestigeLevelUpDuration = prestigeLevelUpDuration,
                    detector = AdvancedDetector(),
                )
                Command.Mode.RAREST_FIRST -> MostExpensiveExecutor(
                    levels = command.levels,
                    delayNewLevelAnimation = delayNewLevelAnimation,
                    perkSelectionDuration = perkSelectionDuration,
                    movementDuration = movementDuration,
                    prestigeLevelUpDuration = prestigeLevelUpDuration,
                    detector = AdvancedDetector(),
                )
                Command.Mode.FURTHEST -> FurthestNodeExecutor(
                    levels = command.levels,
                    delayNewLevelAnimation = delayNewLevelAnimation,
                    perkSelectionDuration = perkSelectionDuration,
                    movementDuration = movementDuration,
                    prestigeLevelUpDuration = prestigeLevelUpDuration,
                    detector = AdvancedDetector(),
                )
                else -> TestExecutionMode(
                    delayNewLevelAnimation = delayNewLevelAnimation,
                    perkSelectionDuration = perkSelectionDuration,
                    movementDuration = movementDuration,
                    prestigeLevelUpDuration = prestigeLevelUpDuration,
                    detector = AdvancedDetector(),
                )
            }
        }
    }
}