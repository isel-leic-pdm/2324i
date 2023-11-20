package isel.pdm.demos.tictactoe.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.lifecycleScope
import isel.pdm.demos.tictactoe.DependenciesContainer
import isel.pdm.demos.tictactoe.domain.Idle
import isel.pdm.demos.tictactoe.domain.Loaded
import isel.pdm.demos.tictactoe.domain.UserInfo
import isel.pdm.demos.tictactoe.domain.getOrNull
import isel.pdm.demos.tictactoe.domain.idle
import isel.pdm.demos.tictactoe.ui.game.lobby.LobbyActivity
import isel.pdm.demos.tictactoe.ui.preferences.UserPreferencesActivity
import kotlinx.coroutines.launch

/**
 * The application's main activity.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            vm.userInfoFlow.collect {
               if (it is Loaded) {
                   doNavigation(it.getOrNull())
                   vm.resetToIdle()
               }
            }
        }

        setContent {
            val currentUserInfo by vm.userInfoFlow.collectAsState(initial = idle())
            MainScreen(
                onPlayEnabled = currentUserInfo is Idle,
                onPlayRequested = { vm.fetchUserInfo() }
            )
        }
    }

    private val vm by viewModels<MainScreenViewModel> {
        MainScreenViewModel.factory((application as DependenciesContainer).userInfoRepository)
    }

    private fun doNavigation(userInfo: UserInfo?) {
        if (userInfo == null)
            UserPreferencesActivity.navigateTo(this)
        else
            LobbyActivity.navigateTo(this)
    }
}
