package execution_mode

import BloodWeb
import ClickHelper
import Command
import logs.TextFileHelper
import java.awt.Robot

sealed class ExecutionMode(
    protected val delayNewLevelAnimation: Long,
    perkSelectionDuration: Long,
    movementDuration: Long,
) {

    private val robot = Robot()
    protected val clickHelper = ClickHelper(
        robot = robot,
        perkSelectionDuration = perkSelectionDuration,
        delayBetweenPerksSelection = movementDuration,
    )
    protected val fileHelper = TextFileHelper()
    protected val bloodWeb = BloodWeb()

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
            delayNewLevelAnimation: Long = 5500,
            perkSelectionDuration: Long = 800,
            movementDuration: Long = 300,
        ): ExecutionMode {
            return when (command.mode) {
                1 -> SimpleExecutionMode(
                    levels = command.levels,
                    delayNewLevelAnimation = delayNewLevelAnimation,
                    perkSelectionDuration = perkSelectionDuration,
                    movementDuration = movementDuration
                )
                else -> TestExecutionMode()
            }
        }
    }
}