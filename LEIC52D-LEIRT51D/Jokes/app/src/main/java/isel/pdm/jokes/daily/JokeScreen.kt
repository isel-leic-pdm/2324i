package isel.pdm.jokes.daily

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.unit.dp
import isel.pdm.jokes.Joke
import isel.pdm.jokes.JokesService
import isel.pdm.jokes.NoOpJokeService
import isel.pdm.jokes.R
import isel.pdm.jokes.ui.ErrorAlert
import isel.pdm.jokes.ui.NavigationHandlers
import isel.pdm.jokes.ui.RefreshFab
import isel.pdm.jokes.ui.TopBar
import isel.pdm.jokes.ui.theme.JokesTheme
import kotlinx.coroutines.launch
import java.net.URL

/**
 * Tags used to identify the components of the JokeScreen in automated tests
 * (see app/src/androidTest/java/isel/pdm/chucknorris/JokeScreenTests.kt)
 */
/**
 * Tags used to identify the components of the MainScreen in automated tests
 */
const val FetchItTestTag = "FetchItTestTag"
const val JokeTestTag = "JokeTestTag"
const val MainScreenTestTag = "JokeScreenTestTag"

/**
 * Root composable for the screen that displays a joke.
 * @param joke the joke to be displayed. If null, no joke is displayed.
 * @param onFetch the callback invoked when the user clicks the "Fetch it!" button.
 * @param onInfoRequested the callback invoked when the user clicks the "Info" button.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JokeScreen(
    joke: LoadState = Idle,
    onFetch: () -> Unit = { },
    onInfoRequested: () -> Unit = { }
) {
    JokesTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp)
                .testTag(MainScreenTestTag),
            floatingActionButton = {
                RefreshFab(
                    refreshing = joke is Loading,
                    onClick = onFetch,
                    modifier = Modifier.testTag(FetchItTestTag)
                )
            },
            floatingActionButtonPosition = FabPosition.Center,
            topBar = {
                TopBar(navigation = NavigationHandlers(onInfoRequested = onInfoRequested))
            }
        ) { innerPadding ->
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                if (joke is Loaded) {
                    joke.result.getOrNull()?.let {
                        JokeView(joke = it)
                    }
                }
            }
        }

        if (joke is Loaded && joke.result.isFailure) {
            ErrorAlert(
                title = R.string.error_api_title,
                message = R.string.error_could_not_reach_api,
                buttonText = R.string.error_retry_button_text,
                onDismiss = onFetch
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun JokeScreenPreview() {
    val aJoke = Joke(
        text = "This graveyard looks overcrowded. People must be dying to get in there.",
        source = URL("https://www.keeplaughingforever.com/corny-dad-jokes")
    )
    JokeScreen()
}

