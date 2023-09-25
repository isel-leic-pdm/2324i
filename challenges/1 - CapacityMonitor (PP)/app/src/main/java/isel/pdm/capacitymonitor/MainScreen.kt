package isel.pdm.capacitymonitor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import isel.pdm.capacitymonitor.ui.theme.CapacityMonitorTheme

/**
 * The tag used to identify the screen's increment button in automated tests.
 */
const val IncrementButtonTestTag = "increment-button"

/**
 * The tag used to identify the screen's decrement button in automated tests.
 */
const val DecrementButtonTestTag = "decrement-button"

/**
 * The main screen of the application.
 *
 * @param occupancy the occupancy of the room. If null, the occupancy is initialized with the
 * screen's defaults.
 */
@Composable
fun MainScreen(occupancy: Occupancy? = null) {

    CapacityMonitorTheme {

        var internalOccupancy by remember {
            mutableStateOf(occupancy ?: Occupancy(10, 0))
        }

        Column(
            modifier = Modifier.fillMaxSize().padding(64.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OccupancyView(occupancy = internalOccupancy, modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                RoundButton(
                    modifier = Modifier.testTag(DecrementButtonTestTag),
                    enabled = internalOccupancy.isNotEmpty(),
                    onClick = { --internalOccupancy }
                ) {
                    Icon(imageVector = Icons.Default.Remove, contentDescription = "Decrement")
                }

                Spacer(modifier = Modifier.width(64.dp))

                RoundButton(
                    modifier = Modifier.testTag(IncrementButtonTestTag),
                    enabled = internalOccupancy.isNotFull(),
                    onClick = { ++internalOccupancy }
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Increment")
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainScreenPreview() {
    MainScreen()
}

