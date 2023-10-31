package isel.pdm.jokes.daily

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import isel.pdm.jokes.JokesDependencyProvider
import isel.pdm.jokes.about.AboutActivity
import isel.pdm.jokes.search.SearchActivity

/**
 * The application's main activity, used to display the daily joke.
 */
class JokeActivity : ComponentActivity() {

    /**
     * The application's dependency provider.
     */
    private val dependencies by lazy { application as JokesDependencyProvider }

    /**
     * The view model for the main screen of the Jokes app.
     */
    private val viewModel by viewModels<JokeScreenViewModel> {
        JokeScreenViewModel.factory(dependencies.jokesService)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JokeFetchScreen(
                joke = viewModel.joke,
                onFetchRequested = { viewModel.fetchJoke() },
                onNavigateToInfo = { AboutActivity.navigate(this) },
                onNavigateToSearch = { SearchActivity.navigate(this) }
            )
        }
    }
}
