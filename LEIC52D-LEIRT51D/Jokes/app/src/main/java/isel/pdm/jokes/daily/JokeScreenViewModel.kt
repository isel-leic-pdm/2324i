package isel.pdm.jokes.daily

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import isel.pdm.jokes.Joke
import isel.pdm.jokes.JokesService
import kotlinx.coroutines.launch

class JokeScreenViewModel : ViewModel() {

    private var _joke by mutableStateOf<Joke?>(null)
    val joke: Joke?
        get() = _joke

    fun fetchJoke(service: JokesService) {
        viewModelScope.launch {
            _joke = service.fetchJoke()
        }
    }
}