package launch_mode

import Constants.EXECUTOR_KEY
import Constants.STOP_KEY
import execution_mode.ExecutionMode
import helper.Command
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import launch_mode.controllers.MainController
import org.jnativehook.GlobalScreen
import org.jnativehook.NativeHookException
import org.jnativehook.keyboard.NativeKeyEvent
import org.jnativehook.keyboard.NativeKeyListener


class JavaFxLauncher : AppLauncher, Application() {

    override fun run() {
        Application.launch();
    }

    override fun start(stage: Stage) {

        val layout = "/scenes/main.fxml"

        val fxmlLoader = FXMLLoader()
        val scene: Scene = Scene(fxmlLoader.load<Parent?>(javaClass.getResource(layout).openStream()))
        val mainController: MainController = fxmlLoader.getController() as MainController
        stage?.scene = scene

        stage.title = "Testing UI for BP"
        stage.isResizable = false

        stage.show()


//        scene.onKeyPressed = object : EventHandler {
//            fun handle(event: KeyEvent) {
//                when (event.getCode()) {
//                    UP -> goNorth = true
//                    DOWN -> goSouth = true
//                    LEFT -> goWest = true
//                    RIGHT -> goEast = true
//                    SHIFT -> running = true
//                }
//            }
//
//
//        }


        try {
            GlobalScreen.registerNativeHook()
            var executor: ExecutionMode? = null

            GlobalScreen.addNativeKeyListener(object : NativeKeyListener {
                override fun nativeKeyTyped(nativeEvent: NativeKeyEvent?) {}
                override fun nativeKeyReleased(nativeEvent: NativeKeyEvent) {
                    val keyText: String = NativeKeyEvent.getKeyText(nativeEvent.keyCode)
                    if (keyText == EXECUTOR_KEY) {
                        val command = Command(1, mainController.levelsTextField.text.toInt())
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
    }

    override fun stop() {
        GlobalScreen.unregisterNativeHook()
        println("Stage is closing")
        // Save file
    }
}