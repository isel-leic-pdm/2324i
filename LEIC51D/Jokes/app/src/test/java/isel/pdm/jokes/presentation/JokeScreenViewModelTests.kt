package isel.pdm.jokes.presentation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Rule

@OptIn(ExperimentalCoroutinesApi::class)
class JokeScreenViewModelTests {

    @get:Rule
    val rule = MockMainDispatcherRule(UnconfinedTestDispatcher())

}