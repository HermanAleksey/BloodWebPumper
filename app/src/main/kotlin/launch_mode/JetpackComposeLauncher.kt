package launch_mode

import Constants.APP_ICON_PATH
import Constants.EXECUTOR_KEY
import Constants.STOP_KEY
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import execution_mode.ExecutionMode
import helper.Command
import org.jnativehook.GlobalScreen
import org.jnativehook.NativeHookException
import org.jnativehook.keyboard.NativeKeyEvent
import org.jnativehook.keyboard.NativeKeyListener
import presentation.compose.LogsField
import presentation.compose.control_panel.ControlPanel

class JetpackComposeLauncher : AppLauncher {

    override fun run() {
        application {
            Window(
                onCloseRequest = ::exitApplication,
                state = WindowState(width = 1000.dp, height = 700.dp),
                resizable = false,
                title = Constants.APP_NAME,
                icon = painterResource(APP_ICON_PATH)
            ) {
                MaterialTheme {
                    App()
                }
            }
        }
    }

    @Composable
    fun App() {
        val (selectedMode, setSelectedMode) = remember { mutableStateOf(Command.Mode.TEST) }
        val (levelsToUpgrade, setLevelsToUpgrade) = remember { mutableStateOf(0) }
        var executor: ExecutionMode? by remember { mutableStateOf(null) }
        var executeCommand by remember { mutableStateOf(Command(Command.Mode.TEST, 0)) }

        LaunchedEffect(key1 = selectedMode, key2 = levelsToUpgrade) {
            executeCommand = Command(selectedMode, levelsToUpgrade)
        }

        LaunchedEffect(Unit) {
            println("Start hook")
            try {
                GlobalScreen.registerNativeHook()

                GlobalScreen.addNativeKeyListener(object : NativeKeyListener {
                    override fun nativeKeyTyped(nativeEvent: NativeKeyEvent?) {}
                    override fun nativeKeyReleased(nativeEvent: NativeKeyEvent) {
                        val keyText: String = NativeKeyEvent.getKeyText(nativeEvent.keyCode)
                        println(keyText)

                        when (keyText) {
                            EXECUTOR_KEY -> {
                                executeCommand.let {
                                    println("Execute command: $it")
                                    executor = ExecutionMode.fromCommand(command = it)
                                    executor?.run()
                                }
                            }

                            STOP_KEY -> {
                                executor?.stop()
                                executor = null
                            }
                        }
                    }

                    override fun nativeKeyPressed(nativeEvent: NativeKeyEvent?) {}
                })
            } catch (e: NativeHookException) {
                e.printStackTrace()
            }
        }

        DisposableEffect(Unit) {
            onDispose {
                println("Unregister hook")
                GlobalScreen.unregisterNativeHook()
            }
        }

        Row(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxSize(),
        ) {
            LogsField(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.5f)
            )
            Spacer(modifier = Modifier.width(24.dp))
            ControlPanel(
                modifier = Modifier.fillMaxSize(),
                setSelectedMode,
                levelsToUpgrade,
                setLevelsToUpgrade
            )
        }
    }
}