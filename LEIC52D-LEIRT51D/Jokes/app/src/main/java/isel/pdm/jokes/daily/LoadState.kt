package isel.pdm.jokes.daily

import isel.pdm.jokes.Joke

sealed class LoadState

data object Idle : LoadState()
data object Loading : LoadState()
data class Loaded(val result: Result<Joke>) : LoadState()
