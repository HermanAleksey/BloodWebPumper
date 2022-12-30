package controller

import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.image.ImageView


class MainController {

    @FXML
    lateinit var levelsTextField: TextField
    lateinit var rbModeTest: RadioButton
    lateinit var rbModeSimple: RadioButton
    lateinit var rbModeEco: RadioButton
    lateinit var rbModeNonEco: RadioButton
    lateinit var runningModeGroup: ToggleGroup
    lateinit var textAreaLogs: TextArea
    lateinit var wholesomeBubbaImage: ImageView
    lateinit var wholesomeStalkImage: ImageView

    fun initialize() {
        textAreaLogs.text = ""
        wholesomeBubbaImage.image = Image(javaClass.getResourceAsStream("/scenes/drawables/bubba_run.gif"))
        wholesomeStalkImage.image = Image(javaClass.getResourceAsStream("/scenes/drawables/stalk.png"))
    }


    fun deleteLogs(event: javafx.event.ActionEvent) {
        textAreaLogs.text = ""
    }

    fun appendLogText(text: String) {
        textAreaLogs.appendText("$text\n")
    }
}
