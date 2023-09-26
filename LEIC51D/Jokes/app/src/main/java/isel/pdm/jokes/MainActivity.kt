package isel.pdm.jokes

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import isel.pdm.jokes.ui.theme.JokesTheme

const val TAG = "JOKES_TAG"

/**
 * Lecture #3 script
 *
 * Step 1 - Lets look at the solution of the first challenge and discuss:
 *  - the separation between the UI and the domain logic
 *  - the design criteria: domain data is immutable
 *  - the requirements that stem from the need to build automated tests
 *  - the flow of data (top to bottom) and the flow of events (bottom to top)
 *  - the use of test doubles to test the UI (see MainScreenTests.kt)
 *
 * Step 2 - Effects in the UI with Jetpack Compose
 *  - discuss the need to perform I/O in the UI and its consequences
 *  - discuss Android's concurrency model
 *  - Step 2.1 - Lets make the joke fetching asynchronous (suspend functions)
 *  - Step 2.2 - The joke is fetched automatically upon composition (LaunchedEffect)
 *  - Step 2.3 - The joke is fetched upon button click (rememberCoroutineScope)
 *  - Step 2.4 - Lets make the joke fetching take its time
 *      - By using a delay
 *      - By blocking the UI thread :-/
 *
 * Step 3 - What happens if there's a reconfiguration?
 *  - Lets talk about the Activity lifecycle and its consequences
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
