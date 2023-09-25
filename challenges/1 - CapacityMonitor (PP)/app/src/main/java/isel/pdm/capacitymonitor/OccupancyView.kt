package isel.pdm.capacitymonitor

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import isel.pdm.capacitymonitor.ui.theme.CapacityMonitorTheme

/**
 * The tag used to identify the OccupancyView's text placeholder in automated tests.
 */
const val OccupancyTextTestTag = "occupancy"

/**
 * Displays the room's occupancy.
 * @param occupancy The room's occupancy.
 * @param modifier The modifier to be applied to the view.
 */
@Composable
fun OccupancyView(occupancy: Occupancy, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.testTag(OccupancyTextTestTag),
            text = occupancy.current.toString(),
            fontSize = 150.sp
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun OccupancyViewPreview() {
    CapacityMonitorTheme {
        OccupancyView(occupancy = Occupancy(10, 3))
    }
}