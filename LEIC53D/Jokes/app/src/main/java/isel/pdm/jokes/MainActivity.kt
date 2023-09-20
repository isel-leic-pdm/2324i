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

const val TAG = "JOKES_TAG"

fun fetchJoke(): String {
    Log.v(TAG, "fetchJoke: called")
    return "The dinosaurs once looked at Chuck Norris the wrong way" +
            " and now we call them extinct."
}

/**
 * Lecture #2 script
 *
 * Step 1 - Observe
 *  - the structure of the project (including manifest)
 *  - the MainActivity class
 *  - the Greeting composable
 *  - the GreetingPreview composable and the preview pane in the IDE
 *  - the JokesTheme composable
 * Step 2 - Lets start building the Jokes app
 *  - cleanup the project (remove the Greeting composable and the GreetingPreview composable)
 *  - create a new composable named JokeScreen, which represents the screen that displays a joke
 * Step 3 - Lets implement the layout of the JokeScreen
 * Step 4 - Lets implement the logic of the JokeScreen (non realistic)
 *      - the joke is fetch from its provider (a fake one) whe the Fetch button is pressed, and displayed
 *      on the screen
 * Step 5 - Lets build automated tests for the JokeScreen
 *    - the tests should check that the joke is displayed when the Fetch button is pressed
 *    - the tests should check that the joke is not displayed until the Fetch button is pressed for
 *    the first time
 *    - the tests should check that the provider function is called when the Fetch button is pressed
 * Step 6 - Discuss the order of the previous two steps (Test Driven Development approach)
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.v(TAG, "MainActivity.onCreate: called")
        super.onCreate(savedInstanceState)
        setContent {
            JokeScreen()
        }
    }
}
