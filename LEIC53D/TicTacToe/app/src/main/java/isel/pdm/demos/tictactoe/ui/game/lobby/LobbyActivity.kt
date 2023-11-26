package isel.pdm.demos.tictactoe.ui.game.lobby

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

/**
 * The activity that hosts the screen that allows the user to select the opponent and start the game.
 */
class LobbyActivity : ComponentActivity() {

    companion object {
        /**
         * Navigates to the [LobbyActivity] activity.
         * @param origin the activity from which the navigation is performed.
         */
        fun navigateTo(origin: ComponentActivity) {
            with(origin) {
                val intent = Intent(origin, LobbyActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LobbyScreen(
                playersInLobby = emptyList(),
                onPlayerSelected = { TODO() },
                onNavigateBackRequested = { TODO() },
                onNavigateToPreferencesRequested = { TODO() }
            )
        }
    }
}
