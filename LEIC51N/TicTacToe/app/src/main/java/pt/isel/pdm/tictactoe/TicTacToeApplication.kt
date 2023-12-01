package pt.isel.pdm.tictactoe

import android.app.Application
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import pt.isel.pdm.tictactoe.repository.DataStoreRepository
import pt.isel.pdm.tictactoe.repository.FakeUserRepository
import pt.isel.pdm.tictactoe.repository.UserRepository
import pt.isel.pdm.tictactoe.services.FirestoreGameService
import pt.isel.pdm.tictactoe.services.FirestoreMatchmakingService
import pt.isel.pdm.tictactoe.services.GameService
import pt.isel.pdm.tictactoe.services.MatchmakingService

interface DependencyContainer {
    val userRepository: UserRepository
    val matchmakingService: MatchmakingService
    val gameService : GameService
}
class TicTacToeApplication() : Application(), DependencyContainer {

    companion object {
        const val UserPreferencesDataStoreName = "UserInfo"
    }

    private val dataStore by preferencesDataStore(
        name = UserPreferencesDataStoreName
    )

    override fun onCreate() {
        super.onCreate()
    }

    override val userRepository: UserRepository by lazy {
        DataStoreRepository(dataStore)
    }

    override val matchmakingService: MatchmakingService by lazy {
        FirestoreMatchmakingService(Firebase.firestore)
    }

    override val gameService: GameService by lazy {
        FirestoreGameService(Firebase.firestore)
    }



}