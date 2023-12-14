package isel.pdm.demos.tictactoe.infrastructure

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import isel.pdm.demos.tictactoe.domain.game.lobby.Challenge
import isel.pdm.demos.tictactoe.domain.game.lobby.ChallengeReceived
import isel.pdm.demos.tictactoe.domain.game.lobby.Lobby
import isel.pdm.demos.tictactoe.domain.game.lobby.LobbyEvent
import isel.pdm.demos.tictactoe.domain.game.lobby.PlayerInfo
import isel.pdm.demos.tictactoe.domain.game.lobby.RosterUpdated
import isel.pdm.demos.tictactoe.domain.user.UserInfo
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID

/**
 * Sum type that represents the possible states of the lobby.
 */
sealed class LobbyState

/**
 * Represents the idle state of the lobby, i.e. when the local player is not inside the lobby.
 */
data object Idle : LobbyState()

/**
 * Represents the state of the lobby when the local player is entering the lobby, but is not yet inside.
 * This state is used to prevent the local player from entering the lobby more than once.
 */
data object Entering : LobbyState()

/**
 * Represents the state of the lobby when the local player is inside the lobby.
 * @property localPlayer The local player's info.
 * @property localPlayerDocRef The reference to the local player's document in the Firestore DB.
 * @property producer The [ProducerScope] used to produce the lobby events, or to the close the flow
 */
data class InsideLobby(
    val localPlayer: PlayerInfo,
    val localPlayerDocRef: DocumentReference,
    val producer: ProducerScope<LobbyEvent>
) : LobbyState()

/**
 * Implementation of the Game's lobby using Firebase's Firestore.
 */
class LobbyFirebase(private val db: FirebaseFirestore) : Lobby {

    private var state: LobbyState = Idle

    override suspend fun enter(localPlayer: PlayerInfo): Flow<LobbyEvent> {
        check(state is Idle) { "The local player is already inside the lobby" }
        state = Entering

        return callbackFlow {
            val localPlayerDocRef = getPlayerDocRef(localPlayer)
            state = InsideLobby(localPlayer, localPlayerDocRef, this)
            val roasterUpdatedSubscription = subscribeRoasterUpdated(this)
            val challengeReceivedSubscription = subscribeChallengeReceived(localPlayerDocRef, this)

            localPlayerDocRef.set(localPlayer.info.toDocumentContent()).await()

            awaitClose {
                roasterUpdatedSubscription.remove()
                challengeReceivedSubscription.remove()
                localPlayerDocRef.delete()
                state = Idle
            }
        }
    }

    override suspend fun issueChallenge(to: PlayerInfo): Challenge {
        state.let {
            check(it is InsideLobby) { "The local player is not inside the lobby" }
            db.collection(LOBBY)
                .document(to.id.toString())
                .update(CHALLENGER_FIELD, it.localPlayer.toDocumentContent())
                .await()
            return Challenge(challenger = it.localPlayer, challenged = to)
        }
    }

    override suspend fun leave() {
        state.let {
            check(it is InsideLobby) { "The local player is not inside the lobby" }
            it.localPlayerDocRef.delete().await()
            it.producer.close()
        }
    }

    private fun subscribeRoasterUpdated(flow: ProducerScope<LobbyEvent>) =
        db.collection(LOBBY).addSnapshotListener { snapshot, error ->
            when {
                error != null -> flow.close(error)
                snapshot != null -> flow.trySend(RosterUpdated(snapshot.toPlayerList()))
            }
        }

    private fun subscribeChallengeReceived(
        localPlayerDocRef: DocumentReference,
        flow: ProducerScope<LobbyEvent>
    ) = localPlayerDocRef.addSnapshotListener { snapshot, error ->
        when {
            error != null -> flow.close(error)
            snapshot != null -> snapshot.toChallengeOrNull()?.let {
                flow.trySend(ChallengeReceived(it))
            }
        }
    }

    private fun getPlayerDocRef(localPlayer: PlayerInfo) =
        db.collection(LOBBY).document(localPlayer.id.toString())
}

/**
 * Names of the fields used in the document representations.
 */
const val LOBBY = "lobby"
const val NICK_FIELD = "nick"
const val MOTTO_FIELD = "motto"
const val CHALLENGER_FIELD = "challenger"
const val CHALLENGER_ID_FIELD = "id"

/**
 * Extension function used to convert player info documents stored in the Firestore DB
 * into [PlayerInfo] instances.
 */
fun QueryDocumentSnapshot.toPlayerInfo() =
    PlayerInfo(
        info = UserInfo(
            nick = data[NICK_FIELD] as String,
            motto = data[MOTTO_FIELD] as String?
        ),
        id = UUID.fromString(id),
    )

/**
 * [PlayerInfo] extension function used to convert an instance to a map of key-value
 * pairs containing the object's properties
 */
fun PlayerInfo.toDocumentContent() = mapOf(
    NICK_FIELD to info.nick,
    MOTTO_FIELD to info.motto,
    CHALLENGER_ID_FIELD to id.toString()
)

/**
 * Extension function used to convert challenge documents stored in the Firestore DB
 * into [Challenge] instances.
 */
@Suppress("UNCHECKED_CAST")
fun DocumentSnapshot.toChallengeOrNull(): Challenge? {
    val docData = data
    if (docData != null) {
        val challenger = docData[CHALLENGER_FIELD] as Map<String, String>?
        if (challenger != null) {
            return Challenge(
                challenger = playerInfoFromDocContent(challenger),
                challenged = this.toPlayerInfo()
            )
        }
    }
    return null
}

fun DocumentSnapshot.toPlayerInfo() = PlayerInfo(
    info = UserInfo(
        nick = data?.get(NICK_FIELD) as String,
        motto = data?.get(MOTTO_FIELD) as String?
    ),
    id = UUID.fromString(id)
)

/**
 * Extension function used to convert player info documents stored in the Firestore DB
 * into [PlayerInfo] instances.
 */
fun playerInfoFromDocContent(properties: Map<String, Any>) = PlayerInfo(
    info = UserInfo(
        nick = properties[NICK_FIELD] as String,
        motto = properties[MOTTO_FIELD] as String?,
    ),
    id = UUID.fromString(properties[CHALLENGER_ID_FIELD] as String)
)

fun QuerySnapshot.toPlayerList() = map { it.toPlayerInfo() }

/**
 * [UserInfo] extension function used to convert an instance to a map of key-value
 * pairs containing the object's properties
 */
fun UserInfo.toDocumentContent() = mapOf(
    NICK_FIELD to nick,
    MOTTO_FIELD to motto
)
