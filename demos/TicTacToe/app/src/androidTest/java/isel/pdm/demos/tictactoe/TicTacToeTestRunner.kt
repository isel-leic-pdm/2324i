package isel.pdm.demos.tictactoe

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.MemoryCacheSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.mockk.coEvery
import io.mockk.mockk
import isel.pdm.demos.tictactoe.domain.game.lobby.Lobby
import isel.pdm.demos.tictactoe.domain.game.play.Match
import isel.pdm.demos.tictactoe.domain.user.UserInfo
import isel.pdm.demos.tictactoe.domain.user.UserInfoRepository
import kotlinx.coroutines.flow.emptyFlow

/**
 * The service locator to be used in the instrumented tests.
 */
class TicTacToeTestApplication : DependenciesContainer, Application() {

    val emulatedFirestoreDb: FirebaseFirestore by lazy {
        Firebase.firestore.also {
            it.useEmulator("10.0.2.2", 8080)
            it.firestoreSettings = FirebaseFirestoreSettings.Builder()
                .setLocalCacheSettings(MemoryCacheSettings.newBuilder().build())
                .build()
        }
    }

    override var userInfoRepository: UserInfoRepository =
        mockk {
            coEvery { getUserInfo() } returns UserInfo("test nickname", "the motto")
        }

    override var lobby: Lobby =
        mockk(relaxed = true) {
            coEvery { enter(any()) } returns emptyFlow()
            coEvery { leave() } returns Unit
        }

    override var matchFactory: () -> Match = {
        mockk(relaxed = true) {
            coEvery { start(any(), any()) } returns emptyFlow()
            coEvery { makeMove(any()) } returns Unit
            coEvery { forfeit() } returns Unit
        }
    }
}

@Suppress("unused")
class TicTacToeTestRunner : AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader, className: String, context: Context): Application {
        return super.newApplication(cl, TicTacToeTestApplication::class.java.name, context)
    }
}
