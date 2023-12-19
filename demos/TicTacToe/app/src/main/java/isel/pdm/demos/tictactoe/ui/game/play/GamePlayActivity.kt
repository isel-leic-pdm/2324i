package isel.pdm.demos.tictactoe.ui.game.play

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.lifecycleScope
import isel.pdm.demos.tictactoe.DependenciesContainer
import isel.pdm.demos.tictactoe.domain.game.lobby.Challenge
import isel.pdm.demos.tictactoe.domain.game.lobby.PlayerInfo
import isel.pdm.demos.tictactoe.domain.user.UserInfo
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import java.util.UUID

/**
 * The name of the extra that contains the match information.
 */
private const val MATCH_INFO_EXTRA = "Challenge"

/**
 * The activity that hosts the screen used to play the game.
 */
class GamePlayActivity : ComponentActivity() {

    companion object {
        /**
         * Navigates to the [GamePlayActivity] activity.
         * @param ctx the context to be used.
         * @param localPlayer the local player information.
         * @param challenge the challenge containing the players' information.
         */
        fun navigateTo(ctx: Context, localPlayer: PlayerInfo, challenge: Challenge) {
            ctx.startActivity(createIntent(ctx, localPlayer, challenge))
        }

        /**
         * Builds the intent used to navigate to the [GamePlayActivity] activity.
         * @param ctx the context to be used.
         * @param localPlayer the local player information.
         * @param challenge the challenge containing the players' information.
         */
        fun createIntent(ctx: Context, localPlayer: PlayerInfo, challenge: Challenge): Intent {
            val intent = Intent(ctx, GamePlayActivity::class.java)
            intent.putExtra(MATCH_INFO_EXTRA, MatchInfoExtra(localPlayer, challenge))
            return intent
        }
    }

    private val vm by viewModels<GamePlayScreenViewModel> {
        GamePlayScreenViewModel.factory((application as DependenciesContainer).matchFactory())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            vm.screenState.collect { state ->
                if (state is GamePlayScreenState.Idle)
                    vm.startMatch(localPlayer, challenge)
            }
        }
        setContent {
            val currentState: GamePlayScreenState by vm.screenState.collectAsState(initial = GamePlayScreenState.Idle)
            GamePlayScreen(
                state = currentState,
                onMoveRequested = { at -> vm.makeMove(at) },
                onForfeitRequested = { finish() }
            )
        }

        onBackPressedDispatcher.addCallback(owner = this, enabled = true) {
            vm.forfeit()
            finish()
        }
    }

    /**
     * The local player information.
     */
    private val localPlayer: PlayerInfo by lazy {
        PlayerInfo(
            info = UserInfo(matchInfoExtra.localPlayerNick),
            id = UUID.fromString(matchInfoExtra.localPlayerId)
        )
    }

    /**
     * The challenge that originated the match.
     */
    private val challenge: Challenge by lazy {
        val opponent = PlayerInfo(
            info = UserInfo(matchInfoExtra.opponentNick),
            id = UUID.fromString(matchInfoExtra.opponentId)
        )

        if (localPlayer.id.toString() == matchInfoExtra.challengerId)
            Challenge(challenger = localPlayer, challenged = opponent)
        else
            Challenge(challenger = opponent, challenged = localPlayer)
    }

    /**
     * Helper method to get the match info extra from the intent.
     */
    private val matchInfoExtra: MatchInfoExtra by lazy {
        val extra = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU)
            intent.getParcelableExtra(MATCH_INFO_EXTRA, MatchInfoExtra::class.java)
        else
            intent.getParcelableExtra(MATCH_INFO_EXTRA)

        checkNotNull(extra) { "No match info extra found in intent" }
    }
}

@Parcelize
internal data class MatchInfoExtra(
    val localPlayerId: String,
    val localPlayerNick: String,
    val opponentId: String,
    val opponentNick: String,
    val challengerId: String,
) : Parcelable


internal fun MatchInfoExtra(localPlayer: PlayerInfo, challenge: Challenge): MatchInfoExtra {
    val opponent =
        if (localPlayer == challenge.challenged) challenge.challenger
        else challenge.challenged

    return MatchInfoExtra(
        localPlayerId = localPlayer.id.toString(),
        localPlayerNick = localPlayer.info.nick,
        opponentId = opponent.id.toString(),
        opponentNick = opponent.info.nick,
        challengerId = challenge.challenger.id.toString(),
    )
}
