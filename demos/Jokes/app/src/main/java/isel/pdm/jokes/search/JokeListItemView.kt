package isel.pdm.jokes.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import isel.pdm.jokes.domain.Joke
import isel.pdm.jokes.ui.theme.JokesTheme
import java.net.URL

/**
 * Tag used to identify [JokeListItemView] components in automated tests.
 */
const val JokeListEntryTestTag = "JokeListEntryTestTag"

/**
 * Displays a joke in a list of jokes.
 * @param index the index of the joke in the list. We use this simply for demo purposes because
 * it facilitates the visual identification of the joke in the list.
 * @param joke the joke to be displayed.
 * @param onEntrySelected the callback invoked when the user clicks the joke entry.
 */
@Composable
fun JokeListItemView(
    index: Int,
    joke: Joke,
    onEntrySelected: (Joke) -> Unit = { }
) {
    Card(
        shape = ShapeDefaults.Small,
        modifier = Modifier
            .testTag(JokeListEntryTestTag)
            .clickable { onEntrySelected(joke) }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "${index + 1}",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .padding(start = 16.dp)
                    .wrapContentSize()
            )
            Text(
                text = joke.text,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Start,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun JokeListEntryViewPreview() {
    val aJoke = Joke(
        text = "It's a 5 minute walk from my house to the pub\n" +
                "It's a 35 minute walk from the pub to my house\n" +
                "The difference is staggering.",
        source = URL("https://www.keeplaughingforever.com/corny-dad-jokes")
    )
    JokesTheme {
        JokeListItemView(0, aJoke)
    }
}
