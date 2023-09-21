package com.example.helloworld

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.helloworld.ui.theme.HelloWorldTheme

const val TAG = "AndroidIntro"

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "onCreate starts on instance = " +
                "${hashCode()} and thread = ${Thread.currentThread().name}")
        setContent {
            HelloWorldTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
        Log.v(TAG, "onCreate ends on instance = " +
                "${hashCode()} and thread = ${Thread.currentThread().name}")
    }

    override fun onStart() {
        super.onStart()
        Log.v(TAG, "onStart on instance = " +
                "${hashCode()} and thread = ${Thread.currentThread().name}")

    }

    override fun onStop() {
        super.onStop()
        Log.v(TAG, "onStop on instance = " +
                "${hashCode()} and thread = ${Thread.currentThread().name}")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(TAG, "onDestroy on instance = " +
                "${hashCode()} and thread = ${Thread.currentThread().name}")

    }
}

@Composable
fun Greeting(name: String) {
    Text(
        text = "PDM in the night...",
        fontSize = MaterialTheme.typography.bodyLarge.fontSize
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    HelloWorldTheme {
        Greeting("Android")
    }
}