package isel.pdm.jokes

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import isel.pdm.jokes.ui.theme.JokesTheme
import kotlinx.coroutines.delay

const val TAG = "JOKES_TAG"

/**
 * The application's main activity
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.v(TAG, "MainActivity.onCreate: called")
        super.onCreate(savedInstanceState)
        setContent {
            JokeScreen(FakeJokesService())
        }
    }

    override fun onStart() {
        Log.v(TAG, "MainActivity.onStart: called")
        super.onStart()
    }

    override fun onStop() {
        Log.v(TAG, "MainActivity.onStop: called")
        super.onStop()
    }

    override fun onDestroy() {
        Log.v(TAG, "MainActivity.onDestroy: called")
        super.onDestroy()
    }
}
