package helper.preferences

import model.Command

class PreferencesImpl: Preferences {

    override suspend fun getPreferences(): PreferencesData {
        return  PreferencesData(
            isLogsOpen = true,
            selectedMode = Command.Mode.RAREST_FIRST,
            levelsToUpgradeAmount = 10,
        )
        TODO("Not yet implemented")
    }

    override suspend fun setPreferences(preferences: PreferencesData) {
        return
        TODO("Not yet implemented")
    }

    override suspend fun clearPreferences() {
        return
        TODO("Not yet implemented")
    }
}