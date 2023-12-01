package pt.isel.pdm.tictactoe.ui.home

import android.os.Bundle
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pt.isel.pdm.tictactoe.helpers.viewModelInitWithSavedState
import pt.isel.pdm.tictactoe.model.CellState
import pt.isel.pdm.tictactoe.model.Cells
import pt.isel.pdm.tictactoe.model.set
import pt.isel.pdm.tictactoe.ui.BaseViewModelActivity
import pt.isel.pdm.tictactoe.ui.lobby.LobbyActivity
import pt.isel.pdm.tictactoe.ui.theme.TicTacToeTheme
import kotlin.random.Random

class HomeActivity : BaseViewModelActivity<HomeViewModel>() {

    private var list = mutableStateListOf(*Cells.emptyBoard.toTypedArray())
    override val viewModel: HomeViewModel by viewModels {
        viewModelInitWithSavedState(this) {
            HomeViewModel(it, dependencyContainer.userRepository)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        var started by mutableStateOf(false)
        var needsToSetupUser by mutableStateOf(true)

        lifecycleScope.launch {
            viewModel.userInfo.collect { usr ->
                if (usr == null)
                    return@collect

                needsToSetupUser = false

                if (started)
                    navigate<LobbyActivity>()
            }
        }

        safeSetContent {
            TicTacToeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LaunchedEffect(Unit) {
                        funAnimation()
                    }

                    //LaunchedEffect(started, viewModel.user) {
                    //    if (started && viewModel.user != null)
                    //        navigate<LobbyActivity>()
                    //}

                    HomeScreen(
                        list = list,
                        onStartClicked = {
                            started = true
                            viewModel.fetchUserData()
                        }
                    )

                    if (needsToSetupUser) {
                        AnimatedVisibility(
                            visible = started,
                            enter = slideInHorizontally(),
                        ) {
                            UserNameScreen(userNameSet = {
                                viewModel.setUserName(it)
                            })
                        }

                    }
                }
            }
        }
    }


    private fun funAnimation() {
        lifecycleScope.launch {
            val time = Random.nextInt() % 2 + 1
            var currState = if (Random.nextBoolean()) CellState.O else CellState.X
            repeat(9) {
                delay(time * 1000L)
                currState = if (currState == CellState.X) CellState.O else CellState.X

                val cell = list.filter { it.state == CellState.EMPTY }.random()
                list[cell] = cell.copy(state = currState)
            }
        }

    }
}


