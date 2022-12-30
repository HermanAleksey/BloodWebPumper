package presentation.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import helper.executionLogs
import kotlinx.coroutines.flow.map

var buffer = StringBuilder("")
val logsFlow = executionLogs.map {
    buffer.append(it + "\n")
    buffer.toString()
}

@Composable
fun LogsField(modifier: Modifier = Modifier) {
    val buffer by remember { mutableStateOf(StringBuilder("")) }

//    CoroutineScope(Dispatchers.Main).launch {
//        executionLogs.onEach {
//            try {
//                buffer.value.append(it + "\n")
//                buffer.toString()
//            }catch (e:java.lang.Exception) {
//                println(e)
//                ""
//            }
//        }.collect()
//    }

//    LaunchedEffect(a) {
//        println("a:$a")
//        buffer.appendLine(a)
//        buffer.toString()
//    }

    val logs = executionLogs.collectAsState()

    //todo try to use LazyColumn
    Card(
        modifier = modifier,
        elevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                modifier = Modifier
                    .fillMaxSize(),
                text = logs.value,
            )
        }
    }
}