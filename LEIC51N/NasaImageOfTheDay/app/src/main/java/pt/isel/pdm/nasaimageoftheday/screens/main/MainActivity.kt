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
import pt.isel.pdm.nasaimageoftheday.services.DependencyContainer
import pt.isel.pdm.nasaimageoftheday.services.FakeNasaImageOfTheDayService
import pt.isel.pdm.nasaimageoftheday.services.NasaImageOfTheDayService
import pt.isel.pdm.nasaimageoftheday.ui.theme.NasaImageOfTheDayTheme


class MainActivity : ComponentActivity() {

    private val nasaService: NasaImageOfTheDayService by
        lazy {
            (application as DependencyContainer).imageService
        }
    private val viewModel: MainScreenViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(AndroidTags.TagName, "MainActivity.onCreate")
        super.onCreate(savedInstanceState)
        setContent {
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

                    LoadingScreen(viewModel.isLoading)

                    ErrorMessage(viewModel.error)

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

@Composable
private fun LoadingScreen(
    isLoading: Boolean
) {

    if (!isLoading)
        return

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White.copy(alpha = 0.8f))
    )
    {
        Text(
            text = stringResource(id = R.string.Loading),
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun ErrorMessage(error: String?) {

    var dismiss by rememberSaveable { mutableStateOf(false) }

    if (error == null || dismiss)
        return

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Red.copy(alpha = 0.5f))
    )
    {
        Text(
            text = error,
            modifier = Modifier.align(Alignment.Center)
        )
        Button(
            onClick = {
                dismiss = true
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(12.dp)
        ) {
            Text(text = stringResource(id = R.string.Dismiss))
        }
    }
}



