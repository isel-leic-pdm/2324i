package pt.isel.pdm.nasaimageoftheday

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import pt.isel.pdm.nasaimageoftheday.ui.theme.NasaImageOfTheDayTheme

const val TAG: String = "NasaImageOfTheDay"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        setContent {
            NasaImageOfTheDayTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }

}



@Composable
fun MainScreen() {

    Log.d(TAG, "MainScreen composition")
    var showText = remember { mutableStateOf(false) }
    Box()
    {
        if (showText.value == false) {
            Button(
                onClick = {
                    Log.d(TAG, "Button Clicked")
                    showText.value = true
                    Log.d(TAG, "State is " + showText.value)
                },
                modifier = Modifier.align(Alignment.Center)
            )
            {
                Text(text = "Load Image")
            }
        } else {
            Text(text = "Here")
        }

    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NasaImageOfTheDayTheme {
        Greeting("Android")
    }
}