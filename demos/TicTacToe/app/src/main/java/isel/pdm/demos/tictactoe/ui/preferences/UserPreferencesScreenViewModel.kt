package isel.pdm.demos.tictactoe.ui.preferences

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import isel.pdm.demos.tictactoe.domain.IOState
import isel.pdm.demos.tictactoe.domain.IOState.Saving
import isel.pdm.demos.tictactoe.domain.idle
import isel.pdm.demos.tictactoe.domain.saved
import isel.pdm.demos.tictactoe.domain.saving
import isel.pdm.demos.tictactoe.domain.user.UserInfo
import isel.pdm.demos.tictactoe.domain.user.UserInfoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * View model for the User Preferences Screen.
 * @param repository    The repository for the user information
 */
class UserPreferencesScreenViewModel(
    private val repository: UserInfoRepository
) : ViewModel() {

    companion object {
        fun factory(repository: UserInfoRepository) = viewModelFactory {
            initializer { UserPreferencesScreenViewModel(repository) }
        }
    }

    private val _ioStateFlow: MutableStateFlow<IOState<UserInfo?>> = MutableStateFlow(idle())

    /**
     * The flow of states the view model traverses.
     */
    val ioState: Flow<IOState<UserInfo?>>
        get() = _ioStateFlow.asStateFlow()

    /**
     * Updates the user information.
     * @param userInfo  The user information.
     * @throws IllegalStateException if the view model is still in the saving state because the
     * user information is still being updated.
     */
    fun updateUserInfo(userInfo: UserInfo) {
        if (_ioStateFlow.value is Saving)
            throw IllegalStateException("The view model is not in the idle state.")

        _ioStateFlow.value = saving()
        viewModelScope.launch {
            val result = runCatching {
                repository.updateUserInfo(userInfo)
                userInfo
            }
            _ioStateFlow.value = saved(result)
        }
    }

    /**
     * Resets the view model to the idle state. From the idle state, the user information
     * can be updated.
     * @throws IllegalStateException if the view model is not in the saving state.
     */
    fun resetToIdle() {
        if (_ioStateFlow.value !is Saving)
            throw IllegalStateException("The view model is not in the saving state.")
        _ioStateFlow.value = idle()
    }
}