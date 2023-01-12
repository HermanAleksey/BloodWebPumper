package helper.preferences

data class PreferencesData(
    val isLogsOpen: Boolean,
    val selectedModeOrderedNumber: Int,
    val levelsToUpgradeAmount: Int,
    //todo we can replace start and finish buttons there for example
)