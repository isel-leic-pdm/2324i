package isel.pdm.demos.tictactoe.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

/**
 * A JUnit rule that sets the main dispatcher to a test dispatcher. To be used
 * in unit tests on view models that launch coroutines.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class MockMainDispatcherRule(
    private val testDispatcher: TestDispatcher = StandardTestDispatcher()
) : TestRule {

    override fun apply(base: Statement, description: Description): Statement =
        object : Statement() {
            override fun evaluate() {
                Dispatchers.setMain(testDispatcher)
                try {
                    base.evaluate()
                }
                finally {
                    Dispatchers.resetMain()
                }
            }
        }

    fun advanceUntilIdle() = testDispatcher.scheduler.advanceUntilIdle()
}
