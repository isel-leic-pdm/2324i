package isel.pdm.demos.tictactoe.ui.preferences

import io.mockk.coEvery
import io.mockk.mockk
import isel.pdm.demos.tictactoe.domain.IOState
import isel.pdm.demos.tictactoe.domain.user.UserInfo
import isel.pdm.demos.tictactoe.domain.user.UserInfoRepository
import isel.pdm.demos.tictactoe.ui.main.MainScreenViewModel
import isel.pdm.demos.tictactoe.utils.MockMainDispatcherRule
import isel.pdm.demos.tictactoe.utils.SuspendingGate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeout
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UserPreferencesScreenViewModelTests {

    @get:Rule
    val rule = MockMainDispatcherRule(testDispatcher = StandardTestDispatcher())

    private val testUserInfo = UserInfo("test", "test")
    private val userInfoRepo = mockk<UserInfoRepository> {
        coEvery { updateUserInfo(any()) } returns Unit
    }

    @Test
    fun initially_the_io_state_flow_is_idle() = runTest {
        // Arrange
        val sut = MainScreenViewModel(mockk())
        // Act
        val gate = SuspendingGate()
        var collectedState: IOState<UserInfo?>? = null
        val collectJob = launch {
            sut.userInfo.collect {
                collectedState = it
                gate.open()
            }
        }

        // Lets wait for the flow to emit the first value
        withTimeout(1000) {
            gate.await()
            collectJob.cancelAndJoin()
        }

        // Assert
        Assert.assertTrue("Expected Idle bot got $collectedState instead", collectedState is IOState.Idle)
    }

    @Test
    fun updateUserInfo_emits_to_the_io_state_flow_the_saved_state() = runTest {
        // Arrange
        val sut = UserPreferencesScreenViewModel(userInfoRepo)
        // Act
        val gate = SuspendingGate()
        var lastCollectedState: IOState<UserInfo?>? = null
        val collectJob = launch {
            sut.ioState.collectLatest {
                if (it is IOState.Saved) {
                    lastCollectedState = it
                    gate.open()
                }
            }
        }
        sut.updateUserInfo(testUserInfo)

        // Lets wait for the flow to emit the latest value
        withTimeout(1000) {
            gate.await()
            collectJob.cancelAndJoin()
        }

        // Assert
        val saved = lastCollectedState as? IOState.Saved
        Assert.assertNotNull("Expected Saved but got $lastCollectedState instead", saved)
        Assert.assertEquals(testUserInfo, saved?.value?.getOrNull())
    }

    @Test
    fun updateUserInfo_emits_to_the_io_state_flow_the_saving_state() = runTest {
        // Arrange
        val sut = UserPreferencesScreenViewModel(userInfoRepo)
        // Act
        val gate = SuspendingGate()
        var lastCollectedState: IOState<UserInfo?>? = null
        val collectJob = launch {
            sut.ioState.collect {
                if (it is IOState.Saving) {
                    lastCollectedState = it
                    gate.open()
                }
            }
        }
        sut.updateUserInfo(testUserInfo)

        // Lets wait for the flow to emit the latest value
        withTimeout(1000) {
            gate.await()
            collectJob.cancelAndJoin()
        }

        // Assert
        val saving = lastCollectedState as? IOState.Saving
        Assert.assertNotNull("Expected Saving but got $lastCollectedState instead", saving)
    }

    @Test(expected = IllegalStateException::class)
    fun updateUserInfo_throws_if_state_is_saving() {
        // Arrange
        val delayedUserInfoRepo = mockk<UserInfoRepository> {
            coEvery { updateUserInfo(any()) } coAnswers { delay(1000) }
        }
        val sut = UserPreferencesScreenViewModel(delayedUserInfoRepo)
        sut.updateUserInfo(testUserInfo)

        // Act
        sut.updateUserInfo(testUserInfo)
    }

    @Test
    fun updateUserInfo_succeeds_if_state_is_saved() = runTest {
        // Arrange
        val sut = UserPreferencesScreenViewModel(userInfoRepo)
        val gate = SuspendingGate()
        var lastCollectedState: IOState<UserInfo?>? = null
        val collectJob = launch {
            sut.ioState.collectLatest {
                if (it is IOState.Saved) {
                    lastCollectedState = it
                    gate.open()
                }
            }
        }
        sut.updateUserInfo(testUserInfo)
        withTimeout(1000) {
            gate.await()
            collectJob.cancelAndJoin()
        }

        // Act
        sut.updateUserInfo(testUserInfo)
        withTimeout(1000) {
            gate.await()
            collectJob.cancelAndJoin()
        }
    }
}