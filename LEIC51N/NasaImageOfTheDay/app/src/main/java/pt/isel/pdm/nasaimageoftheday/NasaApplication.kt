package pt.isel.pdm.nasaimageoftheday

import android.app.Application
import android.util.Log
import pt.isel.pdm.nasaimageoftheday.helpers.AndroidTags
import pt.isel.pdm.nasaimageoftheday.services.DependencyContainer
import pt.isel.pdm.nasaimageoftheday.services.FakeNasaImageOfTheDayService
import pt.isel.pdm.nasaimageoftheday.services.NasaImageOfTheDayService
import pt.isel.pdm.nasaimageoftheday.services.RemoteNasaImageService

class NasaApplication : Application(), DependencyContainer {
    override fun onCreate() {
        super.onCreate()
        Log.d(AndroidTags.TagName, "NasaApplication.onCreate")
    }

    override val imageService by lazy {
        //FakeNasaImageOfTheDayService()
        RemoteNasaImageService(
            "S6RMxbTyb9pAqhr823IBOzI3BNdtplUVxRRqw4z1",
            "https://api.nasa.gov/planetary/apod"
        )
    }

}