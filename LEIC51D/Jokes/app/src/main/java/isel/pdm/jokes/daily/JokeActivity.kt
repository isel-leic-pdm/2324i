package isel.pdm.jokes.daily

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import isel.pdm.jokes.FakeJokesService
import isel.pdm.jokes.JokesApplication
import isel.pdm.jokes.about.AboutActivity

const val TAG = "JOKES_TAG"

/**
 *
 */
class JokeActivity : ComponentActivity() {

    private val app by lazy { application as JokesApplication }
    private val viewModel by viewModels<JokeScreenViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "onCreate() called")
        setContent {
            JokeScreen(
                joke = viewModel.joke,
                onFetchRequested = { viewModel.fetchJoke(app.service) },
                onInfoRequested = { AboutActivity.navigateTo(this) }
            )
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
