package pt.isel.pdm.nasaimageoftheday.screens.main

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.isel.pdm.nasaimageoftheday.model.NasaImage
import pt.isel.pdm.nasaimageoftheday.services.NasaImageOfTheDayService
import java.lang.Exception

class MainScreenViewModel : ViewModel() {
    var nasaImage by mutableStateOf<NasaImage?>(null)
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)

    fun fetchNasaImage(service: NasaImageOfTheDayService) {
        isLoading = true

        viewModelScope.launch {
            try {
                nasaImage = service.getImageOfTheDay()
            } catch (e: Exception) {
                error = e.toString()
            }
            isLoading = false
        }

    }

}