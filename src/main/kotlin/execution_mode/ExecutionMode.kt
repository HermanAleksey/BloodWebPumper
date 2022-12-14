package execution_mode

import helper.ClickHelper
import Constants.IN_BETWEEN_MOVEMENT_DURATION
import Constants.NEW_LEVEL_ANIMATION_DURATION
import Constants.PERK_SELECTION_ANIMATION_DURATION
import Constants.PRESTIGE_LEVEL_UP_DURATION
import detector.Detector
import detector.SimpleDetector
import helper.Command
import helper.TextFileHelper
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
    protected val fileHelper = TextFileHelper()

    private lateinit var executionThread: Thread

    fun stop() {
        try {
            executionThread.stop()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    fun run() {
        try {
            executionThread = Thread {
                pumpBloodWeb()
            }
            executionThread.start()
        } catch (e: java.lang.Exception) {
            executionThread.stop()
            e.printStackTrace()
        }
    }

    abstract fun pumpBloodWeb()

    companion object {
        fun fromCommand(
            command: Command,
            delayNewLevelAnimation: Long = NEW_LEVEL_ANIMATION_DURATION,
            perkSelectionDuration: Long = PERK_SELECTION_ANIMATION_DURATION,
            movementDuration: Long = IN_BETWEEN_MOVEMENT_DURATION,
            prestigeLevelUpDuration: Long = PRESTIGE_LEVEL_UP_DURATION,
        ): ExecutionMode {
            return when (command.mode) {
                1 -> SimpleExecutionMode(
                    levels = command.levels,
                    delayNewLevelAnimation = delayNewLevelAnimation,
                    perkSelectionDuration = perkSelectionDuration,
                    movementDuration = movementDuration,
                    prestigeLevelUpDuration = prestigeLevelUpDuration,
                    detector = SimpleDetector(),
                )
                else -> TestExecutionMode()
            }
        }
    }
}