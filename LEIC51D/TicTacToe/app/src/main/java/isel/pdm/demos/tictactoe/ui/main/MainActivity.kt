package isel.pdm.demos.tictactoe.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.lifecycleScope
import isel.pdm.demos.tictactoe.DependenciesContainer
import isel.pdm.demos.tictactoe.ui.game.lobby.LobbyActivity
import isel.pdm.demos.tictactoe.ui.preferences.UserPreferencesActivity
import kotlinx.coroutines.launch

/**
 * The application's main activity.
 */
class MainActivity : ComponentActivity() {

    private val app by lazy { application as DependenciesContainer }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val scope = rememberCoroutineScope()
            MainScreen(
                onPlayRequested = {
                    scope.launch {
                        val userInfo = app.userInfoRepository.getUserInfo()
                        if (userInfo != null)
                            LobbyActivity.navigateTo(this@MainActivity)
                        else
                            UserPreferencesActivity.navigateTo(this@MainActivity)
                    }
                }
            )
        }
    }
}
