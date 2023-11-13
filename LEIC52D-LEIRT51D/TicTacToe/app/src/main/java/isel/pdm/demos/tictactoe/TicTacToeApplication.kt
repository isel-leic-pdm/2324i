package isel.pdm.demos.tictactoe

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import isel.pdm.demos.tictactoe.domain.UserInfoRepository

const val TAG = "TicTacToeApp"

/**
 * The contract for the object that holds all the globally relevant dependencies.
 */
interface DependenciesContainer {
    val userInfoRepository: UserInfoRepository
}

/**
 * The application class to be used as a Service Locator.
 */
class TicTacToeApplication : Application(), DependenciesContainer {

    override val userInfoRepository: UserInfoRepository
        get() = TODO()
}
