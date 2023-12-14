package isel.pdm.demos.tictactoe.ui.game.play

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import isel.pdm.demos.tictactoe.domain.game.lobby.Challenge
import isel.pdm.demos.tictactoe.domain.game.lobby.PlayerInfo
import kotlinx.parcelize.Parcelize

/**
 * The name of the extra that contains the user information.
 */
private const val CHALLENGE_EXTRA = "Challenge"

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
            intent.putExtra(CHALLENGE_EXTRA, MatchInfoExtra(localPlayer, challenge))
            return intent
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TODO()
        }
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
