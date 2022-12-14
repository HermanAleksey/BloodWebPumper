import launch_mode.AppLauncher
import launch_mode.ConsoleLauncher
import launch_mode.TestLauncher
import org.jnativehook.GlobalScreen
import java.util.logging.Level
import java.util.logging.Logger

fun main() {
    turnOffLogs()
    val appLauncher: AppLauncher = TestLauncher()
    appLauncher.run()
}
private fun turnOffLogs() {
    val logger: Logger = Logger.getLogger(GlobalScreen::class.java.getPackage().name)
    logger.level = Level.OFF
    for (handler in Logger.getLogger("").handlers) handler.level = Level.OFF
}

//tesseract implementation
//experiments with text recognition
//    val bw = blood_web.BloodWeb()
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