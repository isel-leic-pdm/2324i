package isel.pdm.demos.tictactoe.utils

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import isel.pdm.demos.tictactoe.ui.TicTacToeTestApplication
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

/**
 * Test rule that ensures that the default dependencies are preserved after the test is executed.
 *
 * Tests that make use of this rule are allowed to replace the globally accessible dependencies by
 * other doubles that serve their purposes. The behaviour of the remaining tests
 * is preserved by saving the default dependencies and restoring them after the execution of the test.
 */
class PreserveDefaultDependencies<A : ComponentActivity>(
    val composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<A>, A>
) : TestRule {

    val testApplication: TicTacToeTestApplication = InstrumentationRegistry
        .getInstrumentation()
        .targetContext
        .applicationContext as TicTacToeTestApplication

    val scenario:ActivityScenario<A>
        get () = composeTestRule.activityRule.scenario

    override fun apply(test: Statement, description: Description): Statement =
        object : Statement() {
            override fun evaluate() {
                val defaultUserInfoRepo = testApplication.userInfoRepository
                try {
                    val testStatement = composeTestRule.apply(test, description)
                    testStatement.evaluate()
                }
                finally {
                    testApplication.userInfoRepository = defaultUserInfoRepo
                }
            }
        }
}

/**
 * Creates a compose rule that saves and restores the default dependencies and
 * starts an activity of type <A>.
 */
inline fun <reified A : ComponentActivity> createPreserveDependenciesComposeRule() =
    PreserveDefaultDependencies(createAndroidComposeRule<A>())

/**
 * Creates a compose rule that saves and restores the default dependencies and
 * starts an activity of type <A> with an intent containing the received intent.
 */
inline fun <reified A : ComponentActivity> createPreserveDependenciesComposeRule(intent: Intent) =
    PreserveDefaultDependencies(
        AndroidComposeTestRule(
            activityRule = ActivityScenarioRule(intent),
            activityProvider = { rule ->
                lateinit var activity: A
                rule.scenario.onActivity { activity = it as A }
                activity
            }
        )
    )