package launch_mode

import Constants.APP_ICON_PATH
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import presentation.compose.main_screen.MainWindow
import presentation.compose.HelpWindow
import presentation.compose.AppViewModel

class JetpackComposeLauncher : AppLauncher {
    override fun run() {
        application {
            val viewModel = remember { AppViewModel() }
            val windowWidth = viewModel.windowWidth.collectAsState()
            println("windowWidth:$windowWidth")
            Window(
                onCloseRequest = ::exitApplication,
                state = WindowState(width = windowWidth.value.dp, height = 700.dp),
                resizable = false,
                title = Constants.APP_NAME,
                icon = painterResource(APP_ICON_PATH),
//                undecorated = true,/ remove line on top of app with close and hide options
            ) {
                MaterialTheme {
                    MainWindow(viewModel)
                }
            }

            HelpWindow(viewModel)
        }
    }
}