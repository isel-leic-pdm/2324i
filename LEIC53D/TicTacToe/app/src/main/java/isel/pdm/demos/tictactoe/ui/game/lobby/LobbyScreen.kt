package isel.pdm.demos.tictactoe.ui.game.lobby

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.R
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import isel.pdm.demos.tictactoe.ui.common.NavigationHandlers
import isel.pdm.demos.tictactoe.ui.common.TopBar
import isel.pdm.demos.tictactoe.ui.common.theme.TicTacToeTheme

/**
 * The tag used to identify the lobby screen in automated tests.
 */
const val LobbyScreenTag = "LobbyScreen"

/**
 * The screen that allows the user to select the opponent and start the game.
 */
@Composable
fun LobbyScreen() {
    TicTacToeTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize().testTag(LobbyScreenTag),
            topBar = {
                TopBar(
                    NavigationHandlers(
                        onBackRequested = { TODO() },
                        onPreferencesRequested = { TODO() }
                    )
                )
            },
        ) { innerPadding ->
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(innerPadding).fillMaxSize(),
            ) {
                // TODO: Add the screen content
            }
        }
    }
}
