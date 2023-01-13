package presentation.compose.main_screen.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import helper.clearLog
import helper.executionLogs
import kotlinx.coroutines.launch
import presentation.compose.theming.Dimensions
import java.awt.Toolkit
import java.awt.datatransfer.Clipboard

import java.awt.datatransfer.StringSelection




@Composable
fun LogsField(modifier: Modifier = Modifier) {
    val logs = executionLogs.collectAsState()
    val scope = rememberCoroutineScope()

    val logState = rememberScrollState(0)

    LaunchedEffect(logs.value) {
        logState.scrollTo(logState.maxValue)
    }


    Column(modifier = modifier) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(bottom = 24.dp)
                .clip(RoundedCornerShape(Dimensions.CARD_ROUNDED_CORNER)),
            backgroundColor = Color(137, 131, 131),
            elevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxSize()
                    .verticalScroll(logState),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxSize(),
                    text = logs.value,
                )
            }
        }

        Row (modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = {
                    scope.launch {
                        clearLog()
                    }
                }
            ) {
                Text("Clear log")
            }
            Spacer(modifier = Modifier.width(48.dp))
            Button(
                onClick = {
                    scope.launch {
                        logs.value.copyToClipboard()
                    }
                }
            ) {
                Text("Copy log")
            }
        }

    }
}

fun String.copyToClipboard(){
    val selection = StringSelection(this)
    val clipboard: Clipboard = Toolkit.getDefaultToolkit().systemClipboard
    clipboard.setContents(selection, selection)
}