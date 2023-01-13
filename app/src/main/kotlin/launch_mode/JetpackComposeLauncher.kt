package launch_mode

import Constants
import Constants.APP_ICON_PATH
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import presentation.compose.AppViewModel
import presentation.compose.HelpWindow
import presentation.compose.SplashWindow
import presentation.compose.main_screen.MainWindow

class JetpackComposeLauncher : AppLauncher {
    override fun run() {
        application {
            val viewModel = remember { AppViewModel() }
            var isSplashWindowVisible by remember { mutableStateOf(true) }
            var isMainWindowVisible by remember { mutableStateOf(false) }

            SplashWindow(
                visible = isSplashWindowVisible,
                onSplashCompleted = {
                    isSplashWindowVisible = false
                    isMainWindowVisible = true
                }
            )

            if (isMainWindowVisible) {
                MainWindow(viewModel, ::exitApplication)
            }

            HelpWindow(viewModel)
        }
    }
}