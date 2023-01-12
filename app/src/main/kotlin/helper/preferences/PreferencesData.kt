package helper.preferences

import model.Command

data class PreferencesData(
    val isLogsOpen: Boolean,
    val selectedMode: Command.Mode,
    val levelsToUpgradeAmount: Int,
    //todo we can replace start and finish buttons there for example
)