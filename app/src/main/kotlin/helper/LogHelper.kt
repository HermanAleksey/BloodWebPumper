package helper

import kotlinx.coroutines.flow.MutableStateFlow

val executionLogs = MutableStateFlow("")
suspend fun sendLog(log: String) {
    executionLogs.emit(executionLogs.value + "\n" + log)
}