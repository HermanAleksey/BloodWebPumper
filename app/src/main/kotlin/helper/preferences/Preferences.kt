package helper.preferences

interface Preferences {

    suspend fun getPreferences(): PreferencesData

    suspend fun setPreferences(preferences: PreferencesData)

    //sets default value of preferences
    suspend fun clearPreferences()
}