package isel.pdm.jokes

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import isel.pdm.jokes.ui.theme.JokesTheme
import java.net.URL

/**
 * Displays a joke
 * @param joke the joke to be displayed
 */
@Composable
fun JokeView(joke: Joke) {
    Log.i(TAG, "JokeView: composing")
    Column(modifier = Modifier
        .padding(32.dp)
        .testTag(JokeTestTag)
    ) {
        Text(
            text = joke.text,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
        Text(
            text = joke.source.host,
            style = MaterialTheme.typography.bodySmall,
            textDecoration = TextDecoration.Underline,
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun JokeViewPreview() {
    val aJoke = Joke(
        text = "It's a 5 minute walk from my house to the pub\n" +
                "It's a 35 minute walk from the pub to my house\n" +
                "The difference is staggering.",
        source = URL("https://www.keeplaughingforever.com/corny-dad-jokes")
    )

    JokesTheme {
        JokeView(aJoke)
    }
}

