package isel.pdm.demos.tictactoe.ui.preferences

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class UserPreferencesActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UserPreferencesScreen(
                userInfo = null,
                onSaveRequested = { TODO() },
                onNavigateBackRequested = { TODO() }
            )
        }
    }
}