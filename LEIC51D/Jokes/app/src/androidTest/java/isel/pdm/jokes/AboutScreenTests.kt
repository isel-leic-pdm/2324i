package isel.pdm.jokes

import android.net.Uri
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import isel.pdm.jokes.about.AboutScreen
import isel.pdm.jokes.about.AuthorInfoTestTag
import isel.pdm.jokes.about.SocialInfo
import isel.pdm.jokes.about.SocialsElementTestTag
import isel.pdm.jokes.ui.NavigateBackTestTag
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class AboutScreenTests {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun click_on_back_navigation_calls_callback() {
        // Arrange
        var backRequested = false
        composeTestRule.setContent {
            AboutScreen(
                onBackRequested = { backRequested = true },
                socials = emptyList()
            )
        }
        // Act
        composeTestRule.onNodeWithTag(NavigateBackTestTag).performClick()
        // Assert
        assertTrue(backRequested)
    }

    @Test
    fun click_on_author_info_calls_onSendEmailRequested() {
        // Arrange
        var sendEmailRequested = false
        composeTestRule.setContent {
            AboutScreen(
                onSendEmailRequested = { sendEmailRequested = true },
                socials = emptyList()
            )
        }
        // Act
        composeTestRule.onNodeWithTag(AuthorInfoTestTag).performClick()
        // Assert
        assertTrue(sendEmailRequested)
    }

    @Test
    fun click_on_socials_element_calls_onOpenUrlRequested_with_correct_url() {
        // Arrange
        var actualUrl: Uri? = null
        val expectedUrl = Uri.parse("https://www.test.pt")
        composeTestRule.setContent {
            AboutScreen(
                onOpenUrlRequested = { actualUrl = it },
                socials = listOf(SocialInfo(expectedUrl, R.drawable.ic_home))
            )
        }
        // Act
        composeTestRule.onNodeWithTag(SocialsElementTestTag).performClick()
        // Assert
        assertEquals(expectedUrl, actualUrl)
    }
}
