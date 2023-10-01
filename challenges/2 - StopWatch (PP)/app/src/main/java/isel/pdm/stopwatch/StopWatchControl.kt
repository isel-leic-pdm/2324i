package isel.pdm.stopwatch

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import isel.pdm.stopwatch.ui.RoundButton
import isel.pdm.stopwatch.ui.theme.StopWatchTheme

const val START_BUTTON_TEST_TAG = "start"
const val STOP_BUTTON_TEST_TAG = "stop"
const val RESET_BUTTON_TEST_TAG = "reset"

/**
 * The control buttons of the stopwatch.
 *
 * @param stopWatch the stopwatch model
 * @param onStart the action to be performed when start is selected
 * @param onStop the action to be performed when stop is selected
 * @param onResume the action to be performed when resume is selected
 * @param onReset the action to be performed when reset is selected
 */
@Composable
fun StopWatchControl(
    stopWatch: StopWatch,
    onStart: () -> Unit = {},
    onStop: () -> Unit = {},
    onResume: () -> Unit = {},
    onReset: () -> Unit = {}
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        ControlButton(
            modifier = Modifier.testTag(RESET_BUTTON_TEST_TAG),
            text = stringResource(id = R.string.reset_button_label),
            enabled = stopWatch.isStopped && !stopWatch.isZero,
            onClick = onReset
        )
        Spacer(modifier = Modifier.width(32.dp))
        if (stopWatch.isStarted) {
            ControlButton(
                modifier = Modifier.testTag(STOP_BUTTON_TEST_TAG),
                text = stringResource(id = R.string.stop_button_label),
                onClick = onStop,
            )
        } else {
            ControlButton(
                modifier = Modifier.testTag(START_BUTTON_TEST_TAG),
                text = stringResource(id = R.string.start_button_label),
                onClick = if (stopWatch.isZero) onStart else onResume,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StopWatchControlPreview() {
    StopWatchTheme {
        StopWatchControl(StopWatch(startedAt = 0, stoppedAt = 0))
    }
}


@Composable
fun ControlButton(
    modifier: Modifier = Modifier,
    text: String = "",
    enabled: Boolean = true,
    onClick: () -> Unit = { },
) {
    RoundButton(onClick = onClick, enabled = enabled, modifier = modifier) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ControlButtonPreview() {
    StopWatchTheme {
        ControlButton(onClick = {}, text = "Start")
    }
}

