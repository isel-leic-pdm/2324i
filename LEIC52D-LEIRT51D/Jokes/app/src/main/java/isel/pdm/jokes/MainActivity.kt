package isel.pdm.jokes

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import isel.pdm.jokes.ui.theme.JokesTheme

const val TAG = "JOKES_APP_TAG"

/**
 * The starting screen of the Jokes app
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "onCreate() called")
        setContent {
            JokeScreen()
        }
    }
}
