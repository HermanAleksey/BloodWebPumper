package launch_mode.controllers;

import javafx.beans.value.ObservableValue
import javafx.fxml.FXML
import javafx.scene.control.RadioButton
import javafx.scene.control.TextField
import javafx.scene.control.Toggle
import javafx.scene.control.ToggleGroup
import javafx.scene.image.ImageView


public class MainController {

    @FXML
    lateinit var levelsTextField: TextField
    lateinit var rbModeTest: RadioButton
    lateinit var rbModeSimple: RadioButton
    lateinit var rbModeEco: RadioButton
    lateinit var rbModeNonEco: RadioButton
    lateinit var runningModeGroup: ToggleGroup
    lateinit var imageBgRight: ImageView


    fun changed(changed: ObservableValue<out Toggle?>?, oldValue: Toggle?, newValue: Toggle) {

        // получаем выбранный элемент RadioButton
        val selectedBtn = newValue as RadioButton
        println("Selected: " + selectedBtn.text)
    }

    fun stop(event: javafx.event.ActionEvent) {
        //TODO stop logic
    }

    fun deleteLogs(event: javafx.event.ActionEvent){
        println("Selected: " + runningModeGroup.selectedToggle.toString())
    }
}
