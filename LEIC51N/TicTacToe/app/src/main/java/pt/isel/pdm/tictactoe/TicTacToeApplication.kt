package pt.isel.pdm.tictactoe

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import pt.isel.pdm.tictactoe.repository.FakeUserRepository
import pt.isel.pdm.tictactoe.repository.UserRepository

interface DependencyContainer {
    val userRepository: UserRepository
}
class TicTacToeApplication() : Application(), DependencyContainer {
    override fun onCreate() {
        super.onCreate()
    }
    override val userRepository: UserRepository by lazy {
        FakeUserRepository()
    }



}