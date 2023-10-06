package pt.isel.pdm.nasaimageoftheday.screens.playground

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue


@Composable
fun MutableDataPlaygroundScreen() {
    var model by remember { mutableStateOf(MyModelContainer2(1)) }

    ModelContainerView(
        model = model,
        increment = {
            model.anInt++;
        }
    )
}

@Composable
fun ModelContainerView(
    model: MyModelContainer2,
    increment: () -> Unit
) {
    Column()
    {
        Text("Model val is with ${model.anInt}")
        Button(onClick = increment) {
            Text("Increment")
        }
    }
}


class MyModelContainer(var anInt: Int) {
}

class MyModelContainer2(private var value: Int) {

    var anInt by mutableStateOf<Int>(value)
}