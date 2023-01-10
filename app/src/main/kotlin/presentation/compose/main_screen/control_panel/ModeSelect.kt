package presentation.compose.main_screen.control_panel

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import helper.Command

@Composable
fun ModesSelect(
    setSelectedMode: (Command.Mode) -> Unit
) {
    //todo rename
    var testButtonSelected by remember { mutableStateOf(true) }
    var simpleButtonSelected by remember { mutableStateOf(false) }
    var rarestButtonSelected by remember { mutableStateOf(false) }
    var newExecutorButtonSelected by remember { mutableStateOf(false) }

    val selectOption: (Command.Mode) -> Unit = { option ->
        testButtonSelected = false
        simpleButtonSelected = false
        rarestButtonSelected = false
        newExecutorButtonSelected = false

        when (option) {
            Command.Mode.TEST -> {
                setSelectedMode(Command.Mode.TEST)
                testButtonSelected = true
            }
            Command.Mode.SIMPLE -> {
                setSelectedMode(Command.Mode.SIMPLE)
                simpleButtonSelected = true
            }
            Command.Mode.RAREST_FIRST -> {
                setSelectedMode(Command.Mode.RAREST_FIRST)
                rarestButtonSelected = true
            }
            Command.Mode.NEW_EXECUTOR -> {
                setSelectedMode(Command.Mode.NEW_EXECUTOR)
                newExecutorButtonSelected = true
            }
        }
    }

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
                enabled = !testButtonSelected,
                onClick = {
                    selectOption(Command.Mode.TEST)
                }
            ) {
                Text("Test")
            }
            Button(
                enabled = !simpleButtonSelected,
                onClick = {
                    selectOption(Command.Mode.SIMPLE)
                }
            ) {
                Text("Simple")
            }
            Button(
                enabled = !rarestButtonSelected,
                onClick = {
                    selectOption(Command.Mode.RAREST_FIRST)
                }
            ) {
                Text("Rarest")
            }
            Button(
                enabled = !newExecutorButtonSelected,
                onClick = {
                    selectOption(Command.Mode.NEW_EXECUTOR)
                }
            ) {
                Text("NewOne")
            }
        }
    }
}