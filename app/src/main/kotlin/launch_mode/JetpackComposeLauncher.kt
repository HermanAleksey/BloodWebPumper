package launch_mode

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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