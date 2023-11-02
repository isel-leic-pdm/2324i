package pt.isel.pdm.nasaimageoftheday.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import pt.isel.pdm.nasaimageoftheday.R

@Composable
fun LoadingScreen(
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
