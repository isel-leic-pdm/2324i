package isel.pdm.demos.tictactoe.ui.game.lobby

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import isel.pdm.demos.tictactoe.R
import isel.pdm.demos.tictactoe.domain.game.PlayerInfo
import isel.pdm.demos.tictactoe.domain.user.UserInfo
import isel.pdm.demos.tictactoe.ui.common.NavigationHandlers
import isel.pdm.demos.tictactoe.ui.common.TopBar
import isel.pdm.demos.tictactoe.ui.common.theme.TicTacToeTheme

/**
 * The tag used to identify the lobby screen in automated tests.
 */
const val LobbyScreenTag = "LobbyScreen"

/**
 * The screen that allows the user to select the opponent and start the game.
 * @param playersInLobby the list of players in the lobby.
 * @param onPlayerSelected the callback to invoke when a player is selected.
 * @param onNavigateBackRequested the callback to invoke when the user requests
 * to navigate back.
 * @param onNavigateToPreferencesRequested the callback to invoke when the user
 * requests to navigate to the preferences screen.
 */
@Composable
fun LobbyScreen(
    playersInLobby: List<PlayerInfo>,
    onPlayerSelected: (PlayerInfo) -> Unit,
    onNavigateBackRequested: () -> Unit,
    onNavigateToPreferencesRequested: () -> Unit
) {
    TicTacToeTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize().testTag(LobbyScreenTag),
            topBar = {
                TopBar(
                    NavigationHandlers(
                        onBackRequested = onNavigateBackRequested,
                        onPreferencesRequested = onNavigateToPreferencesRequested
                    )
                )
            },
        ) { innerPadding ->
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            ) {
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = stringResource(id = R.string.lobby_screen_title),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(vertical = 32.dp)
                ) {
                    this.items(playersInLobby) {
                        PlayerInfoView(playerInfo = it, onPlayerSelected)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LobbyScreenPreview() {
    LobbyScreen(
        playersInLobby = players,
        onPlayerSelected = { },
        onNavigateBackRequested = { },
        onNavigateToPreferencesRequested = { }
    )
}

private val players = buildList {
    repeat(30) {
        add(PlayerInfo(UserInfo("My Nick $it", "This is my $it moto")))
    }
}