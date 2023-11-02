package pt.isel.pdm.nasaimageoftheday

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import pt.isel.pdm.nasaimageoftheday.services.DependencyContainer
import pt.isel.pdm.nasaimageoftheday.services.FakeNasaImageOfTheDayService
import pt.isel.pdm.nasaimageoftheday.services.NasaImageOfTheDayService


class TestNasaApplication : Application(), DependencyContainer {
    override val imageService: NasaImageOfTheDayService by lazy { FakeNasaImageOfTheDayService() }

    override fun onCreate() {
        super.onCreate()
    }
}

class NasaTestsCustomRunner : AndroidJUnitRunner() {
    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(cl, TestNasaApplication::class.java.name, context)
    }
}
