package isel.pdm.jokes.daily

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import isel.pdm.jokes.FakeJokesService
import isel.pdm.jokes.about.AboutActivity

const val TAG = "JOKES_APP_TAG"

/**
 * Lecture #6 script (state management)
 *
 * Step 1 - Finish the JokeScreen implementation from last lecture
 *  - Add the navigation bar to the JokeScreen
 *  - Add the Refresh Fab to the JokeScreen
 * Step 2 - What happens when to the JokeScreen when a reconfiguration occurs?
 * Step 2.1 - When the joke is already displayed: the state is lost!
 * Step 2.2 - When the joke is being fetched: the joke is never displayed!
 *            What happens to the fetch operation?
 * Step 3 - Add a view model to the JokeScreen, to be used:
 * - as the container of the application state local to the screen
 * - as the host of the execution of the use case (the fetch operation)
 * Step 4 - We now have the trifecta of the MVVM pattern:
 * - the view (the JokeScreen)
 * - the view model (the JokeScreenViewModel)
 * - the model (the Joke and the JokesService)
 * Step 5 - What's the role of the activity?
 * - It deals with navigation
 * - It orchestrates the creation of the view model and the interaction
 *   between the view and the view model
 * Step 6 - What about the AboutActivity? Does it need a view model?
 * Step 7 - Lets add tests to the JokeScreenViewModel
 */
class JokeActivity : ComponentActivity() {

    private val viewModel by viewModels<JokeScreenViewModel>()
    private val service = FakeJokesService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "MainActivity.onCreate() called")
        setContent {
            JokeScreen(
                onInfoRequested = { AboutActivity.navigateTo(this) },
                onFetch = { viewModel.fetchJoke(service) },
                joke = viewModel.joke
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
