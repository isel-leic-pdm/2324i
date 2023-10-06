package pt.isel.pdm.nasaimageoftheday.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.isel.pdm.nasaimageoftheday.R
import pt.isel.pdm.nasaimageoftheday.TAG
import pt.isel.pdm.nasaimageoftheday.helpers.TestTags
import pt.isel.pdm.nasaimageoftheday.model.NasaImage
import pt.isel.pdm.nasaimageoftheday.screens.components.NasaImageView


@Composable
fun MainScreen(
    nasaImage: NasaImage?,
    loadImage: () -> Unit
) {
    LaunchedEffect(Unit)
    {
        launch() {  }
    }

    val scope = rememberCoroutineScope()

    Log.d(TAG, "MainScreen composition")
    Box()
    {
        if (nasaImage == null) {
            Button(
                onClick = {
                          scope.launch(Dispatchers.IO) {
                              loadImage()
                          }
                },
                modifier = Modifier.align(Alignment.Center)
            )
            {
                Text(text = stringResource(R.string.load_image))
            }
        } else {
            NasaImageView(
                nasaImage = nasaImage,
                modifier = Modifier.testTag(TestTags.SingleNasaViewTestTag)
            )
        }

    }
}