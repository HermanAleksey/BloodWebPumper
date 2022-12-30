package helper

import executionLogs

suspend fun sendLog(log: String) {
    executionLogs.emit(log)
}