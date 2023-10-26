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

    var joke by mutableStateOf<LoadState>(Idle)
        private set

    fun fetchJoke(service: JokesService) {
        viewModelScope.launch {
            joke = Loading
            joke = Loaded(runCatching { service.fetchJoke() })
        }
    }
}