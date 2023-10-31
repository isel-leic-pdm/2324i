package isel.pdm.jokes

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import isel.pdm.jokes.search.SearchScreen
import isel.pdm.jokes.ui.NavigateBackTestTag
import isel.pdm.jokes.ui.NavigateToInfoTestTag
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class SearchScreenTests {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun click_on_navigation_to_about_screen_calls_callback() {
        // Arrange
        var infoClicked = false
        composeTestRule.setContent { SearchScreen(onNavigateToInfo = { infoClicked = true }) }
        // Act
        composeTestRule.onNodeWithTag(NavigateToInfoTestTag).performClick()
        // Assert
        Assert.assertTrue(infoClicked)
    }

    @Test
    fun click_on_back_button_calls_callback() {
        // Arrange
        var backClicked = false
        composeTestRule.setContent { SearchScreen(onNavigateBack = { backClicked = true }) }
        // Act
        composeTestRule.onNodeWithTag(NavigateBackTestTag).performClick()
        // Assert
        Assert.assertTrue(backClicked)
    }

    // TODO: Add tests
}