package presentation.compose

import Constants.APP_ICON_PATH
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashWindow(
    onSplashCompleted: () -> Unit,
    visible: Boolean
) {
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch(Dispatchers.IO) {
            delay(100)
            onSplashCompleted()
        }
    }

    Window(
        onCloseRequest = {},
        state = WindowState(width = 200.dp, height = 100.dp, position = WindowPosition(Alignment.Center)),
        resizable = false,
        title = Constants.APP_NAME,
        icon = painterResource(APP_ICON_PATH),
        undecorated = true,
        visible = visible,
        alwaysOnTop = true,

        ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column {
                Text("Blood web")
                Text("pumpum")
            }
        }
    }
}