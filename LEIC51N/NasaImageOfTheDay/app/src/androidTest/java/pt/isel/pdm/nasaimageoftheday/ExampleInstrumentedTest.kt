package pt.isel.pdm.nasaimageoftheday

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule
import pt.isel.pdm.nasaimageoftheday.helpers.TestTags
import pt.isel.pdm.nasaimageoftheday.model.NasaImage
import pt.isel.pdm.nasaimageoftheday.model.NasaImages
import pt.isel.pdm.nasaimageoftheday.screens.components.NasaImageView

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun test_if_all_fields_appear_on_nasaimageview() {
        val nasaImage = NasaImage("title", "author", "description", "10-07-1923", "www.google.com")
        composeTestRule.setContent {
            NasaImageView(nasaImage = nasaImage)
        }

        composeTestRule.onNodeWithText(nasaImage.title).assertIsDisplayed()
        composeTestRule.onNodeWithText(nasaImage.date).assertIsDisplayed()
        composeTestRule.onNodeWithText(nasaImage.description).assertDoesNotExist()
        composeTestRule.onNodeWithTag(TestTags.AuthorOnNasaImageView).assertIsDisplayed()
    }

    @Test
    fun test_if_nasaimage_without_author_doesnt_show_it() {
        val nasaImage = NasaImage("", null, "", "", "")

        composeTestRule.setContent {
            NasaImageView(nasaImage = nasaImage)
        }
        composeTestRule.onNodeWithTag(TestTags.AuthorOnNasaImageView).assertDoesNotExist()
    }
}