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
import pt.isel.pdm.nasaimageoftheday.screens.components.BaseViewModel
import pt.isel.pdm.nasaimageoftheday.services.NasaImageOfTheDayService
import java.lang.Exception
import java.time.LocalDate

class MainScreenViewModel : BaseViewModel() {
    var nasaImage by mutableStateOf<NasaImage?>(null)


    private var date = LocalDate.now()

    fun fetchNasaImage(service: NasaImageOfTheDayService) = safeCall {
        nasaImage = service.getImageOf(date)
        date = date.minusDays(1)
    }


}

