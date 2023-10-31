package isel.pdm.jokes.search.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import isel.pdm.jokes.domain.Joke
import isel.pdm.jokes.daily.JokeView
import isel.pdm.jokes.ui.NavigationHandlers
import isel.pdm.jokes.ui.TopBar
import isel.pdm.jokes.ui.theme.JokesTheme
import java.net.URL

/**
 * Tags used to identify the components of the [JokeDisplayScreen] in automated tests
 */
const val JokeDisplayScreenTestTag = "JokeDisplayScreenTestTag"

/**
 * Root composable for the the screen that displays a joke fetched from the used API.
 * @param joke the joke to be displayed.
 * @param onNavigateBack the callback invoked when the user clicks the "Back" button.
 */
@Composable
fun JokeDisplayScreen(
    joke: Joke,
    onNavigateBack: () -> Unit = { }
) {
    JokesTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp)
                .testTag(JokeDisplayScreenTestTag),
            topBar = {
                TopBar(navigation = NavigationHandlers(onBackRequested = onNavigateBack))
            }
        ) { innerPadding ->
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                JokeView(joke = joke)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun JokeDisplayScreenPreview() {
    val aJoke = Joke(
        text = "It's a 5 minute walk from my house to the pub\n" +
                "It's a 35 minute walk from the pub to my house\n" +
                "The difference is staggering.",
        source = URL("https://www.keeplaughingforever.com/corny-dad-jokes")
    )

    JokeDisplayScreen(aJoke)
}
