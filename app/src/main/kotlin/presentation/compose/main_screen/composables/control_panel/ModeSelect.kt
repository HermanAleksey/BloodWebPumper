package presentation.compose.main_screen.composables.control_panel

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import model.Command

@Composable
fun ModesSelect(
    selectedMode: Command.Mode,
    onModeSelected: (Command.Mode) -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth()
            .height(100.dp),
        elevation = 8.dp
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                enabled = selectedMode == Command.Mode.TEST,
                onClick = {
                    onModeSelected(Command.Mode.TEST)
                }
            ) {
                Text("Test")
            }
            Button(
                enabled = selectedMode == Command.Mode.SIMPLE,
                onClick = {
                    onModeSelected(Command.Mode.SIMPLE)
                }
            ) {
                Text("Simple")
            }
            Button(
                enabled = selectedMode == Command.Mode.RAREST_FIRST,
                onClick = {
                    onModeSelected(Command.Mode.RAREST_FIRST)
                }
            ) {
                Text("Rarest")
            }
            Button(
                enabled = selectedMode == Command.Mode.FURTHEST,
                onClick = {
                    onModeSelected(Command.Mode.FURTHEST)
                }
            ) {
                Text("Furthest")
            }
        }
    }
}