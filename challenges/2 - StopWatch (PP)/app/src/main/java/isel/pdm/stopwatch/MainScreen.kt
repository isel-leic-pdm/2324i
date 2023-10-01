package isel.pdm.stopwatch

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import isel.pdm.stopwatch.ui.theme.StopWatchTheme
import kotlinx.coroutines.delay

/**
 * This version of the screen uses a coroutine launched with LaunchedEffect to periodically
 * update the UI with the stopwatch value.
 */
@Composable
fun MainScreen(stopWatch: StopWatch? = null) {

    var internalStopWatch by remember {
        mutableStateOf(stopWatch ?: StopWatch(startedAt = 0, stoppedAt = 0))
    }

    var stopWatchValue by remember { mutableStateOf(internalStopWatch.value) }

    LaunchedEffect(key1 = internalStopWatch) {
        while (internalStopWatch.isStarted) {
            delay(10)
            stopWatchValue = internalStopWatch.value
        }
    }

    StopWatchTheme {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize(),
        ) {
            StopWatchDisplay(
                value = stopWatchValue,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(32.dp))
            StopWatchControl(
                stopWatch = internalStopWatch,
                onStart = { internalStopWatch = internalStopWatch.start() },
                onStop = { internalStopWatch = internalStopWatch.stop() },
                onReset = { internalStopWatch = StopWatch(startedAt = 0, stoppedAt = 0) },
                onResume = { internalStopWatch = internalStopWatch.resume() }
            )
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun MainScreenPreview() {
    MainScreen(stopWatch = StopWatch(startedAt = 0, stoppedAt = 154432))
}