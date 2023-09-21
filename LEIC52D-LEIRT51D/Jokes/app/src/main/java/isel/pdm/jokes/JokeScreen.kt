package isel.pdm.jokes

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import isel.pdm.jokes.ui.theme.JokesTheme

const val JokeTestTag = "JokeTestTag"
const val FetchButtonTestTag = "FetchButtonTestTag"
const val JokeScreenTestTag = "JokeScreenTestTag"

fun fetchJoke(): String {
    Log.v(TAG, "Fetch joke")
    return "Chuck Norris didn't call the wrong number, you answered the wrong phone."
}

@Composable
fun JokeScreen() {
    JokesTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize().testTag(JokeScreenTestTag),
            color = MaterialTheme.colorScheme.background
        ) {
            Log.v(TAG, "JokeScreen()")
            val joke = remember { mutableStateOf("") }
            Column {
                if (joke.value.isNotBlank()) {
                    Text(
                        text = joke.value,
                        modifier = Modifier.testTag(JokeTestTag)
                    )
                }
                Button(
                    onClick = { joke.value = fetchJoke() },
                    modifier = Modifier.testTag(FetchButtonTestTag)
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
    JokeScreen()
}