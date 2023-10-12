package pt.isel.pdm.nasaimageoftheday.screens.main

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import pt.isel.pdm.nasaimageoftheday.R
import pt.isel.pdm.nasaimageoftheday.helpers.AndroidTags
import pt.isel.pdm.nasaimageoftheday.helpers.TestTags
import pt.isel.pdm.nasaimageoftheday.model.NasaImage
import pt.isel.pdm.nasaimageoftheday.screens.aboutprep.CustomTopBar
import pt.isel.pdm.nasaimageoftheday.screens.aboutprep.zNavigationHandlers
import pt.isel.pdm.nasaimageoftheday.screens.components.NasaImageView
import pt.isel.pdm.nasaimageoftheday.screens.components.NavigationHandlers
import pt.isel.pdm.nasaimageoftheday.screens.components.TopBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    nasaImage: NasaImage?,
    loadImage: () -> Unit,
    navigateToAbout: () -> Unit
) {
    Log.d(AndroidTags.TagName, "MainScreen composition")

    Scaffold(
        topBar = {
            TopBar(
                navigationHandlers = NavigationHandlers(
                    navigateToAboutHandler = navigateToAbout
                )
            )
        }

    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues))
        {
            if (nasaImage == null) {
                Button(
                    onClick = {
                        loadImage()
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

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen2(
    nasaImage: NasaImage?,
    loadImage: () -> Unit,
    aboutRequested: () -> Unit
) {
    Log.d(AndroidTags.TagName, "MainScreen composition")

    Scaffold(
        topBar = {
            CustomTopBar(
                navigation = zNavigationHandlers(
                    navigateToAboutHandler = aboutRequested
                )
            )
        }
    )
    { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues))
        {
            if (nasaImage == null) {
                Button(
                    onClick = {
                        loadImage()
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
}