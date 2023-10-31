package isel.pdm.jokes.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import isel.pdm.jokes.domain.Idle
import isel.pdm.jokes.domain.Joke
import isel.pdm.jokes.domain.JokesService
import isel.pdm.jokes.domain.LoadState
import isel.pdm.jokes.domain.Loading
import isel.pdm.jokes.domain.Term
import isel.pdm.jokes.domain.loaded
import kotlinx.coroutines.launch

/**
 * The view model for the search screen of the Jokes app.
 * @property service The service used to fetch jokes.
 */
class SearchScreenViewModel(private val service: JokesService) : ViewModel() {

    companion object {
        fun factory(service: JokesService) = viewModelFactory {
            initializer { SearchScreenViewModel(service) }
        }
    }

    /**
     * The jokes to be displayed. The jokes are loaded from a remote location by the
     * provided service and therefore the state is represented by a [LoadState].
     */
    var jokes by mutableStateOf<LoadState<List<Joke>>>(Idle)
        private set

    /**
     * Searches for jokes with the given search term.
     */
    fun search(term: Term) {
        viewModelScope.launch {
            jokes = Loading
            val result = runCatching { service.searchJokes(term) }
            jokes = loaded(result)
        }
    }
}
