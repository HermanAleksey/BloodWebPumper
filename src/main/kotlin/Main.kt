import launch_modes.AppLauncher
import launch_modes.ConsoleLauncher
import launch_modes.JavaFxLauncher
import org.jnativehook.GlobalScreen
import java.util.logging.Level
import java.util.logging.Logger

fun main() {
    turnOffLogs()
    val appLauncher: AppLauncher = JavaFxLauncher()
    appLauncher.run()

    //experiments with text recognition
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
private fun turnOffLogs() {
    val logger: Logger = Logger.getLogger(GlobalScreen::class.java.getPackage().name)
    logger.level = Level.OFF
    for (handler in Logger.getLogger("").handlers) handler.level = Level.OFF
}