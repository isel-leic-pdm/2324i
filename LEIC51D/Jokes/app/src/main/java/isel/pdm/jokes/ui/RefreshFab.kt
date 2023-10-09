package isel.pdm.jokes.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

enum class RefreshingState { Idle, Refreshing }

@Composable
fun RefreshFab(onClick: () -> Unit, state: RefreshingState = RefreshingState.Idle) {
    Button(
        onClick = onClick,
        enabled = state == RefreshingState.Idle,
        shape = CircleShape,
        modifier = Modifier
            .defaultMinSize(minWidth = 72.dp, minHeight = 72.dp)
    ){
        if (state == RefreshingState.Refreshing) {
            val transition = rememberInfiniteTransition(label = "fab rotation")
            val rotation by transition.animateFloat(
                initialValue = 0f,
                targetValue = 360f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        delayMillis = 50,
                        durationMillis = 750,
                        easing = FastOutSlowInEasing
                    )
                ), label = ""
            )
            Icon(
                Icons.Default.Refresh,
                contentDescription = "Refresh",
                modifier = Modifier.rotate(rotation)
            )
        }
        else {
            Icon(
                Icons.Default.Refresh,
                contentDescription = "Refresh",
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun IdleRefreshFabPreview() {
    RefreshFab(onClick = { }, state = RefreshingState.Idle)
}

@Preview(showBackground = true)
@Composable
private fun RefreshingRefreshFabPreview() {
    RefreshFab(onClick = { }, state = RefreshingState.Refreshing)
}
