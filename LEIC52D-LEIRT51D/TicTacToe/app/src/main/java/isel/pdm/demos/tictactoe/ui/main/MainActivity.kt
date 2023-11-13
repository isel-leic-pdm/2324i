package isel.pdm.demos.tictactoe.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import isel.pdm.demos.tictactoe.DependenciesContainer
import isel.pdm.demos.tictactoe.TicTacToeApplication

/**
 * The application's main activity.
 */
class MainActivity : ComponentActivity() {

    private val app by lazy { application as DependenciesContainer }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TODO()
        }
    }
}
