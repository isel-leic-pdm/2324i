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
import isel.pdm.demos.tictactoe.domain.game.Lobby
import isel.pdm.demos.tictactoe.domain.game.PlayerInfo
import isel.pdm.demos.tictactoe.domain.user.UserInfo
import isel.pdm.demos.tictactoe.domain.user.UserInfoRepository


val localTestPlayer = PlayerInfo(UserInfo("local"))

val otherTestPlayersInLobby: List<PlayerInfo> = buildList {
    repeat(3 ) {
        add(PlayerInfo(UserInfo("remote $it", "motto")))
    }
}

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

    override val lobby: Lobby
        get() = TODO("Not yet implemented")

}

@Suppress("unused")
class TicTacToeTestRunner : AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader, className: String, context: Context): Application {
        return super.newApplication(cl, TicTacToeTestApplication::class.java.name, context)
    }
}
