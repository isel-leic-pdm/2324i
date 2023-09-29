package pt.isel.pdm.nasaimageoftheday.screens

import android.util.Log
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

val TAG = "LaunchEffectTest"

@Composable
fun EffectsScreen() {

    var switchComposition by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(switchComposition) {
        launch {
            Log.d(TAG, "Start EffectsScreen")
            delay(2000)
            Log.d(TAG, "End EffectsScreen")
        }
    }

    if (switchComposition)
        Composition2();
    else
        Composition1 {
            Log.d(TAG, "Button clicked")
            switchComposition = true
        }
}

@Composable
fun Composition1(function: () -> Unit) {

    LaunchedEffect(Unit) {
        launch {
            Log.d(TAG, "Start Composition1")
            delay(2000)
            Log.d(TAG, "End Composition1")
        }
    }
    Button(onClick = function) {
        Text(text = "Press me")
    }
}

@Composable
fun Composition2() {
}

