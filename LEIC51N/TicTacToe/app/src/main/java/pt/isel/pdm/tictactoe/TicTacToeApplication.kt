package pt.isel.pdm.tictactoe

import android.app.Application
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import pt.isel.pdm.tictactoe.database.TicTacToeDatabase
import pt.isel.pdm.tictactoe.database.UserStatDao
import pt.isel.pdm.tictactoe.repository.DataStoreRepository
import pt.isel.pdm.tictactoe.repository.FakeUserRepository
import pt.isel.pdm.tictactoe.repository.RoomUserStatRepository
import pt.isel.pdm.tictactoe.repository.UserRepository
import pt.isel.pdm.tictactoe.repository.UserStatRepository
import pt.isel.pdm.tictactoe.services.FirestoreGameService
import pt.isel.pdm.tictactoe.services.FirestoreMatchmakingService
import pt.isel.pdm.tictactoe.services.GameService
import pt.isel.pdm.tictactoe.services.MatchmakingService

interface DependencyContainer {
    val userRepository: UserRepository
    val matchmakingService: MatchmakingService
    val gameService: GameService
    val userStatRepository: UserStatRepository
}

class TicTacToeApplication() : Application(), DependencyContainer {

    companion object {
        const val UserPreferencesDataStoreName = "UserInfo"
        const val TicTacToeDatabaseName = "TicTacToeDatabase"
    }

    private val dataStore by preferencesDataStore(
        name = UserPreferencesDataStoreName
    )

    override fun onCreate() {
        super.onCreate()
    }

    private val appDatabase: TicTacToeDatabase by lazy {
        Room.databaseBuilder(
            this,
            TicTacToeDatabase::class.java,
            TicTacToeDatabaseName
        ).build()
    }


    override val userRepository: UserRepository by lazy {
        DataStoreRepository(dataStore)
    }

    override val userStatRepository: UserStatRepository by lazy {
        RoomUserStatRepository(appDatabase.userStatDao())
    }

    override val matchmakingService: MatchmakingService by lazy {
        FirestoreMatchmakingService(Firebase.firestore)
    }

    override val gameService: GameService by lazy {
        FirestoreGameService(Firebase.firestore)
    }


}