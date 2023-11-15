package isel.pdm.demos.tictactoe.ui.preferences

import androidx.compose.runtime.Composable
import isel.pdm.demos.tictactoe.domain.UserInfo
import isel.pdm.demos.tictactoe.ui.common.theme.TicTacToeTheme

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

@Composable
fun UserPreferencesScreen(
    userInfo: UserInfo? = null,
    onSaveRequested: (UserInfo) -> Unit,
    onNavigateBackRequested: () -> Unit
) {
    TicTacToeTheme {
        TODO()
    }
}