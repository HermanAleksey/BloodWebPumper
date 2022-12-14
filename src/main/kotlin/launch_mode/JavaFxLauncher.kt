package launch_mode

import javafx.application.Application
import javafx.fxml.FXMLLoader.load
import javafx.scene.Scene
import javafx.stage.Stage
import javafx.scene.Parent;


class JavaFxLauncher : AppLauncher, Application() {

    override fun run() {
        Application.launch();
    }

    override fun start(stage: Stage) {

        val layout = "/scenes/main.fxml"

//        val a = getResource(layout)

//        println(a)

        stage?.scene = Scene(load<Parent?>(javaClass.getResource(layout)))

//        val root: Parent = FXMLLoader.load(a)
//
//        val scene = Scene(root)
//        stage.scene = scene
        stage.title = "Testing UI for BP"
//        stage.width = 300.0
//        stage.height = 250.0
//
//
        stage.show()
    }
}