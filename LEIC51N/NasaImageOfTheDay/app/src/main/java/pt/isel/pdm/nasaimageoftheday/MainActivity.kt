package pt.isel.pdm.nasaimageoftheday

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import pt.isel.pdm.nasaimageoftheday.model.NasaImage
import pt.isel.pdm.nasaimageoftheday.model.NasaImages
import pt.isel.pdm.nasaimageoftheday.screens.EffectsScreen
import pt.isel.pdm.nasaimageoftheday.screens.MainScreen
import pt.isel.pdm.nasaimageoftheday.screens.components.NasaImageView
import pt.isel.pdm.nasaimageoftheday.screens.components.NasaImageViewPreview
import pt.isel.pdm.nasaimageoftheday.ui.theme.NasaImageOfTheDayTheme

const val TAG: String = "NasaImageOfTheDay"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        setContent {
            NasaImageOfTheDayTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var nasaImage by remember { mutableStateOf<NasaImage?>(null) }
/*
                    MainScreen(
                        nasaImage = nasaImage,
                        loadImage = { nasaImage = getNasaImage() }

                    )
 */
                    EffectsScreen()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }

    fun getNasaImage(): NasaImage {
        return NasaImages.Images.random()
    }

}




