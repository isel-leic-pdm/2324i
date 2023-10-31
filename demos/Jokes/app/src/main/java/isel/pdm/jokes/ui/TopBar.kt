package isel.pdm.jokes.ui

import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import isel.pdm.jokes.R
import isel.pdm.jokes.ui.theme.JokesAppIcons
import isel.pdm.jokes.ui.theme.JokesTheme

/**
 * Used to aggregate [TopBar] navigation handlers. If a handler is null, the corresponding
 * navigation element is not displayed.
 *
 * @property onBackRequested the callback invoked when the user clicks the back button.
 * @property onInfoRequested the callback invoked when the user clicks the info button.
 * @property onSearchRequested the callback invoked when the user clicks the search button.
 */
data class NavigationHandlers(
    val onBackRequested: (() -> Unit)? = null,
    val onInfoRequested: (() -> Unit)? = null,
    val onSearchRequested: (() -> Unit)? = null
)

// Test tags for the TopBar navigation elements
const val NavigateBackTestTag = "NavigateBack"
const val NavigateToInfoTestTag = "NavigateToInfo"
const val NavigateToSearchTestTag = "NavigateToSearch"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navigation: NavigationHandlers = NavigationHandlers()) {
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.app_name)) },
        navigationIcon = {
            if (navigation.onBackRequested != null) {
                IconButton(
                    onClick = navigation.onBackRequested,
                    modifier = Modifier.testTag(NavigateBackTestTag)
                ) {
                    Icon(
                        imageVector = JokesAppIcons.ArrowBack,
                        contentDescription = stringResource(id = R.string.top_bar_go_back)
                    )
                }
            }
        },
        actions = {
            if (navigation.onSearchRequested != null) {
                IconButton(
                    onClick = navigation.onSearchRequested,
                    modifier = Modifier.testTag(NavigateToSearchTestTag)
                ) {
                    Icon(
                        imageVector = JokesAppIcons.Search,
                        contentDescription = stringResource(id = R.string.top_bar_navigate_to_search)
                    )
                }
            }

            if (navigation.onInfoRequested != null) {
                IconButton(
                    onClick = navigation.onInfoRequested,
                    modifier = Modifier.testTag(NavigateToInfoTestTag)
                ) {
                    Icon(
                        imageVector = JokesAppIcons.Info,
                        contentDescription = stringResource(id = R.string.top_bar_navigate_to_about)
                    )
                }
            }
        }
    )}

@Preview
@Composable
private fun TopBarPreviewInfoAndHistory() {
    JokesTheme {
        TopBar(
            navigation = NavigationHandlers(onInfoRequested = { })
        )
    }
}

@Preview
@Composable
private fun TopBarPreviewAll() {
    JokesTheme {
        TopBar(
            navigation = NavigationHandlers(
                onBackRequested = { },
                onInfoRequested = { },
                onSearchRequested = { }
            )
        )
    }
}

@Preview
@Composable
private fun TopBarPreviewInfoAndBack() {
    JokesTheme {
        TopBar(navigation = NavigationHandlers(onBackRequested = { }, onInfoRequested = { }))
    }
}


@Preview
@Composable
private fun TopBarPreviewBack() {
    JokesTheme {
        TopBar(navigation = NavigationHandlers(onBackRequested = { }))
    }
}