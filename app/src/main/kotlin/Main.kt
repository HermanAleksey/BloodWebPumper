import kotlinx.coroutines.flow.MutableStateFlow
import launch_mode.AppLauncher
import launch_mode.JavaFxLauncher
import org.jnativehook.GlobalScreen
import java.util.logging.Level
import java.util.logging.Logger


val executionLogs =  MutableStateFlow("")

fun main() {
    turnOffLogs()
    val appLauncher: AppLauncher = JavaFxLauncher()
    appLauncher.run()
}

private fun turnOffLogs() {
    val logger: Logger = Logger.getLogger(GlobalScreen::class.java.getPackage().name)
    logger.level = Level.OFF
    for (handler in Logger.getLogger("").handlers) handler.level = Level.OFF
}