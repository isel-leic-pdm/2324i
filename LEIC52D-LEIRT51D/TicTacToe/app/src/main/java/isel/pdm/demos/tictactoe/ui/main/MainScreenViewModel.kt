package isel.pdm.demos.tictactoe.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import isel.pdm.demos.tictactoe.domain.Idle
import isel.pdm.demos.tictactoe.domain.LoadState
import isel.pdm.demos.tictactoe.domain.UserInfo
import isel.pdm.demos.tictactoe.domain.UserInfoRepository
import isel.pdm.demos.tictactoe.domain.idle
import isel.pdm.demos.tictactoe.domain.loaded
import isel.pdm.demos.tictactoe.domain.loading
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainScreenViewModel(private val repository: UserInfoRepository) : ViewModel() {

    private val _userInfoFlow: MutableStateFlow<LoadState<UserInfo?>> = MutableStateFlow(idle())
    val userInfoFlow: Flow<LoadState<UserInfo?>>
        get() = _userInfoFlow.asStateFlow()

    fun fetchUserInfo() {
        if (_userInfoFlow.value is Idle) {
            _userInfoFlow.value = loading()
            viewModelScope.launch {
                val result = runCatching { repository.getUserInfo() }
                _userInfoFlow.value = loaded(result)
            }
        }
    }

    fun resetToIdle() {
        _userInfoFlow.value = idle()
    }

    companion object {
        fun factory(repository: UserInfoRepository) = viewModelFactory {
            initializer { MainScreenViewModel(repository) }
        }
    }
}