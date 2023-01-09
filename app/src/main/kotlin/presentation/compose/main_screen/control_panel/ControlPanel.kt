package presentation.compose.main_screen.control_panel

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import presentation.compose.main_screen.MainScreenViewModel

@Composable
fun ControlPanel(
    modifier: Modifier = Modifier,
    viewModel: MainScreenViewModel,
) {
    val levelsToUpgrade = viewModel.levelsToPumpAmount.collectAsState()

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

        ModesSelect(setSelectedMode = {
            viewModel.onExecutionModeSelected(it)
        })

        LevelSelect(
            levelsToUpgrade = levelsToUpgrade.value,
            setLevelsToUpgrade = {
                viewModel.onLevelToPumpUpdated(it)
            }
        )

        HelperCard()

        Row {
            Button(onClick = {
                viewModel.onCloseLogsClick()
            }) {
                Text("Close logs")
            }

            Spacer(modifier = Modifier.width(48.dp))

            Button(onClick = {
                viewModel.onOpenLogsClick()
            }) {
                Text("Open logs")
            }
        }
    }
}