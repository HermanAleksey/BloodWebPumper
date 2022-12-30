package presentation.compose.control_panel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import helper.Command


@Composable
fun ModesSelect(
    setSelectedMode: (Command.Mode) -> Unit
) {
    var testButtonSelected by remember { mutableStateOf(true) }
    var simpleButtonSelected by remember { mutableStateOf(false) }
    var ecoButtonSelected by remember { mutableStateOf(false) }

    val selectOption: (Command.Mode) -> Unit = { option ->
        testButtonSelected = false
        simpleButtonSelected = false
        ecoButtonSelected = false

        when (option) {
            Command.Mode.TEST -> {
                setSelectedMode(Command.Mode.TEST)
                testButtonSelected = true
            }
            Command.Mode.SIMPLE -> {
                setSelectedMode(Command.Mode.SIMPLE)
                simpleButtonSelected = true
            }
            Command.Mode.CHEAPEST_FIRST -> {
                setSelectedMode(Command.Mode.CHEAPEST_FIRST)
                ecoButtonSelected = true
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
                enabled = !ecoButtonSelected,
                onClick = {
                    selectOption(Command.Mode.CHEAPEST_FIRST)
                }
            ) {
                Text("Eco")
            }
        }
    }
}