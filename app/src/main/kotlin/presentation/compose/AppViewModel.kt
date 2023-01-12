package presentation.compose

import execution_mode.ExecutionMode
import helper.preferences.Preferences
import helper.preferences.PreferencesData
import helper.preferences.PreferencesImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import model.Command
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppViewModel {

    private val appScope = CoroutineScope(Dispatchers.Default)
    private val preferences: Preferences = PreferencesImpl()

    private val _logFieldIsVisible: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val logFieldIsVisible = _logFieldIsVisible.asStateFlow()

    private val _windowWidth: MutableStateFlow<Int> = MutableStateFlow(450)
    val windowWidth = _windowWidth.asStateFlow()

    private val _selectedExecutionMode: MutableStateFlow<Command.Mode> = MutableStateFlow(Command.Mode.TEST)
    private val selectedExecutionMode = _selectedExecutionMode.asStateFlow()

    private val _levelsToPumpAmount: MutableStateFlow<Int> = MutableStateFlow(0)
    val levelsToPumpAmount = _levelsToPumpAmount.asStateFlow()

    private val _helpWindowIsVisible = MutableStateFlow(false)
    val helpWindowIsVisible = _helpWindowIsVisible.asStateFlow()

    init {
        appScope.launch {
            withContext(Dispatchers.IO) {
                preferences.getPreferences().let {
                    _logFieldIsVisible.value = it.isLogsOpen

                    _selectedExecutionMode.value = it.selectedMode
                    _levelsToPumpAmount.value = it.levelsToUpgradeAmount
                }
            }
        }
    }

    fun onCloseLogsClick() {
        setLogsVisibility(false)
    }

    fun onOpenLogsClick() {
        setLogsVisibility(true)
    }

    private fun setLogsVisibility(visible: Boolean) {
        appScope.launch {
            withContext(Dispatchers.Main) {
                _logFieldIsVisible.value = visible
                _windowWidth.value = if (visible) 1000 else 450
            }
        }
    }

    private var executor: ExecutionMode? = null
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