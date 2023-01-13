package presentation.compose.main_screen.composables.control_panel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import presentation.compose.AppViewModel
import presentation.compose.theming.Colors

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
            Button(
                onClick = {
                    viewModel.onCloseLogsClick()
                },
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    backgroundColor = Colors.BLOOD_RED
                )
            ) {
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