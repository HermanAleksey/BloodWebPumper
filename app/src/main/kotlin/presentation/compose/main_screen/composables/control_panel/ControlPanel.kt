package presentation.compose.main_screen.composables.control_panel

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import presentation.compose.AppViewModel

@Composable
fun ControlPanel(
    modifier: Modifier = Modifier,
    viewModel: AppViewModel,
) {
    val levelsToUpgrade = viewModel.levelsToPumpAmount.collectAsState()
    val selectedExecutionMode = viewModel.selectedExecutionMode.collectAsState()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(48.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "BloodWeb upgrade",
                style = TextStyle(fontSize = 24.sp)
            )
        }

        ModesSelect(
            onModeSelected = {
                  viewModel.onExecutionModeSelected(it)
            },
            selectedMode = selectedExecutionMode.value
        )

        LevelSelect(
            levelsToUpgrade = levelsToUpgrade.value,
            setLevelsToUpgrade = {
                viewModel.onLevelToPumpUpdated(it)
            }
        )

        HelperCard()

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                viewModel.onCloseLogsClick()
            }) {
                Text("Close logs")
            }

            Button(onClick = {
                viewModel.onOpenLogsClick()
            }) {
                Text("Open logs")
            }

            Button(onClick = {
                viewModel.onHelpClick()
            }) {
                Text("Help")
            }
        }
    }
}