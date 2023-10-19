package pt.isel.pdm.nasaimageoftheday.screens.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import pt.isel.pdm.nasaimageoftheday.helpers.AndroidTags
import pt.isel.pdm.nasaimageoftheday.model.NasaImage
import pt.isel.pdm.nasaimageoftheday.model.NasaImages
import pt.isel.pdm.nasaimageoftheday.screens.about.AboutActivity
import pt.isel.pdm.nasaimageoftheday.ui.theme.NasaImageOfTheDayTheme


class MainActivity : ComponentActivity() {
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

                    var nasaImage by remember { mutableStateOf<NasaImage?>(null) }

                    MainScreen(
                        nasaImage = nasaImage,
                        loadImage = { nasaImage = getNasaImage() },
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

    fun getNasaImage(): NasaImage {
        return NasaImages.Images.random()
    }

}



