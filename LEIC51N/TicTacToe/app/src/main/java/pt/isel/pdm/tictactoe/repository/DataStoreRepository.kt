package pt.isel.pdm.tictactoe.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.skip
import pt.isel.pdm.tictactoe.model.UserInfo

class DataStoreRepository(
    private val dataStore: DataStore<Preferences>
) : UserRepository {
    companion object {
        val UserNamePreferenceKey = stringPreferencesKey("user_name")
    }

    override suspend fun getUserData(): UserInfo? {
        val prefs = dataStore.data.first()
        val userName = prefs.get(UserNamePreferenceKey)

        if (userName.isNullOrEmpty())
            return null

        return UserInfo(userName)
    }

    override suspend fun setUserData(usr: UserInfo) {
        dataStore.edit {
            it.set(UserNamePreferenceKey, usr.userName)
        }
    }

}