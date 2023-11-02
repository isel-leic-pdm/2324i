package pt.isel.pdm.nasaimageoftheday.screens.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.isel.pdm.nasaimageoftheday.model.NasaImage
import pt.isel.pdm.nasaimageoftheday.screens.components.BaseViewModel
import pt.isel.pdm.nasaimageoftheday.services.NasaImageOfTheDayService
import java.time.LocalDate
import java.time.LocalTime

class ListViewModel : BaseViewModel() {
    var images by mutableStateOf(listOf<NasaImage>())

    private var startDate: LocalDate;
    private var endDate: LocalDate;

    val NrOfImagesPerRequest = 10L

    init {
        endDate = LocalDate.now()
        startDate = endDate.minusDays(NrOfImagesPerRequest)
    }

    fun loadMoreImages(nasaService: NasaImageOfTheDayService) = safeCall {
        val items = nasaService.getImages(startDate, endDate)
        images = images.union(items).toList()

        endDate = startDate
        startDate = startDate.minusDays(NrOfImagesPerRequest)
    }


}