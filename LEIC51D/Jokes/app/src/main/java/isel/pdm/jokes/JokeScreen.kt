package isel.pdm.jokes

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import isel.pdm.jokes.ui.theme.JokesTheme

fun fetchJoke(): String {
    Log.v(TAG, "fetchJoke() called")
    return "Chuck Norris didn't call the wrong number, you answered the wrong phone."
}

/**
 * Tags used to identify the components of the JokeScreen in automated tests
 * (see app/src/androidTest/java/isel/pdm/chucknorris/JokeScreenTests.kt)
 */
const val FetchItTestTag = "FetchItTestTag"
const val JokeTestTag = "JokeTestTag"
const val JokeScreenTestTag = "JokeScreenTestTag"

/**
 * Root composable for the screen that displays a joke
 */
@Composable
fun JokeScreen() {
    JokesTheme {
        Log.v(TAG, "JokeScreen: composing")
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize().testTag(JokeScreenTestTag),
            color = MaterialTheme.colorScheme.background
        ) {
            var joke by remember { mutableStateOf("") }

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(6.dp),
            ) {
                Log.i(TAG, "Column: composing")
                if (joke.isNotBlank()) {
                    Text(
                        text = joke,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                            .testTag(JokeTestTag)
                    )
                }
                Button(
                    onClick = { /*joke = fetchJoke()*/ },
                    modifier = Modifier.testTag(FetchItTestTag)
                ) {
                    Text(text = "Fetch it!")
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun JokeScreenPreview() {
    JokesTheme {
        JokeScreen()
    }
}
