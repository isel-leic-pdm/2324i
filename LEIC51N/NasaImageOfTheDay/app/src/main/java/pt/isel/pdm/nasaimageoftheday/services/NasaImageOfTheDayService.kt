package pt.isel.pdm.nasaimageoftheday.services

import pt.isel.pdm.nasaimageoftheday.model.NasaImage
import java.time.LocalDate

interface NasaImageOfTheDayService {
    suspend fun getImageOfTheDay(): NasaImage
    suspend fun getImageOf(date: LocalDate): NasaImage

    suspend fun getImages(startDate: LocalDate, endDate: LocalDate): List<NasaImage>
}