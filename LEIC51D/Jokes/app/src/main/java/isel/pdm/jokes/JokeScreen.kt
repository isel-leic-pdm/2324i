package isel.pdm.jokes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import isel.pdm.jokes.ui.theme.JokesTheme
import kotlinx.coroutines.launch

/**
 * Tags used to identify the components of the JokeScreen in automated tests
 * (see app/src/androidTest/java/isel/pdm/chucknorris/JokeScreenTests.kt)
 */
const val FetchItTestTag = "FetchItTestTag"
const val JokeTestTag = "JokeTestTag"
const val JokeScreenTestTag = "JokeScreenTestTag"

/**
 * Root composable for the screen that displays a joke.
 * @param service the service used to fetch jokes.
 */
@Composable
fun JokeScreen(service: JokesService = NoOpJokeService) {

    var internalJoke by remember { mutableStateOf<Joke?>(null) }
    val scope = rememberCoroutineScope()

//    LaunchedEffect(key1 = service) {
//        internalJoke = service.fetchJoke()
//    }

    JokesTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .testTag(JokeScreenTestTag),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(6.dp),
            ) {
                internalJoke?.let { JokeView(joke = it) }
                Button(
                    modifier = Modifier.testTag(FetchItTestTag),
                    onClick = { scope.launch { internalJoke = service.fetchJoke() } }
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
    JokeScreen(NoOpJokeService)
}

