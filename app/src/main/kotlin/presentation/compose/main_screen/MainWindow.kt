package presentation.compose.main_screen

import Constants
import Constants.APP_ICON_PATH
import Constants.EXECUTOR_KEY
import Constants.STOP_KEY
import androidx.compose.foundation.layout.*
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import org.jnativehook.GlobalScreen
import org.jnativehook.NativeHookException
import org.jnativehook.keyboard.NativeKeyEvent
import org.jnativehook.keyboard.NativeKeyListener
import presentation.compose.AppViewModel
import presentation.compose.main_screen.composables.LogsField
import presentation.compose.main_screen.composables.control_panel.ControlPanel
import presentation.compose.theming.Colors.BLOOD_RED

@Composable
fun MainWindow(viewModel: AppViewModel, exitApplication: () -> Unit) {
    val windowsWidthDp = viewModel.windowWidth.collectAsState(450)
    Window(
        onCloseRequest = exitApplication,
        state = WindowState(width = windowsWidthDp.value.dp, height = 700.dp),
        resizable = false,
        icon = painterResource(APP_ICON_PATH),
        title = Constants.APP_NAME,
        undecorated = true,
        transparent = true
    ) {
        Column {
            WindowDraggableArea {
                MainToolbar(exitApplication)
            }
            MaterialTheme(
//                colors = Colors(
//                    primary = Color.White,
//                    primaryVariant = Color.White,
//                    secondary = Color.White,
//                    secondaryVariant = Color.White,
//                    background = BLOOD_RED,
//                    error = Color.White,
//                    isLight = false,
//                    surface = BLOOD_RED,
//                    onBackground = Color.White,
//                    onError = Color.White,
//                    onPrimary = Color.White,
//                    onSecondary = Color.White,
//                    onSurface = Color.White
//                )
            ) {
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

                MainWindowContent(viewModel)
            }
        }
    }
}