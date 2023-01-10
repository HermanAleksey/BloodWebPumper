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
import helper.Command
import helper.takeScreenShot
import kotlinx.coroutines.*
import java.awt.Robot
import java.awt.image.BufferedImage

sealed class ExecutionMode(
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

    protected abstract suspend fun pumpBloodWeb()

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