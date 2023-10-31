package isel.pdm.jokes.search

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import isel.pdm.jokes.JokesDependencyProvider
import isel.pdm.jokes.about.AboutActivity
import isel.pdm.jokes.search.detail.JokeDisplayActivity

/**
 * The application's search activity, used to display the search screen.
 *
 * Navigation to this activity is done through the [SearchActivity.navigate] method.
 */
class SearchActivity : ComponentActivity() {

    companion object {
        /**
         * Navigates to the [SearchActivity] activity.
         * @param origin the activity from which the navigation is performed.
         */
        fun navigate(origin: Activity) {
            with(origin) {
                val intent = Intent(this, SearchActivity::class.java)
                startActivity(intent)
            }
        }
    }

    /**
     * The application's dependency provider.
     */
    private val dependencies by lazy { application as JokesDependencyProvider }

    /**
     * The view model for the search screen of the Jokes app.
     */
    private val viewModel by viewModels<SearchScreenViewModel>(
        factoryProducer = { SearchScreenViewModel.factory(dependencies.jokesService) }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SearchScreen(
                jokes = viewModel.jokes,
                onNavigateBack = { finish() },
                onNavigateToInfo = { AboutActivity.navigate(this) },
                onSearchRequested = { viewModel.search(it) },
                onJokeSelected = { joke -> JokeDisplayActivity.navigate(this, joke)  }
            )
        }
    }
}