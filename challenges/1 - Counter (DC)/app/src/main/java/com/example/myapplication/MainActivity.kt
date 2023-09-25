package com.example.myapplication

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val capacity = 10

        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var currentCapacity by remember { mutableStateOf(0) }

                    CounterScreenFancy(
                        capacity = capacity,
                        current = currentCapacity,
                        onIncrement = { currentCapacity++ },
                        onDecrement = { currentCapacity-- }
                    );

                }
            }
        }
    }
}

@Composable
fun CounterScreen(
    capacity: Int,
    current: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    Column {
        Text(text = "Capacity: $current/$capacity")
        Button(
            onClick = onIncrement,
            enabled = current < capacity
        ) {
            Text(text = "Increment")
        }
        Button(
            onClick = onDecrement,
            enabled = current != 0

        ) {
            Text(text = "Decrement")
        }
    }
}

@Preview
@Composable
fun CounterScreenFancyPreview() {
    CounterScreenFancy(
        capacity = 20,
        current = 12,
        onIncrement = { },
        onDecrement = { }
    );
}

//Design taken from https://henryegloff.com/simple-counter/
@Composable
fun CounterScreenFancy(
    capacity: Int,
    current: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {

    Box(Modifier.fillMaxSize()) {
        Text(
            text = current.toString(),
            modifier = Modifier.align(Alignment.Center),
            fontSize = 150.sp
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 120.dp),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Text(
                text = (capacity - current).toString(),
                fontSize = 30.sp
            )

            if (current != capacity)
                Text(text = "Remaining")
            else
                Text(
                    text = "Capacity Reached",
                    color = androidx.compose.ui.graphics.Color.Red
                )
        }

        Box(
            Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
        ) {
            val buttonModifier = Modifier.size(75.dp)
            val buttonContentModifier = Modifier.size(40.dp)

            Row(Modifier.align(Alignment.Center)) {

                RoundButton(
                    onClick = onIncrement,
                    modifier = buttonModifier,
                    contentModifier = buttonContentModifier,
                    enabled = current < capacity
                )
                Spacer(modifier = Modifier.size(30.dp))

                RoundButton(
                    onClick = onDecrement,
                    modifier = buttonModifier.rotate(180F),
                    contentModifier = buttonContentModifier,
                    enabled = current != 0,
                )
            }

        }
    }
}

@Composable
fun RoundButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    contentModifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.size(50.dp),
        contentPadding = PaddingValues(0.dp),
        enabled = enabled

    ) {
        Icon(
            imageVector = Icons.Default.KeyboardArrowUp,
            contentDescription = "Favorite",
            modifier = contentModifier.size(20.dp)
        )
    }
}