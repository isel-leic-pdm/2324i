package pt.isel.pdm.tictactoe.services.firebase

import pt.isel.pdm.tictactoe.model.GameLobby

class FirestoreLobby(
    override val displayName: String,
    override val id: String
) : GameLobby {
    override fun toString(): String {
        return "FirestoreLobby(displayName='$displayName', gameId='$id')"
    }
}