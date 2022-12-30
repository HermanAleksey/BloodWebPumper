package launch_mode

import Constants.APP_ICON_PATH
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import executionLogs
import helper.convertIntoBufferImage
import javafx.scene.image.Image
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File

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
                App()
            }
        }
    }

    @Composable
    fun App() {
        MaterialTheme {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                LogsField()
                ContollPanel()
            }
        }
    }

    @Composable
    fun LogsField() {
        var logsFieldText by remember { mutableStateOf("") }

        CoroutineScope(Dispatchers.Main).launch {
            executionLogs.collect {
//                logsFieldText = (it)
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            TextField(
                value = logsFieldText,
                onValueChange = { logsFieldText = it}
            )
        }
    }

    @Composable
    fun ContollPanel(){
        Box(modifier = Modifier.fillMaxSize()) {
//            TextField(
//                value = logsFieldText,
//                onValueChange = { logsFieldText = it}
//            )
        }
    }
}