package isel.pdm.jokes.daily

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import isel.pdm.jokes.domain.Joke
import isel.pdm.jokes.domain.JokesService
import isel.pdm.jokes.domain.LoadState
import isel.pdm.jokes.domain.idle
import isel.pdm.jokes.domain.loaded
import isel.pdm.jokes.domain.loading
import kotlinx.coroutines.launch

/**
 * The view model for the main screen of the Jokes app.
 * @property service The service used to fetch jokes.
 */
class JokeScreenViewModel(private val service: JokesService) : ViewModel() {

    companion object {
        fun factory(service: JokesService) = viewModelFactory {
            initializer { JokeScreenViewModel(service) }
        }
    }

    /**
     * The joke to be displayed. The joke is loaded from a remote location by the
     * provided service and therefore its state is represented by a [LoadState].
     */
    var joke by mutableStateOf<LoadState<Joke>>(idle())
        private set

    /**
     * Fetches a joke from the given service.
     */
    fun fetchJoke() {
        viewModelScope.launch {
            joke = loading()
            val result = kotlin.runCatching { service.fetchJoke() }
            joke = loaded(result)
        }
    }
}