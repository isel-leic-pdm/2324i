package isel.pdm.demos.tictactoe.ui.preferences

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import isel.pdm.demos.tictactoe.domain.UserInfo
import isel.pdm.demos.tictactoe.ui.common.NavigateBackTag
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class UserPreferencesScreenTests {

    @get:Rule
    val composeTree = createComposeRule()
    private val userInfo = UserInfo(nick = "test user", motto = "test moto")

    @Test
    fun the_screen_is_in_view_mode_if_user_info_is_received() {
        // Arrange
        composeTree.setContent {
            UserPreferencesScreen(
                userInfo = userInfo,
                onSaveRequested = { _ -> },
                onNavigateBackRequested = { }
            )
        }
        // Act
        // Assert
        composeTree.onNodeWithTag(ViewModeTestTag).assertExists()
        composeTree.onNodeWithTag(EditModeTestTag).assertDoesNotExist()
    }

    @Test
    fun in_view_mode_input_fields_are_populated_with_the_user_info() {
        // Arrange
        val motto = requireNotNull(userInfo.motto)
        composeTree.setContent {
            UserPreferencesScreen(
                userInfo = userInfo,
                onSaveRequested = { _ -> },
                onNavigateBackRequested = { }
            )
        }
        // Act
        // Assert
        composeTree.onNodeWithText(userInfo.nick).assertExists()
        composeTree.onNodeWithText(motto).assertExists()
    }

    @Test
    fun in_view_mode_the_edit_button_is_available() {
        // Arrange
        composeTree.setContent {
            UserPreferencesScreen(
                userInfo = userInfo,
                onSaveRequested = { _ -> },
                onNavigateBackRequested = { }
            )
        }
        // Act
        // Assert
        composeTree.onNodeWithTag(EditButtonTag).assertExists()
    }

    @Test
    fun in_view_mode_the_save_button_is_not_available() {
        // Arrange
        composeTree.setContent {
            UserPreferencesScreen(
                userInfo = userInfo,
                onSaveRequested = { _ -> },
                onNavigateBackRequested = { }
            )
        }
        // Act
        // Assert
        composeTree.onNodeWithText(SaveButtonTag).assertDoesNotExist()
    }

    @Test
    fun in_view_mode_the_input_fields_are_disabled() {
        // Arrange
        composeTree.setContent {
            UserPreferencesScreen(
                userInfo = userInfo,
                onSaveRequested = { _ -> },
                onNavigateBackRequested = { }
            )
        }
        // Act
        // Assert
        composeTree.onNodeWithTag(NickInputFieldTag).assertIsNotEnabled()
        composeTree.onNodeWithTag(MottoInputFieldTag).assertIsNotEnabled()
    }

    @Test
    fun pressing_the_edit_button_puts_the_screen_in_edit_mode() {
        // Arrange
        composeTree.setContent {
            UserPreferencesScreen(
                userInfo = userInfo,
                onSaveRequested = { _ -> },
                onNavigateBackRequested = { }
            )
        }
        // Act
        composeTree.onNodeWithTag(EditButtonTag).performClick()
        composeTree.waitForIdle()
        // Assert
        composeTree.onNodeWithTag(ViewModeTestTag).assertDoesNotExist()
        composeTree.onNodeWithTag(EditModeTestTag).assertExists()
    }

    @Test
    fun the_screen_is_in_edit_mode_if_user_info_is_not_received() {
        // Arrange
        composeTree.setContent {
            UserPreferencesScreen(
                userInfo = null,
                onSaveRequested = { _ -> },
                onNavigateBackRequested = { }
            )
        }
        // Act
        // Assert
        composeTree.onNodeWithTag(ViewModeTestTag).assertDoesNotExist()
        composeTree.onNodeWithTag(EditModeTestTag).assertExists()
    }

    @Test
    fun in_edit_mode_the_edit_button_is_not_available() {
        // Arrange
        composeTree.setContent {
            UserPreferencesScreen(
                userInfo = null,
                onSaveRequested = { _ -> },
                onNavigateBackRequested = { }
            )
        }
        // Act
        // Assert
        composeTree.onNodeWithTag(EditButtonTag).assertDoesNotExist()
    }

    @Test
    fun in_edit_mode_the_save_button_is_available() {
        // Arrange
        composeTree.setContent {
            UserPreferencesScreen(
                userInfo = null,
                onSaveRequested = { _ -> },
                onNavigateBackRequested = { }
            )
        }
        // Act
        // Assert
        composeTree.onNodeWithTag(SaveButtonTag).assertExists()
    }

    @Test
    fun in_edit_mode_the_input_fields_are_editable() {
        // Arrange
        composeTree.setContent {
            UserPreferencesScreen(
                userInfo = null,
                onSaveRequested = { _ -> },
                onNavigateBackRequested = { }
            )
        }
        // Act
        // Assert
        composeTree.onNodeWithTag(NickInputFieldTag).assertIsEnabled()
    }

    @Test
    fun pressing_the_save_button_with_valid_input_calls_the_onSaveRequested_callback() {
        // Arrange
        var userInfoToSave: UserInfo? = null
        composeTree.setContent {
            UserPreferencesScreen(
                userInfo = null,
                onSaveRequested = { userInfoToSave = it },
                onNavigateBackRequested = { }
            )
        }
        val expectedNick = "nick"
        val expectedMoto = "moto"
        composeTree.onNodeWithTag(NickInputFieldTag).performTextInput(expectedNick)
        composeTree.onNodeWithTag(MottoInputFieldTag).performTextInput(expectedMoto)
        // Act
        composeTree.onNodeWithTag(SaveButtonTag).performClick()
        // Assert
        assertNotNull(userInfoToSave)
        val expectedUserInfo = UserInfo(expectedNick, expectedMoto)
        assertEquals(expectedUserInfo, userInfoToSave)
    }

    @Test
    fun pressing_the_save_button_with_valid_input_calls_callback_with_trimmed_values() {
        // Arrange
        var userInfoToSave: UserInfo? = null
        composeTree.setContent {
            UserPreferencesScreen(
                userInfo = null,
                onSaveRequested = { userInfoToSave = it },
                onNavigateBackRequested = { }
            )
        }
        val enteredNick = " nick "
        val enteredMoto = " moto "
        composeTree.onNodeWithTag(NickInputFieldTag).performTextInput(enteredNick)
        composeTree.onNodeWithTag(MottoInputFieldTag).performTextInput(enteredMoto)
        // Act
        composeTree.onNodeWithTag(SaveButtonTag).performClick()
        // Assert
        assertNotNull("Expected non null userInfo", userInfoToSave)
        val expectedUserInfo = UserInfo(enteredNick.trim(), enteredMoto.trim())
        assertEquals(expectedUserInfo, userInfoToSave)
    }

    @Test
    fun when_nick_input_content_is_invalid_save_button_is_disabled() {
        // Arrange
        composeTree.setContent {
            UserPreferencesScreen(
                userInfo = null,
                onSaveRequested = { _ -> },
                onNavigateBackRequested = { }
            )
        }
        // Act
        composeTree.onNodeWithTag(NickInputFieldTag).performTextInput("  ")
        // Assert
        composeTree.onNodeWithTag(SaveButtonTag).assertIsNotEnabled()
    }

    @Test
    fun pressing_the_save_button_with_invalid_input_does_not_put_the_screen_in_view_mode() {
        // Arrange
        composeTree.setContent {
            UserPreferencesScreen(
                userInfo = null,
                onSaveRequested = { _ -> },
                onNavigateBackRequested = { }
            )
        }
        // Act
        composeTree.onNodeWithTag(SaveButtonTag).performClick()
        // Assert
        composeTree.onNodeWithTag(ViewModeTestTag).assertDoesNotExist()
        composeTree.onNodeWithTag(EditModeTestTag).assertExists()
    }

    @Test
    fun pressing_navigate_back_button_calls_the_onNavigateBackRequested_callback() {
        // Arrange
        var navigateBackRequested = false
        composeTree.setContent {
            UserPreferencesScreen(
                userInfo = null,
                onSaveRequested = { _ -> },
                onNavigateBackRequested = { navigateBackRequested = true }
            )
        }
        // Act
        composeTree.onNodeWithTag(NavigateBackTag).performClick()
        // Assert
        assertTrue(navigateBackRequested)
    }
}