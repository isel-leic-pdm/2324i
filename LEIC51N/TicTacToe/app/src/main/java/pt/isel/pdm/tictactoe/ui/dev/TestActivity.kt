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
import pt.isel.pdm.tictactoe.ui.theme.TicTacToeTheme

class TestActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        val service = (application as TicTacToeApplication).matchmakingService
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            val lobbies = service.getLobbies()
            for (l in lobbies)
                Log.d("Test", l.toString())
        }
    }
}
