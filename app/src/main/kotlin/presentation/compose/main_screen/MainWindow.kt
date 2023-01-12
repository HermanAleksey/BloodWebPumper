package presentation.compose.main_screen

import Constants.EXECUTOR_KEY
import Constants.STOP_KEY
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jnativehook.GlobalScreen
import org.jnativehook.NativeHookException
import org.jnativehook.keyboard.NativeKeyEvent
import org.jnativehook.keyboard.NativeKeyListener
import presentation.compose.AppViewModel
import presentation.compose.main_screen.composables.LogsField
import presentation.compose.main_screen.composables.control_panel.ControlPanel

@Composable
fun MainWindow(viewModel: AppViewModel) {
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
                            viewModel.onExecutorKeyPressed()
                        }
                        STOP_KEY -> {
                            viewModel.onStopExecutionKeyPressed()
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

    val logsFieldIsVisible  = viewModel.logFieldIsVisible.collectAsState()

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
        )
    }
}