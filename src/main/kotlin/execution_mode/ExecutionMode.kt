package execution_mode

import Constants.IN_BETWEEN_MOVEMENT_DURATION
import Constants.NEW_LEVEL_ANIMATION_DURATION
import Constants.PERK_SELECTION_ANIMATION_DURATION
import Constants.PRESTIGE_LEVEL_UP_DURATION
import detector.AdvancedDetector
import detector.Detector
import detector.SimpleDetector
import helper.ClickHelper
import helper.Command
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.awt.Robot

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

    private var executionScope = CoroutineScope(Dispatchers.Main)

    fun stop() {
        try {
            executionScope.cancel()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

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

    abstract suspend fun pumpBloodWeb()

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
                    detector = SimpleDetector(),
                )
                Command.Mode.CHEAPEST_FIRST -> SecondGraderExecutor(
                    levels = command.levels,
                    delayNewLevelAnimation = delayNewLevelAnimation,
                    perkSelectionDuration = perkSelectionDuration,
                    movementDuration = movementDuration,
                    prestigeLevelUpDuration = prestigeLevelUpDuration,
                    detector = AdvancedDetector(),
                )
                else -> TestExecutionMode()
            }
        }
    }
}