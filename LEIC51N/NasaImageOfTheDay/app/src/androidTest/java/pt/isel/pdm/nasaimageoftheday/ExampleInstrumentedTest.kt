package pt.isel.pdm.nasaimageoftheday

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import okhttp3.internal.wait

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule
import pt.isel.pdm.nasaimageoftheday.helpers.TestTags
import pt.isel.pdm.nasaimageoftheday.model.NasaImage
import pt.isel.pdm.nasaimageoftheday.screens.components.NasaImageView
import pt.isel.pdm.nasaimageoftheday.screens.main.MainActivity

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@OptIn(ExperimentalTestApi::class)
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

    @get:Rule
    val activityRule = createAndroidComposeRule<MainActivity>()
    @Test
    fun ensure_clicking_on_button_loads_image() {
        val resources = InstrumentationRegistry.getInstrumentation().targetContext.resources

        activityRule.onNodeWithTag(TestTags.SingleNasaViewTestTag).assertDoesNotExist()
        activityRule.onNodeWithText(resources.getString(R.string.load_image)).assertExists()
        val button = activityRule.onNodeWithTag(TestTags.LoadImageButton)

        button.assertIsEnabled()
        button.performClick()

        activityRule.waitUntilExactlyOneExists(
            hasTestTag(TestTags.SingleNasaViewTestTag),
            3000
        )
        activityRule.onNodeWithTag(TestTags.SingleNasaViewTestTag).assertExists()
        activityRule.onNodeWithTag(TestTags.LoadImageButton).assertExists()
    }

    @Test
    fun ensure_activity_rotation_maintains_state() {
        activityRule
            .onNodeWithTag(TestTags.LoadImageButton)
            .assertIsEnabled()
            .performClick()

        activityRule.waitUntilExactlyOneExists(
            hasTestTag(TestTags.SingleNasaViewTestTag),
            3000
        )

        activityRule.onNodeWithTag(TestTags.SingleNasaViewTestTag).assertExists()
        activityRule.onNodeWithTag(TestTags.LoadImageButton).assertExists()

        activityRule.activityRule.scenario.recreate()

        activityRule.onNodeWithTag(TestTags.SingleNasaViewTestTag).assertExists()
        activityRule.onNodeWithTag(TestTags.LoadImageButton).assertExists()
    }
}