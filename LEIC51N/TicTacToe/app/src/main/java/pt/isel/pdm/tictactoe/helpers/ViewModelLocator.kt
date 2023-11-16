package pt.isel.pdm.tictactoe.helpers

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner

@Suppress("UNCHECKED_CAST")
fun <T> viewModelInit(block: () -> T) =
    object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            val a: SavedStateViewModelFactory? = null
            return block() as T
        }
    }


@Suppress("UNCHECKED_CAST")
fun <T> viewModelInitWithSavedState(
    owner: SavedStateRegistryOwner,
    block: (SavedStateHandle) -> T
) =
    object : AbstractSavedStateViewModelFactory(owner, null) {
        override fun <T : ViewModel> create(
            key: String,
            modelClass: Class<T>,
            handle: SavedStateHandle
        ): T {
            return block(handle) as T
        }
    }