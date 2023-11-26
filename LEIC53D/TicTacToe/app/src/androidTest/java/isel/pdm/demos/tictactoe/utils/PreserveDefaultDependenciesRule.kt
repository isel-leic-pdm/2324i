package isel.pdm.demos.tictactoe.utils

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.rules.ActivityScenarioRule
import isel.pdm.demos.tictactoe.TicTacToeTestApplication
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

/**
 * Test rule that ensures that the default dependencies are preserved after the test is executed.
 *
 * Tests that make use of this rule are allowed to replace the globally accessible dependencies by
 * other doubles that serve their purposes. The behaviour of the remaining tests
 * is preserved by saving the default dependencies and restoring them after the execution of the test.
 * The rule also ensures that an Activity of type <A> is started before the test is executed.
 */
class PreserveDefaultDependencies<A : ComponentActivity>(
    val composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<A>, A>
) : TestRule {

    val testApplication: TicTacToeTestApplication =
        ApplicationProvider.getApplicationContext() as TicTacToeTestApplication

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
inline fun <reified A : ComponentActivity> createActivityAndPreserveDependenciesComposeRule() =
    PreserveDefaultDependencies(createAndroidComposeRule<A>())

/**
 * Creates a compose rule that saves and restores the default dependencies and
 * starts an activity of type <A> with an intent containing the received intent.
 */
inline fun <reified A : ComponentActivity> createActivityAndPreserveDependenciesComposeRule(intent: Intent) =
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

/**
 * Test rule that ensures that the default dependencies are preserved after the test is executed.
 *
 * Tests that make use of this rule are allowed to replace the globally accessible dependencies by
 * other doubles that serve their purposes. The behaviour of the remaining tests
 * is preserved by saving the default dependencies and restoring them after the execution of the test.
 *
 * This rule does not start an activity before the test is executed. The test is responsible for
 * starting the activity.
 */
class PreserveDefaultDependenciesNoActivity() : TestRule {

    val testApplication: TicTacToeTestApplication =
        ApplicationProvider.getApplicationContext() as TicTacToeTestApplication

    override fun apply(test: Statement, description: Description): Statement =
        object : Statement() {
            override fun evaluate() {
                val defaultUserInfoRepo = testApplication.userInfoRepository
                try {
                    test.evaluate()
                }
                finally {
                    testApplication.userInfoRepository = defaultUserInfoRepo
                }
            }
        }
}


/**
 * Creates a compose rule that saves and restores the default dependencies. If the test needs
 * to start an activity, it is responsible for starting it.
 */
fun createPreserveDefaultDependenciesComposeRuleNoActivity() =
    AndroidComposeTestRule<TestRule, ComponentActivity>(
        activityRule = PreserveDefaultDependenciesNoActivity(),
        activityProvider = {
            error("This rule does not provide an Activity. Launch and use the Activity yourself.")
        }
    )
