package helper

import Constants
import Constants.PRESTIGE_REWARD_FADE_IN_DURATION
import Constants.PRESTIGE_REWARD_FADE_OUT_DURATION
import blood_web.Node
import kotlinx.coroutines.delay
import java.awt.Robot
import java.awt.event.InputEvent


class ClickHelper(
    private val robot: Robot,
    private val delayBetweenPerksSelection: Long,
    private val perkSelectionDuration: Long,
    private val prestigeLevelUpDuration: Long,
) {

    suspend fun performClickOnNode(
        node: Node,
        mask: Int = InputEvent.BUTTON1_DOWN_MASK,
    ) = with(robot) {
        mouseMove(node.centerCoordinates.x, node.centerCoordinates.y)
        delay(delayBetweenPerksSelection)

        mousePress(mask)
        delay(perkSelectionDuration)

        mouseRelease(mask)

        //await in corner
        moveOutCursor()
        delay(delayBetweenPerksSelection)
    }

    suspend fun upgradePrestigeLevel() = with(robot) {
        mouseMove(Constants.BLOOD_WEB_CENTER_X, Constants.BLOOD_WEB_CENTER_Y)
        delay(delayBetweenPerksSelection)

        mousePress(InputEvent.BUTTON1_DOWN_MASK)
        delay(prestigeLevelUpDuration)

        mouseRelease(InputEvent.BUTTON1_DOWN_MASK)
        delay(PRESTIGE_REWARD_FADE_IN_DURATION)
    }

    suspend fun skipNotification() = with(robot) {
        mouseMove(Constants.BLOOD_WEB_CENTER_X, Constants.BLOOD_WEB_CENTER_Y)
        delay(delayBetweenPerksSelection)

        mousePress(InputEvent.BUTTON1_DOWN_MASK)
        delay(delayBetweenPerksSelection)

        mouseRelease(InputEvent.BUTTON1_DOWN_MASK)
        moveOutCursor()
        delay(PRESTIGE_REWARD_FADE_OUT_DURATION)
    }

    fun moveOutCursor() {
        robot.mouseMove(10, 10)
    }
}