import execution_mode.ExecutionMode
import org.jnativehook.GlobalScreen
import org.jnativehook.NativeHookException
import org.jnativehook.keyboard.NativeKeyEvent
import org.jnativehook.keyboard.NativeKeyListener
import java.util.logging.Level
import java.util.logging.Logger


fun main() {
    turnOffLogs()
    run()
//    val bw = BloodWeb()
//    val bfIm: BufferedImage = ImageIO.read(File("bw_center.png"))
//    println(bw.checkPrestigeUpgrade(bfIm))

//    val imageFile = File("BloodWebScreenShot.png")
//    val instance: ITesseract = Tesseract() // JNA Interface Mapping
//
//    // ITesseract instance = new Tesseract1(); // JNA Direct Mapping
//    instance.setDatapath("P:\\tessdata") // path to tessdata directory
//
//    try {
//        val result: String = instance.doOCR(imageFile)
//        println(result)
//    } catch (e: TesseractException) {
//        System.err.println(e.message)
//    }
}

/**
 * F4 - is a trigger for starting operations.
 * after input of F4 program process inputted values
 * and run in configured mode
 *
 * Mode contains arguments: running mode, amount of levels to upgrade
 * in form of:
 * mXlYYF4
 * where X - is INT execution mode
 * Y - is INT number of levels to upgrade
 *
 * F5 press stops the program
 * */
private fun run(): Nothing {
    try {
        GlobalScreen.registerNativeHook()
        val commandProcessor = CommandProcessor()
        val stringBuffer = StringBuffer()
        var executor: ExecutionMode? = null

        GlobalScreen.addNativeKeyListener(object : NativeKeyListener {
            override fun nativeKeyTyped(nativeEvent: NativeKeyEvent?) {}
            override fun nativeKeyReleased(nativeEvent: NativeKeyEvent) {
                val keyText: String = NativeKeyEvent.getKeyText(nativeEvent.keyCode)
                stringBuffer.append(keyText)
                if (keyText == EXECUTOR_KEY) {
                    val command = commandProcessor.processStringCommand(stringBuffer.toString())
                    stringBuffer.delete(0, stringBuffer.length - 1)
                    command?.let {
                        executor = ExecutionMode.fromCommand(command = it)
                        executor?.run()
                    }
                }
                if (keyText == STOP_KEY) {
                    executor?.stop()
                }
            }

            override fun nativeKeyPressed(nativeEvent: NativeKeyEvent?) {}
        })
    } catch (e: NativeHookException) {
        e.printStackTrace()
    }

    while (true) {
    }
}

private fun turnOffLogs() {
    val logger: Logger = Logger.getLogger(GlobalScreen::class.java.getPackage().name)
    logger.level = Level.OFF
    for (handler in Logger.getLogger("").handlers) handler.level = Level.OFF
}

const val EXECUTOR_KEY = "F4"
const val STOP_KEY = "F5"