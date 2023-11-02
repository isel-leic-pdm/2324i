package pt.isel.pdm.nasaimageoftheday.screens.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.isel.pdm.nasaimageoftheday.R
import pt.isel.pdm.nasaimageoftheday.helpers.AndroidTags
import pt.isel.pdm.nasaimageoftheday.model.NasaImage
import pt.isel.pdm.nasaimageoftheday.screens.about.AboutActivity
import pt.isel.pdm.nasaimageoftheday.screens.components.BaseComponentActivity
import pt.isel.pdm.nasaimageoftheday.services.DependencyContainer
import pt.isel.pdm.nasaimageoftheday.services.FakeNasaImageOfTheDayService
import pt.isel.pdm.nasaimageoftheday.services.NasaImageOfTheDayService
import pt.isel.pdm.nasaimageoftheday.ui.theme.NasaImageOfTheDayTheme


class MainActivity : BaseComponentActivity<MainScreenViewModel>() {

    private val nasaService: NasaImageOfTheDayService by lazy { dependencyContainer.imageService }
    override val viewModel: MainScreenViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(AndroidTags.TagName, "MainActivity.onCreate")
        super.onCreate(savedInstanceState)
        safeSetContent {
            NasaImageOfTheDayTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(
                        nasaImage = viewModel.nasaImage,
                        loadImage = {
                            viewModel.fetchNasaImage(nasaService)
                        },
                        navigateToAbout = { AboutActivity.navigate(this) }
                    )
                }
            }
        }
    }


    override fun onStart() {
        super.onStart()
        Log.d(AndroidTags.TagName, "MainActivity.onStart")
    }

    override fun onStop() {
        super.onStop()
        Log.d(AndroidTags.TagName, "MainActivity.onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(AndroidTags.TagName, "MainActivity.onDestroy")
    }


}




