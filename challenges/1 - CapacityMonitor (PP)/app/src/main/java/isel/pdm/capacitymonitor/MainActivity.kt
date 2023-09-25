package isel.pdm.capacitymonitor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

/**
 * The application's tag for logging purposes.
 */
const val TAG = "CapacityMonitorApp"

/**
 * The activity that hosts the occupancy screen, the application's main screen.
 * The application UI design is based on https://henryegloff.com/simple-counter/
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}
