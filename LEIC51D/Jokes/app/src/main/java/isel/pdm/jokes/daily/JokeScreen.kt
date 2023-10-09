package isel.pdm.jokes.daily

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import isel.pdm.jokes.Joke
import isel.pdm.jokes.JokesService
import isel.pdm.jokes.NoOpJokeService
import isel.pdm.jokes.ui.NavigationHandlers
import isel.pdm.jokes.ui.TopBar
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JokeScreen(
    service: JokesService = NoOpJokeService,
    onInfoRequested: () -> Unit = { }
) {

    var internalJoke by remember { mutableStateOf<Joke?>(null) }
    val scope = rememberCoroutineScope()

    JokesTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag(JokeScreenTestTag),
            topBar = { TopBar(
                navigation = NavigationHandlers(
                    onInfoRequested = onInfoRequested
                )
            ) }
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
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

