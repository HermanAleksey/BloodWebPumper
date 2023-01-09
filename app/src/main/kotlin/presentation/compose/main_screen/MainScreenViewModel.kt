package presentation.compose.main_screen

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class MainScreenViewModel {

    private val _logFieldIsVisible: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val logFieldIsVisible = _logFieldIsVisible.asStateFlow()

    val windowWidth = _logFieldIsVisible.map { visible ->
        if (visible) 1000 else 450
    }

    fun onCloseLogsClick() {
        _logFieldIsVisible.value = false
    }

    fun onOpenLogsClick() {
        _logFieldIsVisible.value = true
    }
}