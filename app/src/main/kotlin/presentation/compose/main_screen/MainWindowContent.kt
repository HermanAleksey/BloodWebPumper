package presentation.compose.main_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import presentation.compose.AppViewModel
import presentation.compose.theming.Dimensions
import presentation.compose.main_screen.composables.LogsField
import presentation.compose.main_screen.composables.control_panel.ControlPanel


@Composable
fun MainWindowContent(viewModel: AppViewModel) {
    val logsFieldIsVisible = viewModel.logFieldIsVisible.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(
                RoundedCornerShape(
                    bottomStart = Dimensions.APP_WINDOW_CORNERS_SIZE,
                    bottomEnd = Dimensions.APP_WINDOW_CORNERS_SIZE
                )
            )
    ) {
        Image(
            painter = painterResource("/scenes/drawables/background.jpg"),
            contentDescription = null,
            contentScale = ContentScale.FillHeight,
            modifier = Modifier.fillMaxHeight()
        )

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
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
}