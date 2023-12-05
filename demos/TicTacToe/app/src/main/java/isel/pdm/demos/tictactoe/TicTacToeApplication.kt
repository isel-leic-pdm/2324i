package isel.pdm.demos.tictactoe

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.MemoryCacheSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import isel.pdm.demos.tictactoe.domain.game.Lobby
import isel.pdm.demos.tictactoe.domain.user.UserInfoRepository
import isel.pdm.demos.tictactoe.infrastructure.LobbyFirebase
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

    private val emulatedFirestoreDb: FirebaseFirestore by lazy {
        Firebase.firestore.also {
            it.useEmulator("10.0.2.2", 8080)
            it.firestoreSettings = FirebaseFirestoreSettings.Builder()
                .setLocalCacheSettings(MemoryCacheSettings.newBuilder().build())
                .build()
        }
    }

    private val realFirestoreDb: FirebaseFirestore by lazy {
        Firebase.firestore
    }

    override val userInfoRepository: UserInfoRepository
        get() = UserInfoDataStore(dataStore)

    override val lobby: Lobby
        get() = LobbyFirebase(emulatedFirestoreDb)
}
