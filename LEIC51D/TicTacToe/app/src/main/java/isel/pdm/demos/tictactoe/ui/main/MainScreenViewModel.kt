package isel.pdm.demos.tictactoe.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import isel.pdm.demos.tictactoe.domain.Idle
import isel.pdm.demos.tictactoe.domain.IOState
import isel.pdm.demos.tictactoe.domain.Loaded
import isel.pdm.demos.tictactoe.domain.UserInfo
import isel.pdm.demos.tictactoe.domain.UserInfoRepository
import isel.pdm.demos.tictactoe.domain.user.UserInfo
import isel.pdm.demos.tictactoe.domain.user.UserInfoRepository
import isel.pdm.demos.tictactoe.domain.idle
import isel.pdm.demos.tictactoe.domain.loaded
import isel.pdm.demos.tictactoe.domain.loading
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * The view model for the application's main screen.
 * @param repository    The repository for the user information
 */
class MainScreenViewModel(
    private val repository: UserInfoRepository
) : ViewModel() {

    companion object {
        fun factory(repository: UserInfoRepository) = viewModelFactory {
            initializer { MainScreenViewModel(repository) }
        }
    }

    private val _userInfoFlow: MutableStateFlow<IOState<UserInfo?>> = MutableStateFlow(idle())

    /**
     * The flow of states the view model traverses.
     */
    val userInfo: Flow<IOState<UserInfo?>>
        get() = _userInfoFlow.asStateFlow()


    /**
     * Fetches the user information. The states the view model traverses while fetching the
     * user information published in the [userInfo] flow. These states are:
     * - [Loading] while fetching the user information;
     * - [Loaded] with the user information if the fetch succeeds;
     * @throws IllegalStateException if the view model is not in the idle state.
     */
    fun fetchUserInfo() {
        if (_userInfoFlow.value !is Idle)
            throw IllegalStateException("The view model is not in the idle state.")

        _userInfoFlow.value = loading()
        viewModelScope.launch {
            val result = runCatching { repository.getUserInfo() }
            _userInfoFlow.value = loaded(result)
        }
    }

    /**
     * Resets the view model to the idle state. From the idle state, the user information
     * can be fetched again.
     */
    fun resetToIdle() {
        if(_userInfoFlow.value !is Loaded)
            throw IllegalStateException("The view model is not in the idle state."

        _userInfoFlow.value = idle()
    }
}