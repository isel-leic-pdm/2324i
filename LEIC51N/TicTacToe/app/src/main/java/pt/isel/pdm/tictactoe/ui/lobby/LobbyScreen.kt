package pt.isel.pdm.tictactoe.ui.lobby

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pt.isel.pdm.tictactoe.model.GameLobby
import pt.isel.pdm.tictactoe.ui.components.NavigationHandlers
import pt.isel.pdm.tictactoe.ui.components.TopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LobbyScreen(
    lobbies: List<GameLobby>,
    refreshLobbies: () -> Unit,
    createNewLobby: () -> Unit,
    lobbyClicked: (GameLobby) -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(
                navigationHandlers = NavigationHandlers(
                    refreshHandler = refreshLobbies
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = createNewLobby,
            ) {
                Icon(Icons.Filled.Add, "New Lobby.")
            }

        }

    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues),
            contentPadding = PaddingValues(36.dp)
        )
        {
            items(lobbies) { lobby ->
                Button(
                    onClick = {
                        lobbyClicked(lobby)
                    }) {
                    Text(text = lobby.displayName)
                }
            }
        }
    }
}