package launch_mode

import Constants.EXECUTOR_KEY
import Constants.STOP_KEY
import controller.MainController
import execution_mode.ExecutionMode
import helper.Command
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.RadioButton
import javafx.scene.image.Image
import javafx.stage.Stage
import kotlinx.coroutines.*
import org.jnativehook.GlobalScreen
import org.jnativehook.NativeHookException
import org.jnativehook.keyboard.NativeKeyEvent
import org.jnativehook.keyboard.NativeKeyListener
import executionLogs


class JavaFxLauncher : AppLauncher, Application() {

    private val mainScope = CoroutineScope(Dispatchers.Main)

    override fun run() {
        launch()
    }

    override fun start(stage: Stage) {
        mainScope.launch {
            val fxmlLoader = FXMLLoader()
            val mainScene = withContext(Dispatchers.IO) {
                Scene(
                    fxmlLoader.load(
                        javaClass.getResource("/scenes/main.fxml")!!.openStream()
                    )
                )
            }.apply {
                stylesheets.add("/style.css")
            }

            val mainController: MainController = fxmlLoader.getController() as MainController

            stage.apply {
                scene = mainScene
                title = "Bloodweb autoleveling"
                isResizable = false
                icons.add(Image(javaClass.getResourceAsStream("/scenes/drawables/bubba.jpg")))

                show()
            }

            try {
                launch(Dispatchers.IO) {
                    executionLogs.collect {
                        mainController.appendLogText(it)
                    }
                }

                GlobalScreen.registerNativeHook()
                var executor: ExecutionMode? = null

                GlobalScreen.addNativeKeyListener(object : NativeKeyListener {
                    override fun nativeKeyTyped(nativeEvent: NativeKeyEvent?) {}
                    override fun nativeKeyReleased(nativeEvent: NativeKeyEvent) {
                        val keyText: String = NativeKeyEvent.getKeyText(nativeEvent.keyCode)

                        when (keyText) {
                            EXECUTOR_KEY -> {
                                //if we already have one executor running - we don't want to launch another one
                                if (executor != null) return
                                val mode: Int =
                                    when ((mainController.runningModeGroup.selectedToggle as? RadioButton)?.id) {
                                        mainController.rbModeTest.id -> 0
                                        mainController.rbModeSimple.id -> 1
                                        mainController.rbModeEco.id -> 2
                                        mainController.rbModeNonEco.id -> 3
                                        else -> 0
                                    }

                                Command(mode, mainController.levelsTextField.text.toInt()).let {
                                    executor = ExecutionMode.fromCommand(command = it)
                                    executor?.run()
                                }
                            }

                            STOP_KEY -> {
                                executor?.stop()
                                executor = null
                            }
                        }
                    }

                    override fun nativeKeyPressed(nativeEvent: NativeKeyEvent?) {}
                })
            } catch (e: NativeHookException) {
                e.printStackTrace()
            }
        }
    }

    override fun stop() {
        GlobalScreen.unregisterNativeHook()
        println("Stage is closing")
    }
}