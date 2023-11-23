package pt.isel.pdm.tictactoe.ui.dev

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pt.isel.pdm.tictactoe.TicTacToeApplication
import pt.isel.pdm.tictactoe.model.GameLobby
import pt.isel.pdm.tictactoe.model.GameSession
import pt.isel.pdm.tictactoe.ui.theme.TicTacToeTheme

class TestActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        val service = (application as TicTacToeApplication).matchmakingService
        var game by mutableStateOf<GameSession?>(null)
        var lobby by mutableStateOf<GameLobby?>(null)
        var currOper by mutableStateOf<String?>(null)

        super.onCreate(savedInstanceState)
        setContent {
            TicTacToeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    if (game == null && lobby == null && currOper == null) {
                        Column {
                            Button(onClick = {
                                currOper = "Create"
                                lifecycleScope.launch {
                                    game = service.createLobbyAndWaitForPlayer("Test")
                                }
                            }) {
                                Text(text = "Create n Wait")
                            }
                            Button(onClick = {
                                lifecycleScope.launch {
                                    currOper = "Join"
                                    lobby = service.getLobbies().filter { it.displayName == "Test" }
                                        .first()
                                    game = service.joinLobby("P2", lobby!!)
                                }
                            }) {
                                Text(text = "Join")
                            }
                        }
                    }
                    if (game != null) {
                        Text(text = "Matchmaking complete\n$game")
                    } else if (lobby != null) {
                        Text(text = "Lobby created \n$lobby ")
                    } else if (currOper != null) {
                        Text("Loading $currOper")
                    }


                }
            }
        }
    }
}
