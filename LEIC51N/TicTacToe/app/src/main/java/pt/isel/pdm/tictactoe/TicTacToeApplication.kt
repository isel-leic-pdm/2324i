package pt.isel.pdm.tictactoe

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import pt.isel.pdm.tictactoe.repository.FakeUserRepository
import pt.isel.pdm.tictactoe.repository.UserRepository
import pt.isel.pdm.tictactoe.services.FirestoreMatchmakingService
import pt.isel.pdm.tictactoe.services.MatchmakingService

interface DependencyContainer {
    val userRepository: UserRepository
    val matchmakingService: MatchmakingService
}
class TicTacToeApplication() : Application(), DependencyContainer {
    override fun onCreate() {
        super.onCreate()
    }
    override val userRepository: UserRepository by lazy {
        FakeUserRepository()
    }

    override val matchmakingService: MatchmakingService by lazy {
        FirestoreMatchmakingService(Firebase.firestore)
    }



}