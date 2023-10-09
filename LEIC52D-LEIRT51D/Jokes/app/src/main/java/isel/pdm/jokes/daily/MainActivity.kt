package isel.pdm.jokes.daily

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import isel.pdm.jokes.FakeJokesService
import isel.pdm.jokes.about.AboutActivity

const val TAG = "JOKES_APP_TAG"

/**
 * The starting screen of the Jokes app
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "MainActivity.onCreate() called")
        setContent {
            JokeScreen(
                service = FakeJokesService(),
                onInfoRequested = { AboutActivity.navigateTo(this) }
            )
        }
    }

    override fun onStart() {
        super.onStart()
        Log.v(TAG, "MainActivity.onStart() called")
    }

    override fun onStop() {
        super.onStop()
        Log.v(TAG, "MainActivity.onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(TAG, "MainActivity.onDestroy() called")
    }
}
