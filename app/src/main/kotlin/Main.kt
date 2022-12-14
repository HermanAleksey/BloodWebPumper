import launch_mode.AppLauncher
import launch_mode.JetpackComposeLauncher
import org.jnativehook.GlobalScreen
import java.util.logging.Level
import java.util.logging.Logger

fun main() {
    turnOffLogs()
    val appLauncher: AppLauncher = JetpackComposeLauncher()
    appLauncher.run()
}

private fun turnOffLogs() {
    val logger: Logger = Logger.getLogger(GlobalScreen::class.java.getPackage().name)
    logger.level = Level.OFF
    for (handler in Logger.getLogger("").handlers) handler.level = Level.OFF
}