package pt.isel.pdm.nasaimageoftheday.screens.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)

    protected fun safeCall(function: suspend () -> Unit) {
        isLoading = true


        viewModelScope.launch {
            try {
                function()
            } catch (e: Exception) {
                error = e.toString()
            }
            isLoading = false
        }


    }
}