package helper

import Constants
import Constants.PRESTIGE_REWARD_FADE_IN_DURATION
import Constants.PRESTIGE_REWARD_FADE_OUT_DURATION
import Constants.SCREEN_SHOT_HEIGHT
import Constants.SCREEN_SHOT_WIDTH
import blood_web.Node
import java.awt.Rectangle
import java.awt.Robot
import java.awt.event.InputEvent
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import javax.swing.JFileChooser


class ClickHelper(
    private val robot: Robot,
    private val delayBetweenPerksSelection: Long,
    private val perkSelectionDuration: Long,
    private val prestigeLevelUpDuration: Long,
) {

    fun performClickOnPerk(
        node: Node,
        mask: Int = InputEvent.BUTTON1_DOWN_MASK,
    ) = with(robot) {
        mouseMove(node.centerCoordinates.x, node.centerCoordinates.y)
        Thread.sleep(delayBetweenPerksSelection)
        mousePress(mask)
        Thread.sleep(perkSelectionDuration)
        mouseRelease(mask)

        //await in corner
        moveOutCursor()
        Thread.sleep(delayBetweenPerksSelection)
    }

    fun upgradeAndSkipPrestigeLevel() {
        upgradePrestigeLevel()
        //todo think about displaying prestige rewards. Perhaps the are displayed only on 1-3 prestige
        Thread.sleep(PRESTIGE_REWARD_FADE_IN_DURATION)
        skipPrestigeRewardsPopUp()
        //awaiting fade animation
        Thread.sleep(PRESTIGE_REWARD_FADE_OUT_DURATION)
    }

    fun upgradePrestigeLevel() = with(robot) {
        mouseMove(Constants.BLOOD_WEB_CENTER_X, Constants.BLOOD_WEB_CENTER_Y)
        Thread.sleep(delayBetweenPerksSelection)
        mousePress(InputEvent.BUTTON1_DOWN_MASK)
        Thread.sleep(prestigeLevelUpDuration)
        mouseRelease(InputEvent.BUTTON1_DOWN_MASK)
    }

    fun skipPrestigeRewardsPopUp() = with(robot) {
        mouseMove(Constants.BLOOD_WEB_CENTER_X, Constants.BLOOD_WEB_CENTER_Y)
        Thread.sleep(delayBetweenPerksSelection)
        mousePress(InputEvent.BUTTON1_DOWN_MASK)
        Thread.sleep(delayBetweenPerksSelection)
        mouseRelease(InputEvent.BUTTON1_DOWN_MASK)
        moveOutCursor()
        Thread.sleep(delayBetweenPerksSelection)
    }

    fun performClick(
        mask: Int = InputEvent.BUTTON1_DOWN_MASK,
        duration: Long
    ) = with(robot) {
        mousePress(mask)
        Thread.sleep(duration)
        mouseRelease(mask)
    }

    fun moveOutCursor() {
        robot.mouseMove(10, 10)
    }

    fun takeScreenShot(
        x: Int = 0,
        y: Int = 0,
        width: Int = SCREEN_SHOT_WIDTH,
        height: Int = SCREEN_SHOT_HEIGHT,
    ): BufferedImage {
        val rect = Rectangle(x, y, width, height)
        return robot.createScreenCapture(rect)
    }

    fun saveScreenShot(
        screenShot: BufferedImage,
        fileName: String = Constants.DEFAULT_LOG_FILE_NAME
    ) {
        val docsDirectory = JFileChooser().fileSystemView.defaultDirectory.toString()
        val fileSeparator = System.getProperty(Constants.FILE_SEPARATOR_PROPERTY)
        val absoluteFilePath = docsDirectory + fileSeparator + fileName
        val file = File(absoluteFilePath)
        ImageIO.write(screenShot, "png", file)
    }
}