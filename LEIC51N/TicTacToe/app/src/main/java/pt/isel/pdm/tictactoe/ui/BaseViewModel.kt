package pt.isel.pdm.tictactoe.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {
    var isLoading by mutableStateOf<Boolean>(false)
    var error by mutableStateOf<String?>(null)

    protected fun loadingAndErrorAwareScope(function: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch {
            isLoading = true
            safeCallInternal(this, function)
            isLoading = false
        }
    }

    protected fun errorAwareScope(function: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch {
            safeCallInternal(this) { function() }
        }
    }

    private suspend fun safeCallInternal(
        cs: CoroutineScope,
        function: suspend CoroutineScope.() -> Unit
    ) {
        error = null
        try {
            function(cs)
        } catch (e: Exception) {
            error = e.toString()
        }
    }


}