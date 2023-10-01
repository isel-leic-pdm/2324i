package isel.pdm.stopwatch

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StopWatchDisplay(value: StopWatchValue, modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        HeaderText(
            text = String.format("%02d:", value.minutes),
            header = stringResource(id = R.string.minutes_label)
        )
        HeaderText(
            text = String.format("%02d,", value.seconds),
            header = stringResource(id = R.string.seconds_label)
        )
        HeaderText(
            text = String.format("%02d", value.milliseconds / 10),
            header = stringResource(id = R.string.deciseconds_label)
        )
    }
}

@Composable
private fun HeaderText(text: String, header: String) {
    Column {
        Text(
            text = header,
            modifier = Modifier.offset(y = 16.dp).padding(start = 8.dp)
        )
        Text(
            text = text,
            style = TextStyle(letterSpacing = 4.sp),
            fontSize = MaterialTheme.typography.displayLarge.fontSize,
        )
    }
}


@Composable
@Preview(showBackground = true)
private fun HeaderTextPreview() {
    HeaderText(text = "00:", header = "minutes")
}

@Composable
@Preview(showBackground = true)
private fun StopWatchDisplayPreview() {
    StopWatchDisplay(StopWatchValue(minutes = 2, seconds = 51, milliseconds = 236))
}
