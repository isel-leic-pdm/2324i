package isel.pdm.demos.tictactoe.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
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

    private val vm by viewModels<MainScreenViewModel> {
        MainScreenViewModel.factory((application as DependenciesContainer).userInfoRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            vm.userInfo.collect {
                if (it is Loaded) {
                    doNavigation(userInfo = it.getOrNull())
                    vm.resetToIdle()
                }
            }
        }

        setContent {
            val state by vm.userInfo.collectAsState(initial = idle())
            MainScreen(
                onPlayEnabled = state is Idle,
                onPlayRequested = {
                    vm.fetchUserInfo()
                }
            )
        }
    }

    /**
     * Navigates to the appropriate activity, depending on whether the
     * user information has already been provided or not.
     * @param userInfo the user information.
     */
    private fun doNavigation(userInfo: UserInfo?) {
        if (userInfo == null)
            UserPreferencesActivity.navigateTo(this)
        else
            LobbyActivity.navigateTo(this)
    }
}
