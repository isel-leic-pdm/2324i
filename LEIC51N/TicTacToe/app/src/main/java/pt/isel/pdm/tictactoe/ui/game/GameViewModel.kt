package pt.isel.pdm.tictactoe.ui.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import pt.isel.pdm.tictactoe.database.model.UserStat
import pt.isel.pdm.tictactoe.model.Cell
import pt.isel.pdm.tictactoe.model.CellState
import pt.isel.pdm.tictactoe.model.Cells
import pt.isel.pdm.tictactoe.model.GameInfo
import pt.isel.pdm.tictactoe.model.GameSession
import pt.isel.pdm.tictactoe.repository.UserStatRepository
import pt.isel.pdm.tictactoe.services.GameForfeitedException
import pt.isel.pdm.tictactoe.services.GameService
import pt.isel.pdm.tictactoe.ui.BaseViewModel

class GameViewModel
    (
    private val service: GameService,
    private val userStatRepository: UserStatRepository
) : BaseViewModel() {

    companion object {
        const val DrawWinnerString = "____DRAW____"
    }

    var remoteGame by mutableStateOf<GameSession?>(null)
    var winner by mutableStateOf<String?>(null)
    var matchEndedWithDraw = false
    val board: List<Cell>
        get() = _boardList


    private var _isPlayer1: Boolean = false
    private var _boardList = mutableStateListOf<Cell>().apply {
        this.addAll(Cells.emptyBoard)
    }
    private var _playerState: CellState = CellState.X
    private var _info: GameInfo? = null
    private var _plays = 0


    fun init(info: GameInfo) {
        loadingAndErrorAwareScope {
            _info = info
            remoteGame = service.getGameSession(info)

            _isPlayer1 = info.isPlayer1
            _playerState = if (info.isPlayer1) CellState.X else CellState.O

            fillBoard()

            if (remoteGame!!.isPlayer1Turn xor _isPlayer1)
                waitForOtherPlayerMove()

        }
    }

    fun play(cell: Cell) {
        loadingAndErrorAwareScope {
            val idx = _boardList.indexOf(cell)
            _boardList[idx] = Cell.createCell(idx, _playerState)
            _plays++
            checkWin()

            try {
                remoteGame = service.play(remoteGame!!, idx, _isPlayer1)

            }catch (e : GameForfeitedException)
            {
                winner = getPlayerName(_isPlayer1)
            }

            if (winner != null)
                return@loadingAndErrorAwareScope

            waitForOtherPlayerMove()
        }
    }


    fun deleteGame() {
        loadingAndErrorAwareScope {
            service.deleteGame(remoteGame!!)
        }
    }

    private suspend fun checkWin(playerMove: Boolean = true) {

        if (_plays == board.size) {
            matchEndedWithDraw = true
            winner = DrawWinnerString

        }

        if (Cells.checkPlayerWin(board)) {
            winner = if (playerMove) getPlayerName(_isPlayer1) else getPlayerName(!_isPlayer1)
        }

        if (winner != null) {
            val otherUserName = getPlayerName(!_info!!.isPlayer1)
            var stat: UserStat
            try {
                stat = userStatRepository.getStat(otherUserName)

            } catch (e: Exception) {
                stat = userStatRepository.createStat(otherUserName)
            }

            if (winner == otherUserName)
                stat = stat.copy(losses = stat.losses + 1)
            else if (winner == DrawWinnerString)
                stat = stat.copy(draws = stat.draws + 1)
            else
                stat = stat.copy(draws = stat.wins + 1)

            userStatRepository.updateStat(stat)

        }

    }

    private fun getPlayerName(player1: Boolean): String {
        if (player1)
            return remoteGame!!.player1
        return remoteGame!!.player2
    }

    private suspend fun waitForOtherPlayerMove() {
        try {
            remoteGame = service.waitForOtherPlayer(remoteGame!!)
            fillBoard()
        } catch (e: GameForfeitedException) {
            winner = getPlayerName(_isPlayer1)
        }
        checkWin(false)
    }

    private fun fillBoard() {
        _plays = 0
        remoteGame!!.board.forEachIndexed { idx, cell ->

            if (cell == ' ')
                return@forEachIndexed

            if (cell == GameService.Player1Move) {
                _boardList[idx] = Cell.createCell(idx, CellState.X)
            } else
                _boardList[idx] = Cell.createCell(idx, CellState.O)

            _plays++
        }
    }

}