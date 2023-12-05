package isel.pdm.demos.tictactoe.infrastructure

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import isel.pdm.demos.tictactoe.domain.user.UserInfo
import isel.pdm.demos.tictactoe.domain.user.UserInfoRepository
import kotlinx.coroutines.flow.first

private const val USER_NICK_KEY = "Nick"
private const val USER_MOTTO_KEY = "Motto"

/**
 * A user information repository implementation supported in DataStore, the
 * modern alternative to SharedPreferences.
 */
class UserInfoDataStore(private val store: DataStore<Preferences>) : UserInfoRepository {

    private val nickKey = stringPreferencesKey(USER_NICK_KEY)
    private val mottoKey = stringPreferencesKey(USER_MOTTO_KEY)

    override suspend fun getUserInfo(): UserInfo? {
        val preferences = store.data.first()
        val nick = preferences[nickKey]
        return if (nick != null) UserInfo(nick, preferences[mottoKey]) else null
    }

    override suspend fun updateUserInfo(userInfo: UserInfo) {
        store.edit { preferences ->
            preferences[nickKey] = userInfo.nick
            userInfo.motto?.let {
                preferences[mottoKey] = it
            } ?: preferences.remove(mottoKey)
        }
    }
}
