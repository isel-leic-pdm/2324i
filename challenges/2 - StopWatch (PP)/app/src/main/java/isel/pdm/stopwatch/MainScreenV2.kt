package isel.pdm.stopwatch

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import isel.pdm.stopwatch.ui.theme.StopWatchTheme
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * This version of the screen uses a coroutine scope to launch the coroutine responsible
 * for periodically updating the UI.
 *
 * SUBJECTIVITY ALERT: Personally, I prefer the [MainScreen] version of the screen, because it
 * is more declarative and the code is simpler to read. We only need to understand the semantics
 * of the [LaunchedEffect] composable, which is not that hard. On the other hand, the [MainScreenV2]
 * implementation uses an imperative style, which is more difficult to read and understand.
 */
@Composable
fun MainScreenV2(stopWatch: StopWatch? = null) {

    var internalStopWatch = stopWatch ?: StopWatch(startedAt = 0, stoppedAt = 0)
    var stopWatchValue by remember { mutableStateOf(internalStopWatch.value) }

    val scope = rememberCoroutineScope()

    fun launchStopWatchUpdate(): Job = scope.launch {
        while (internalStopWatch.isStarted) {
            stopWatchValue = internalStopWatch.value
            delay(10)
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
                onStart = { internalStopWatch = internalStopWatch.start(); launchStopWatchUpdate() },
                onStop = { internalStopWatch = internalStopWatch.stop(); },
                onReset = { internalStopWatch = StopWatch(startedAt = 0, stoppedAt = 0); stopWatchValue = internalStopWatch.value },
                onResume = { internalStopWatch = internalStopWatch.resume(); launchStopWatchUpdate() }
            )
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun MainScreenV2Preview() {
    MainScreenV2(stopWatch = StopWatch(startedAt = 0, stoppedAt = 154432))
}