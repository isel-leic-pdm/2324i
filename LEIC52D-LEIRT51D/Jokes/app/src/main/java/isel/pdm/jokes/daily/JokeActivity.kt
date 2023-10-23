package isel.pdm.jokes.daily

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.google.gson.Gson
import isel.pdm.jokes.FakeJokesService
import isel.pdm.jokes.JokesApplication
import isel.pdm.jokes.about.AboutActivity
import isel.pdm.jokes.http.IcanhazDadJoke
import okhttp3.OkHttpClient

const val TAG = "JOKES_APP_TAG"

class JokeActivity : ComponentActivity() {

    private val viewModel by viewModels<JokeScreenViewModel>()
    private val app by lazy { application as JokesApplication }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "MainActivity.onCreate() called")
        setContent {
            JokeScreen(
                onInfoRequested = { AboutActivity.navigateTo(this) },
                onFetch = { viewModel.fetchJoke(app.jokesService) },
                joke = viewModel.joke,
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
