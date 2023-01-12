package presentation.compose

import execution_mode.ExecutionMode
import helper.Command
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class AppViewModel {

    private val _logFieldIsVisible: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val logFieldIsVisible = _logFieldIsVisible.asStateFlow()

    val windowWidth = _logFieldIsVisible.map { visible ->
        if (visible) 1000 else 450
    }

    private val _selectedExecutionMode: MutableStateFlow<Command.Mode> = MutableStateFlow(Command.Mode.TEST)
    private val selectedExecutionMode = _selectedExecutionMode.asStateFlow()

    private val _levelsToPumpAmount: MutableStateFlow<Int> = MutableStateFlow(0)
    val levelsToPumpAmount = _levelsToPumpAmount.asStateFlow()

    private val _helpWindowIsVisible = MutableStateFlow(false)
    val helpWindowIsVisible = _helpWindowIsVisible.asStateFlow()


    fun onCloseLogsClick() {
        _logFieldIsVisible.value = false
    }

    fun onOpenLogsClick() {
        _logFieldIsVisible.value = true
    }

    var executor: ExecutionMode? = null
    fun onExecutorKeyPressed() {
        executor = ExecutionMode.fromCommand(
            Command(selectedExecutionMode.value, levelsToPumpAmount.value)
        )
        executor?.run()
    }

    fun onStopExecutionKeyPressed() {
        executor?.stop()
        executor = null
    }

    fun onExecutionModeSelected(mode: Command.Mode) {
        _selectedExecutionMode.value = mode
    }

    fun onLevelToPumpUpdated(value: Int) {
        _levelsToPumpAmount.value = value
    }

    fun onHelpClick() {
        _helpWindowIsVisible.value = true
    }

    fun onHelpWindowCloseClick() {
        _helpWindowIsVisible.value = false
    }
}