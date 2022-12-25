package execution_mode

import detector.TestDetector
import kotlinx.coroutines.*
import executionLogs

class TestExecutionMode : ExecutionMode(TestDetector(), 0, 0, 0, 0) {

    override suspend fun pumpBloodWeb()  {
        for (i in 0..1000) {
            delay(300)
            executionLogs.emit("pumping blood web: $i")
        }
    }
}