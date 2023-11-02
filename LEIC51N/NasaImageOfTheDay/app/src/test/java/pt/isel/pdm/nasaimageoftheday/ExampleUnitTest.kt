package pt.isel.pdm.nasaimageoftheday

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import pt.isel.pdm.nasaimageoftheday.screens.main.MainScreenViewModel
import pt.isel.pdm.nasaimageoftheday.services.FakeNasaImageOfTheDayService
import pt.isel.pdm.nasaimageoftheday.services.RemoteNasaImageService
import java.time.LocalDate

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ExampleUnitTest {
    //
    //  Offers
    //
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun test_viewmodel_fetch() {
        runTest {
            val viewModel = MainScreenViewModel()
            viewModel.fetchNasaImage(FakeNasaImageOfTheDayService())
            advanceTimeBy(3000)
            assertNotNull(viewModel.nasaImage)
        }
    }

    @Test
    fun test_viewmodel_loading() {
        runTest {
            val viewModel = MainScreenViewModel()
            viewModel.fetchNasaImage(FakeNasaImageOfTheDayService())
            advanceTimeBy(1000)
            assertTrue(viewModel.isLoading)
            advanceTimeBy(3000)
            assertNotNull(viewModel.nasaImage)
            assertFalse(viewModel.isLoading)
        }
    }

    @Test
    fun remote_nasa_service_check_if_get_images_returns_more_than_one() {
        runTest {
            val service = RemoteNasaImageService(
                NasaApplication.RemoteServiceApiKey,
                NasaApplication.RemoteServiceBaseUrl
            )
            val endDate = LocalDate.now()
            val nrOfDays = 2L

            var startDate = endDate.minusDays(nrOfDays)

            val data = service.getImages(
                startDate,
                endDate
            )

            assertTrue(data.size == (nrOfDays.toInt() + 1))
        }
    }

}