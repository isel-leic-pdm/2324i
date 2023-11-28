package isel.pdm.demos.tictactoe.infrastructure

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import isel.pdm.demos.tictactoe.domain.game.Challenge
import isel.pdm.demos.tictactoe.domain.game.Lobby
import isel.pdm.demos.tictactoe.domain.game.LobbyEvent
import isel.pdm.demos.tictactoe.domain.game.PlayerInfo
import isel.pdm.demos.tictactoe.domain.game.RosterUpdated
import isel.pdm.demos.tictactoe.domain.user.UserInfo
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID

/**
 * Implementation of the Game's lobby using Firebase's Firestore.
 */
class LobbyFirebase(private val db: FirebaseFirestore) : Lobby {

    private var isLocalPLayerInside = false

    override suspend fun enter(localPlayer: PlayerInfo): Flow<LobbyEvent> {
        check(!isLocalPLayerInside) { "The local player is already inside the lobby" }

        isLocalPLayerInside = true

        return callbackFlow {
            val localPlayerDocRef = addLocalPlayer(localPlayer)
            val roasterUpdatedSubscription = subscribeRoasterUpdated(this)

            awaitClose {
                roasterUpdatedSubscription.remove()
                localPlayerDocRef.delete()
            }
        }
    }

    override suspend fun issueChallenge(to: PlayerInfo): Challenge {
        TODO("Not yet implemented")
    }

    override suspend fun leave() {
        TODO("Not yet implemented")
    }

    private fun subscribeRoasterUpdated(flow: ProducerScope<LobbyEvent>) =
        db.collection(LOBBY).addSnapshotListener { snapshot, error ->
            when {
                error != null -> flow.close(error)
                snapshot != null -> flow.trySend(RosterUpdated(snapshot.toPlayerList()))
            }
        }

    private suspend fun addLocalPlayer(localPlayer: PlayerInfo): DocumentReference {
        val docRef = db.collection(LOBBY).document(localPlayer.id.toString())
        docRef
            .set(localPlayer.info.toDocumentContent())
            .await()
        return docRef
    }
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

