package isel.pdm.jokes.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import isel.pdm.jokes.domain.FakeJokesService
import isel.pdm.jokes.domain.Joke
import isel.pdm.jokes.domain.LoadState
import isel.pdm.jokes.domain.Loading
import isel.pdm.jokes.R
import isel.pdm.jokes.domain.Term
import isel.pdm.jokes.daily.JokeFetchScreenTestTag
import isel.pdm.jokes.domain.Idle
import isel.pdm.jokes.domain.Loaded
import isel.pdm.jokes.domain.exceptionOrNull
import isel.pdm.jokes.domain.getOrNull
import isel.pdm.jokes.domain.idle
import isel.pdm.jokes.domain.success
import isel.pdm.jokes.domain.toTermOrNull
import isel.pdm.jokes.ui.ErrorAlert
import isel.pdm.jokes.ui.NavigationHandlers
import isel.pdm.jokes.ui.TopBar
import isel.pdm.jokes.ui.theme.JokesTheme

/**
 * Tags used to identify the components of the SearchScreen in automated tests
 */
const val SearchBarTestTag = "SearchBarTestTag"

/**
 * Root composable for the Search screen, the one that displays the list of jokes
 * that contain a given term.
 *
 * @param jokes the jokes to be displayed, if already loaded.
 * @param onSearchRequested the callback invoked when the user elects to search for a term.
 * @param onNavigateToInfo the callback invoked when the user clicks the "Info" button.
 * @param onNavigateBack the callback invoked when the user clicks the "Back" button.
 * @param onJokeSelected the callback invoked when the user clicks a joke.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    jokes: LoadState<List<Joke>> = idle(),
    onSearchRequested: (Term) -> Unit = { },
    onClearSearch: () -> Unit = { },
    onNavigateToInfo: () -> Unit = { },
    onNavigateBack: () -> Unit = { },
    onJokeSelected: (Joke) -> Unit = { }
) {

    var query by rememberSaveable { mutableStateOf("") }

    JokesTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag(JokeFetchScreenTestTag),
            topBar = {
                TopBar(
                    navigation = NavigationHandlers(
                        onBackRequested = onNavigateBack,
                        onInfoRequested = onNavigateToInfo
                    )
                )
            }
        ) { contentPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SearchBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(SearchBarTestTag),
                    placeholder = { Text(stringResource(R.string.search_hint))},
                    query = query,
                    onQueryChange = { query = it },
                    onSearch = { query.toTermOrNull()?.let { onSearchRequested(it) } },
                    active = false,
                    onActiveChange = { },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    enabled = jokes !is Loading,
                    trailingIcon = {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = null,
                            modifier = Modifier.clickable { onClearSearch() }
                        )
                    },
                    content = { }
                )

                when (jokes){
                    is Idle -> { }
                    is Loading -> { LoadingStateView() }
                    is Loaded -> {
                        LoadedStateView(
                            jokes = jokes,
                            onJokeSelected = onJokeSelected,
                            query = query,
                            onSearchRequested = onSearchRequested
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun SearchScreenPreview() {
    SearchScreen(
        jokes = success(FakeJokesService.jokes)
    )
}

@Preview
@Composable
private fun SearchScreenLoadingPreview() {
    SearchScreen(
        jokes = Loading
    )
}

@Composable
private fun LoadingStateView() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(modifier = Modifier.width(64.dp))
    }
}

@Composable
private fun LoadedStateView(
    jokes: Loaded<List<Joke>>,
    query: String,
    onJokeSelected: (Joke) -> Unit = { },
    onSearchRequested: (Term) -> Unit = { }
) {
    LazyColumn(
        userScrollEnabled = true,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
    ) {
        jokes.getOrNull()?.let { jokesList ->
            itemsIndexed(items = jokesList) { index, joke ->
                JokeListItemView(
                    index = index,
                    joke = joke,
                    onEntrySelected = onJokeSelected
                )
            }
        }
    }

    jokes.exceptionOrNull()?.let {
        ErrorAlert(
            title = R.string.error_api_title,
            message = R.string.error_could_not_reach_api,
            buttonText = R.string.error_retry_button_text,
            onDismiss = { query.toTermOrNull()?.let { onSearchRequested(it) } }
        )
    }
}