package pt.isel.pdm.cronometer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import pt.isel.pdm.cronometer.ui.theme.CronometerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CronometerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    StopwatchScreenFancy()
                }
            }
        }
    }
}


@Composable
private fun StopwatchScreenSimple() {
    var startedAt by remember { mutableStateOf<Long>(0) }
    var time by remember { mutableStateOf<Long>(0) }
    var running by remember { mutableStateOf(false) }


    LaunchedEffect(running) {
        while (running) {
            delay(100)
            time = (System.currentTimeMillis() - startedAt) / 1000;
        }
    }
    Column(modifier = Modifier.fillMaxSize()) {

        Text("Time: $time")

        if (running) {

            Button(onClick = { running = false }) {
                Text("Stop")
            }

            Button(onClick = { startedAt = System.currentTimeMillis(); time = 0 }) {
                Text("Reset")
            }
        } else Button(onClick = { time = 0; running = true; startedAt = System.currentTimeMillis() }) {
            Text("Start")
        }
    }
}

//taken from https://www.behance.net/gallery/40534207/StopWatch-Day84100-My-UIUX-Free-SketchApp-Challenge
@Composable
private fun StopwatchScreenFancy() {


    var startedAt by remember { mutableStateOf<Long>(0) }
    var time by remember { mutableStateOf<Long>(0) }
    var running by remember { mutableStateOf(false) }

    LaunchedEffect(running) {
        while (running) {
            delay(100)
            time = (System.currentTimeMillis() - startedAt) / 1000;
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {


        TimeView(
            time,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
        )

        ControlBar(
            isRunning = running,
            onStart = { time = 0; running = true; startedAt = System.currentTimeMillis() },
            onReset = { startedAt = System.currentTimeMillis(); time = 0 },
            onStop = { running = false },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        )

    }
}

@Composable
fun TimeView(
    totalSeconds: Long,
    modifier: Modifier = Modifier
) {
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60

    Box(
        modifier = modifier
    )
    {

        Row(
            modifier = Modifier.align(Alignment.Center),
        ) {

            HeaderText(text = String.format("%02d:", hours), header = "hours")
            HeaderText(text = String.format("%02d:", minutes), header = "minutes")
            HeaderText(text = String.format("%02d", seconds), header = "seconds")


        }
    }
}

@Composable
fun HeaderText(
    text: String,
    header: String
) {
    Column(horizontalAlignment = Alignment.Start) {
        Text(
            text = header,
            Modifier
                .offset(y = 20.dp)
                .padding(start = 10.dp),
        )
        Text(
            text = text,
            fontSize = 70.sp
        )

    }
}

@Composable
fun ControlBar(
    isRunning: Boolean,
    onStart: () -> Unit,
    onReset: () -> Unit,
    onStop: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center


    ) {
        var buttonModifier = Modifier.padding(12.dp)

        if (isRunning) {
            RoundButton(
                onClick = onStop,
                icon = Icons.Default.Close,
                modifier = buttonModifier
            )
            RoundButton(
                onClick = onReset,
                icon = Icons.Default.Refresh,
                modifier = buttonModifier
            )
        } else
            RoundButton(
                onClick = onStart,
                icon = Icons.Default.PlayArrow,
                modifier = buttonModifier
            )
    }
}

@Composable
fun RoundButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    icon: ImageVector,
    contentModifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.size(50.dp),
        contentPadding = PaddingValues(0.dp),
        enabled = enabled

    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Favorite",
            modifier = contentModifier.size(20.dp)
        )
    }
}
