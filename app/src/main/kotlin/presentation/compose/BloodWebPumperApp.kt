package presentation.compose

import Constants.EXECUTOR_KEY
import Constants.STOP_KEY
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import execution_mode.ExecutionMode
import helper.Command
import org.jnativehook.GlobalScreen
import org.jnativehook.NativeHookException
import org.jnativehook.keyboard.NativeKeyEvent
import org.jnativehook.keyboard.NativeKeyListener
import presentation.compose.main_screen.MainScreenViewModel
import presentation.compose.main_screen.control_panel.ControlPanel

@Composable
fun BloodWebPumperApp(viewModel: MainScreenViewModel) {
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

    val logsFieldIsVisible  = viewModel.logFieldIsVisible.collectAsState()//by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxSize(),
    ) {
        if (logsFieldIsVisible.value) {
            LogsField(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.5f)
            )
            Spacer(modifier = Modifier.width(24.dp))
        }
        ControlPanel(
            modifier = Modifier.fillMaxSize(),
            viewModel = viewModel,
            setSelectedMode,
            levelsToUpgrade,
            setLevelsToUpgrade
        )
    }
}