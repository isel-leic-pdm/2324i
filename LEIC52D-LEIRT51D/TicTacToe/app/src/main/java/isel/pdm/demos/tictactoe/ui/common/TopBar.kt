package isel.pdm.demos.tictactoe.ui.common

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import isel.pdm.demos.tictactoe.R
import isel.pdm.demos.tictactoe.ui.common.theme.TicTacToeTheme

/**
 * Used to aggregate [TopBar] navigation handlers.
 */
data class NavigationHandlers(
    val onBackRequested: (() -> Unit)? = null,
    val onPreferencesRequested: (() -> Unit)? = null,
)

// Test tags for the TopBar navigation elements
const val NavigateBackTag = "NavigateBack"
const val NavigateToPreferencesTag = "NavigateToPreferences"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navigation: NavigationHandlers = NavigationHandlers()) {
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.app_name)) },
        navigationIcon = {
            if (navigation.onBackRequested != null) {
                IconButton(
                    onClick = navigation.onBackRequested,
                    modifier = Modifier.testTag(NavigateBackTag)
                ) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = stringResource(id = R.string.top_bar_go_back)
                    )
                }
            }
        },
        actions = {
            if (navigation.onPreferencesRequested != null) {
                IconButton(
                    onClick = navigation.onPreferencesRequested,
                    modifier = Modifier.testTag(NavigateToPreferencesTag)
                ) {
                    Icon(
                        Icons.Default.Settings,
                        contentDescription = stringResource(id = R.string.top_bar_navigate_to_prefs)
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
        )
    )}

@Preview(showBackground = true)
@Composable
private fun TopBarPreviewBack() {
    TicTacToeTheme {
        TopBar(
            NavigationHandlers(onBackRequested = { })
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TopBarPreviewBackAndPrefs() {
    TicTacToeTheme {
        TopBar(
            NavigationHandlers(onBackRequested = { }, onPreferencesRequested = { })
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TopBarPreviewNoNavigation() {
    TicTacToeTheme {
        TopBar()
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun TopBarPreviewBackAndPrefsDark() {
    TicTacToeTheme {
        TopBar(
            NavigationHandlers(onBackRequested = { }, onPreferencesRequested = { })
        )
    }
}

