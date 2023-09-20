package isel.pdm.jokes

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import isel.pdm.jokes.ui.theme.JokesTheme

/**
 * Tags used to identify the components of the JokeScreen in automated tests
 * (see app/src/androidTest/java/isel/pdm/chucknorris/JokeScreenTests.kt)
 */
const val FetchItTestTag = "FetchItTestTag"
const val JokeTestTag = "JokeTestTag"
const val JokeScreenTestTag = "JokeScreenTestTag"

@Composable
fun JokeScreen() {
    JokesTheme {
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
                if (joke.isNotBlank())
                    Text(text = joke, modifier = Modifier.testTag(JokeTestTag))

                Button(
                    onClick = { joke = fetchJoke() },
                    modifier = Modifier.testTag(FetchItTestTag)
                ) {
                    Text(text = "Fetch it!")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun JokeScreenPreview() {
    JokeScreen()
}