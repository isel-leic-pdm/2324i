package isel.pdm.jokes.daily

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import isel.pdm.jokes.FakeJokesService

const val TAG = "JOKES_TAG"

/**
 * Lecture #5 script
 *
 * Step 1 - Lets briefly review the application's structure
 * Step 2 - Describe the application's UX (navigation)
 * Step 3 - Lets create the About screen, using the same structure as before
 *     Step 3.1 - Create the AboutActivity.
 *     Step 3.2 - Create the AboutScreen. Empty, at first.
 * Step 4 - Add a top bar to both screens.
 *     Step 4.1 - Start by describing the top bar composable and the possible actions.
 *     Step 4.2 - Add the top bar to the Joke Screen with navigation to the About screen.
 *     Step 4.3 - Add the top bar to the About Screen with back navigation.
 * Step 5 - Implement the AboutScreen.
 * Step 6 - Refactor the main screen so that it uses the RefreshFab composable.
 *     Step 5.1 - Describe the RefreshFab composable.
 *     Step 5.2 - Use it on the MainScreen.
 * Step 7 - Add a delay to the joke fetching and observe the consequences of a reconfiguration.
 * Step 8 - Lets add a ViewModel to the MainScreen.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "onCreate() called")
        setContent {
            JokeScreen(FakeJokesService())
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
