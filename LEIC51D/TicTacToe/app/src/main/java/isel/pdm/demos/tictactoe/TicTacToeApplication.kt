package isel.pdm.demos.tictactoe

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import isel.pdm.demos.tictactoe.domain.game.Lobby
import isel.pdm.demos.tictactoe.domain.user.UserInfoRepository
import isel.pdm.demos.tictactoe.infrastructure.UserInfoDataStore

const val TAG = "TicTacToeApp"

/**
 * The contract for the object that holds all the globally relevant dependencies.
 */
interface DependenciesContainer {
    val userInfoRepository: UserInfoRepository
    val lobby: Lobby
}

/**
 * The application class to be used as a Service Locator.
 */
class TicTacToeApplication : Application(), DependenciesContainer {

    private val dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_info")

    override val userInfoRepository: UserInfoRepository
        get() = UserInfoDataStore(dataStore)

    override val lobby: Lobby
        get() = TODO("Not yet implemented")
}
