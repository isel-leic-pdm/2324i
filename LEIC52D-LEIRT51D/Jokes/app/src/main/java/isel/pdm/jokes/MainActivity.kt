package isel.pdm.jokes

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

const val TAG = "JOKES_APP_TAG"

/**
 * The starting screen of the Jokes app
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "onCreate() called")
        setContent {
            JokeScreen(service = FakeJokesService())
        }
    }

    override fun onStart() {
        super.onStart()
        Log.v(TAG, "onStart() called")
    }

    override fun onStop() {
        super.onStop()
        Log.v(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(TAG, "onDestroy() called")
    }
}
