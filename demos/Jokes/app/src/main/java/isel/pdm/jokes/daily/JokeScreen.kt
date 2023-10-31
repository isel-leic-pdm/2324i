package isel.pdm.jokes.daily

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import isel.pdm.jokes.domain.Joke
import isel.pdm.jokes.domain.LoadState
import isel.pdm.jokes.domain.Loading
import isel.pdm.jokes.R
import isel.pdm.jokes.domain.exceptionOrNull
import isel.pdm.jokes.domain.getOrNull
import isel.pdm.jokes.domain.idle
import isel.pdm.jokes.domain.success
import isel.pdm.jokes.ui.ErrorAlert
import isel.pdm.jokes.ui.NavigationHandlers
import isel.pdm.jokes.ui.RefreshFab
import isel.pdm.jokes.ui.TopBar
import isel.pdm.jokes.ui.theme.JokesTheme
import java.net.URL

/**
 * Tags used to identify the components of the [JokeFetchScreen] in automated tests
 */
const val FetchItTestTag = "FetchItTestTag"
const val JokeFetchScreenTestTag = "JokeFetchScreenTestTag"

/**
 * Root composable for the main screen, the one that displays a joke fetched from the used API.
 *
 * @param joke the joke to be displayed, if already loaded.
 * @param onFetchRequested the callback invoked when the user clicks elects to fetch a joke.
 * @param onNavigateToInfo the callback invoked when the user clicks the "Info" button.
 * @param onNavigateToSearch the callback invoked when the user clicks the "Search" button.
 */
@Composable
fun JokeFetchScreen(
    joke: LoadState<Joke> = idle(),
    onFetchRequested: () -> Unit = { },
    onNavigateToInfo: () -> Unit = { },
    onNavigateToSearch: () -> Unit = { }
) {
    JokesTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp)
                .testTag(JokeFetchScreenTestTag),
            floatingActionButton = {
                RefreshFab(
                    onClick = onFetchRequested,
                    refreshing = joke is Loading,
                    modifier = Modifier.testTag(FetchItTestTag)
                )
            },
            floatingActionButtonPosition = FabPosition.Center,
            topBar = {
                TopBar(
                    navigation = NavigationHandlers(
                        onInfoRequested = onNavigateToInfo,
                        onSearchRequested = onNavigateToSearch
                    )
                )
            }
        ) { innerPadding ->
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                joke.getOrNull()?.let {
                    JokeView(joke = it)
                }
            }
        }

        joke.exceptionOrNull()?.let {
            ErrorAlert(
                title = R.string.error_api_title,
                message = R.string.error_could_not_reach_api,
                buttonText = R.string.error_retry_button_text,
                onDismiss = onFetchRequested
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun JokeScreenPreview() {
    val aJoke = Joke(
        text = "It's a 5 minute walk from my house to the pub\n" +
                "It's a 35 minute walk from the pub to my house\n" +
                "The difference is staggering.",
        source = URL("https://www.keeplaughingforever.com/corny-dad-jokes")
    )

    JokeFetchScreen(success(aJoke))
}
