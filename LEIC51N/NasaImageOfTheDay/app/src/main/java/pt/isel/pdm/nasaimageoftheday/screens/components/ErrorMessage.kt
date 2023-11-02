package pt.isel.pdm.nasaimageoftheday.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pt.isel.pdm.nasaimageoftheday.R


@Composable
fun ErrorMessage(error: String?) {

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