package pt.isel.pdm.tictactoe.ui.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import pt.isel.pdm.tictactoe.model.UserInfo
import pt.isel.pdm.tictactoe.repository.UserRepository
import pt.isel.pdm.tictactoe.ui.BaseViewModel

class HomeViewModel(
    private val savedState: SavedStateHandle,
    private val userRepository: UserRepository
) : BaseViewModel() {

    private val _userInfoFlow = MutableStateFlow<UserInfo?>(null)

    val userInfo: Flow<UserInfo?>
        get() = _userInfoFlow

    fun fetchUserData() = errorAwareScope {
        _userInfoFlow.value = userRepository.getUserData()
    }

    fun setUserName(name: String) = loadingAndErrorAwareScope {
        val newUser = UserInfo(name)
        userRepository.setUserData(newUser)
        _userInfoFlow.value = newUser
    }
}