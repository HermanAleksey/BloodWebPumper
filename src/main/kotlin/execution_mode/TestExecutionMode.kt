package execution_mode

import detector.TestDetector
import helper.sendLog
import kotlinx.coroutines.delay

class TestExecutionMode : ExecutionMode(TestDetector(), 0, 0, 0, 0) {

    override suspend fun pumpBloodWeb()  {
        for (i in 0..1000) {
            delay(300)
            sendLog("pumping blood web: $i")
        }
    }
}