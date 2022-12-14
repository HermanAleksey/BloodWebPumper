package execution_mode

import detectors.TestDetector

class TestExecutionMode : ExecutionMode(TestDetector(), 0, 0, 0) {

    override fun pumpBloodWeb() {
        fileHelper.appendFileLine("Running TextExecutionMode")
        for (i in 0..1000) {
            Thread.sleep(300)
            println("pumping blood web: $i")
            fileHelper.appendFileLine("pumping blood web: $i")
        }
        fileHelper.appendFileLine("TextExecutionMode completed")
    }
}