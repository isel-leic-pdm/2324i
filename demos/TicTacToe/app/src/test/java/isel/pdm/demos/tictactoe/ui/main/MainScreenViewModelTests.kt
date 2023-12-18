package isel.pdm.demos.tictactoe.ui.main

import io.mockk.coEvery
import io.mockk.mockk
import isel.pdm.demos.tictactoe.domain.IOState
import isel.pdm.demos.tictactoe.domain.user.UserInfo
import isel.pdm.demos.tictactoe.domain.user.UserInfoRepository
import isel.pdm.demos.tictactoe.utils.MockMainDispatcherRule
import isel.pdm.demos.tictactoe.utils.SuspendingGate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainScreenViewModelTests {

    @get:Rule
    val rule = MockMainDispatcherRule(testDispatcher = StandardTestDispatcher())

    private val testUserInfo = UserInfo("test", "test")
    private val userInfoRepo = mockk<UserInfoRepository> {
        coEvery { getUserInfo() } coAnswers { testUserInfo }
    }

    @Test
    fun initially_the_user_info_flow_is_idle() = runTest {
        // Arrange
        val sut = MainScreenViewModel(userInfoRepo)
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
        assertTrue("Expected Idle bot got $collectedState instead", collectedState is IOState.Idle)
    }

    @Test
    fun fetchUserInfo_emits_to_the_user_info_flow_the_loaded_state() = runTest {
        // Arrange
        val sut = MainScreenViewModel(userInfoRepo)
        // Act
        val gate = SuspendingGate()
        var lastCollectedState: IOState<UserInfo?>? = null
        val collectJob = launch {
            sut.userInfo.collectLatest {
                if (it is IOState.Loaded) {
                    lastCollectedState = it
                    gate.open()
                }
            }
        }
        sut.fetchUserInfo()

        // Lets wait for the flow to emit the latest value
        withTimeout(1000) {
            gate.await()
            collectJob.cancelAndJoin()
        }

        // Assert
        val loaded = lastCollectedState as? IOState.Loaded
        assertNotNull("Expected Loaded but got $lastCollectedState instead", loaded)
        assertEquals(testUserInfo, loaded?.value?.getOrNull())
    }

    @Test
    fun fetchUserInfo_emits_to_the_user_info_flow_the_loading_state() = runTest {
        // Arrange
        val sut = MainScreenViewModel(userInfoRepo)
        // Act
        val gate = SuspendingGate()
        var lastCollectedState: IOState<UserInfo?>? = null
        val collectJob = launch {
            sut.userInfo.collect {
                if (it is IOState.Loading) {
                    lastCollectedState = it
                    gate.open()
                }
            }
        }
        sut.fetchUserInfo()

        // Lets wait for the flow to emit the latest value
        withTimeout(1000) {
            gate.await()
            collectJob.cancelAndJoin()
        }

        // Assert
        val loading = lastCollectedState as? IOState.Loading
        assertNotNull("Expected Loading but got $lastCollectedState instead", loading)
    }

    @Test(expected = IllegalStateException::class)
    fun fetchUserInfo_throws_if_state_is_not_idle() {
        // Arrange
        val sut = MainScreenViewModel(userInfoRepo)
        sut.fetchUserInfo()

        // Act
        sut.fetchUserInfo()
    }
}