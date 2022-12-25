package launch_mode

import Constants.EXECUTOR_KEY
import Constants.STOP_KEY
import execution_mode.ExecutionMode
import helper.Command
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.RadioButton
import javafx.scene.image.Image
import javafx.stage.Stage
import launch_mode.controllers.MainController
import org.jnativehook.GlobalScreen
import org.jnativehook.NativeHookException
import org.jnativehook.keyboard.NativeKeyEvent
import org.jnativehook.keyboard.NativeKeyListener


class JavaFxLauncher : AppLauncher, Application() {

    override fun run() {
        launch()
    }

    override fun start(stage: Stage) {
        val fxmlLoader = FXMLLoader()
        val scene = Scene(fxmlLoader.load(javaClass.getResource("/scenes/main.fxml")!!.openStream()))
        val mainController: MainController = fxmlLoader.getController() as MainController

        scene.stylesheets.add("/style.css")
        stage.scene = scene

        stage.title = "Bloodweb autoleveling"
        stage.isResizable = false
        stage.icons.add(Image(javaClass.getResourceAsStream("/scenes/drawables/bubba.jpg")))

//        mainController.textAreaLogs.sty

        stage.show()

        try {
            GlobalScreen.registerNativeHook()
            var executor: ExecutionMode? = null

            GlobalScreen.addNativeKeyListener(object : NativeKeyListener {
                override fun nativeKeyTyped(nativeEvent: NativeKeyEvent?) {}
                override fun nativeKeyReleased(nativeEvent: NativeKeyEvent) {
                    val keyText: String = NativeKeyEvent.getKeyText(nativeEvent.keyCode)
                    if (keyText == EXECUTOR_KEY) {

                        val mode: Int = when ((mainController.runningModeGroup.selectedToggle as? RadioButton)?.id) {
                            mainController.rbModeTest.id -> 0
                            mainController.rbModeSimple.id -> 1
                            mainController.rbModeEco.id -> 2
                            mainController.rbModeNonEco.id -> 3
                            else -> 0
                        }

                        val command = Command(mode, mainController.levelsTextField.text.toInt())
                        command.let {
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
    }

    override fun stop() {
        GlobalScreen.unregisterNativeHook()
        println("Stage is closing")
    }
}