package isel.pdm.demos.tictactoe.ui.preferences

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.SaveAlt
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import isel.pdm.demos.tictactoe.R
import isel.pdm.demos.tictactoe.domain.UserInfo
import isel.pdm.demos.tictactoe.domain.toUserInfoOrNull
import isel.pdm.demos.tictactoe.ui.common.NavigationHandlers
import isel.pdm.demos.tictactoe.ui.common.TopBar
import isel.pdm.demos.tictactoe.ui.common.theme.TicTacToeTheme
import kotlin.math.min

/**
 * The tag used to identify the screen.
 */
const val UserPreferencesScreenTag = "UserPreferencesScreen"

/**
 * The tags used to identify the screen's modes.
 */
const val ViewModeTestTag = "ViewModeTestTag"
const val EditModeTestTag = "EditModeTestTag"

/**
 * The tags used to identify the screen's elements that have associated functionalities
 * and are therefore testable.
 */
const val EditButtonTag = "EditButtonTag"
const val SaveButtonTag = "SaveButtonTag"
const val NickInputFieldTag = "NickInputTag"
const val MottoInputFieldTag = "MottoInputTag"

/**
 * The screen that allows the user to specify its username and moto, to be
 * used in the lobby and during the game.
 * @param userInfo The user info to be displayed in the screen, if any.
 * @param onSaveRequested The callback to be invoked when the user requests to save the user info.
 * @param onNavigateBackRequested The callback to be invoked when the user requests to navigate back.
 */
@Composable
fun UserPreferencesScreen(
    userInfo: UserInfo? = null,
    onSaveRequested: (UserInfo) -> Unit,
    onNavigateBackRequested: () -> Unit
) {
    TicTacToeTheme {
        var isInEditMode by remember { mutableStateOf(userInfo == null) }
        if (userInfo == null || isInEditMode) {
            UserPreferencesScreenEditMode(
                initialUserInfo = userInfo,
                onSaveRequested = onSaveRequested,
                onNavigateBackRequested = onNavigateBackRequested
            )
        }
        else {
            UserPreferencesScreenViewMode(
                userInfo = userInfo,
                onEditRequested = { isInEditMode = true },
                onNavigateBackRequested = onNavigateBackRequested
            )
        }
    }
}

@Composable
private fun UserPreferencesScreenViewMode(
    userInfo: UserInfo,
    onEditRequested: () -> Unit,
    onNavigateBackRequested: () -> Unit
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag(UserPreferencesScreenTag),
        floatingActionButton = { SwitchToEditModeFab(onEditRequested) },
        topBar = { TopBar(NavigationHandlers(onNavigateBackRequested)) },
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .testTag(ViewModeTestTag),
        ) {
            Text(
                text = stringResource(id = R.string.preferences_screen_title),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(50.dp))

            OutlinedTextField(
                value = userInfo.nick,
                onValueChange = { },
                singleLine = true,
                label = { Text(stringResource(id = R.string.preferences_screen_nickname_tip)) },
                leadingIcon = { Icon(imageVector = Icons.Default.Face, contentDescription = "") },
                enabled = false,
                modifier = Modifier
                    .padding(horizontal = 48.dp)
                    .fillMaxWidth()
                    .testTag(NickInputFieldTag)
            )

            OutlinedTextField(
                value = userInfo.motto ?: "",
                onValueChange = { },
                maxLines = 3,
                label = { Text(stringResource(id = R.string.preferences_screen_moto_tip)) },
                leadingIcon = { Icon(imageVector = Icons.Default.Comment, contentDescription = "") },
                enabled = false,
                modifier = Modifier
                    .padding(horizontal = 48.dp)
                    .fillMaxWidth()
                    .testTag(MottoInputFieldTag)
            )
            Spacer(modifier = Modifier.sizeIn(minHeight = 128.dp, maxHeight = 256.dp))
        }
    }
}

@Composable
private fun UserPreferencesScreenEditMode(
    initialUserInfo: UserInfo?,
    onSaveRequested: (UserInfo) -> Unit,
    onNavigateBackRequested: () -> Unit
) {
    var nick by remember { mutableStateOf(initialUserInfo?.nick ?: "") }
    var motto by remember { mutableStateOf(initialUserInfo?.motto ?: "") }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag(UserPreferencesScreenTag),
        floatingActionButton = {
            SaveFab(
                enabled = nick.isNotBlank(),
                onClick = { toUserInfoOrNull(nick.trim(), motto.trim())?.let { onSaveRequested(it) }}
            )
        },
        topBar = { TopBar(NavigationHandlers(onNavigateBackRequested)) },
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .testTag(EditModeTestTag),
        ) {
            Text(
                text = stringResource(id = R.string.preferences_screen_title),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(50.dp))

            OutlinedTextField(
                value = nick,
                onValueChange = { nick = ensureInputBounds(it) },
                singleLine = true,
                label = { Text(stringResource(id = R.string.preferences_screen_nickname_tip)) },
                leadingIcon = { Icon(imageVector = Icons.Default.Face, contentDescription = "") },
                enabled = true,
                modifier = Modifier
                    .padding(horizontal = 48.dp)
                    .fillMaxWidth()
                    .testTag(NickInputFieldTag)
            )
            OutlinedTextField(
                value = motto,
                onValueChange = { motto = ensureInputBounds(it) },
                maxLines = 3,
                label = { Text(stringResource(id = R.string.preferences_screen_moto_tip)) },
                leadingIcon = { Icon(Icons.Default.Comment, contentDescription = "") },
                enabled = true,
                modifier = Modifier
                    .padding(horizontal = 48.dp)
                    .fillMaxWidth()
                    .testTag(MottoInputFieldTag)
            )
            Spacer(modifier = Modifier.sizeIn(minHeight = 128.dp, maxHeight = 256.dp))
        }
    }
}

@Composable
fun SwitchToEditModeFab(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = CircleShape,
        modifier = Modifier
            .defaultMinSize(minWidth = 72.dp, minHeight = 72.dp)
            .testTag(EditButtonTag)
    ){
        Icon(Icons.Default.Edit, contentDescription = "")
    }
}

@Composable
private fun SaveFab(enabled: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = CircleShape,
        modifier = Modifier
            .defaultMinSize(minWidth = 72.dp, minHeight = 72.dp)
            .testTag(SaveButtonTag)
    ){
        Icon(Icons.Default.SaveAlt, contentDescription = "")
    }
}

private const val MAX_INPUT_SIZE = 50
private fun ensureInputBounds(input: String) =
    input.also {
        it.substring(range = 0 until min(it.length, MAX_INPUT_SIZE))
    }


@Preview(showBackground = true)
@Composable
private fun UserPreferencesScreenViewModePreview() {
    TicTacToeTheme {
        UserPreferencesScreen(
            userInfo = UserInfo("Tina Turner", "Simply the best!"),
            onSaveRequested = {},
            onNavigateBackRequested = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun UserPreferencesScreenEditModePreview() {
    TicTacToeTheme {
        UserPreferencesScreen(
            userInfo = null,
            onSaveRequested = {},
            onNavigateBackRequested = {}
        )
    }
}
