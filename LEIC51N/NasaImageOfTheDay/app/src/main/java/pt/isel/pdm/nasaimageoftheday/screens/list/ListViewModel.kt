package pt.isel.pdm.nasaimageoftheday.screens.list

import android.os.Parcelable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import kotlinx.parcelize.Parcelize
import pt.isel.pdm.nasaimageoftheday.model.NasaImage
import pt.isel.pdm.nasaimageoftheday.screens.components.BaseViewModel
import pt.isel.pdm.nasaimageoftheday.services.NasaImageOfTheDayService
import java.time.LocalDate

class ListViewModel(
    private val saveHandle: SavedStateHandle
) : BaseViewModel() {

    companion object {
        const val SaveArgument = "__arg"
    }

    var images by mutableStateOf(listOf<NasaImage>())

    var startDate by mutableStateOf<LocalDate>(LocalDate.now())
    var endDate by mutableStateOf<LocalDate>(LocalDate.now())

    val NrOfImagesPerRequest = 20L

    init {
        endDate = LocalDate.now()
        startDate = endDate.minusDays(NrOfImagesPerRequest)

        val data = saveHandle.get<ListViewTransientState?>(SaveArgument)
        if(data != null)
        {
            images = data.images
            startDate = data.startDate
            endDate = data.endDate
        }
    }

    fun loadMoreImages(nasaService: NasaImageOfTheDayService) = safeCall {
        val items = nasaService.getImages(startDate, endDate)
        images = images.union(items).toList()

        endDate = startDate
        startDate = startDate.minusDays(NrOfImagesPerRequest)

        saveHandle.set(SaveArgument, ListViewTransientState(images, startDate, endDate))
    }
}

@Parcelize
data class ListViewTransientState(
    val images: List<NasaImage>,
    val startDate: LocalDate,
    val endDate: LocalDate
) : Parcelable