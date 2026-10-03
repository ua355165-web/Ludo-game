package com.ludoroyale.app.online

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.SetOptions
import com.ludoroyale.app.game.*
import kotlinx.coroutines.tasks.await

/** Firestore transport only. Rules remain in LudoGameEngine and MoveValidator. */
data class OnlineGameSnapshot(
    val roomCode: String = "",
    val status: String = "WAITING",
    val currentPlayer: String = PlayerColor.RED.name,
    val dice: Int? = null,
    val tokenProgress: Map<String, Int> = emptyMap(),
    val connected: Map<String, Boolean> = emptyMap(),
    val winner: String? = null,
    val version: Long = 0L,
    val lastActionId: String? = null
)

data class OnlineAction(val actionId: String, val playerId: String, val type: String, val tokenId: Int? = null, val dice: Int? = null, val version: Long = 0L)

class OnlineGameRepository(private val db: FirebaseFirestore = FirebaseFirestore.getInstance()) {
    private fun ref(roomCode: String) = db.collection("games").document(roomCode)

    suspend fun startGame(room: OnlineRoom, hostId: String): OnlineResult<OnlineGameSnapshot> = try {
        require(room.hostId == hostId) { "Only the host can start the match" }
        require(room.players.size in 2..4) { "The match needs 2 to 4 players" }
        val seats = room.players.mapIndexed { index, player -> player.playerId to PlayerColor.entries[index].name }.toMap()
        val snapshot = OnlineGameSnapshot(room.code, "ACTIVE", seats.values.first(), tokenProgress = emptyMap(), connected = room.players.associate { it.playerId to it.online })
        ref(room.code).set(snapshot).await()
        OnlineResult.Success(snapshot)
    } catch (e: Exception) { OnlineResult.Failure(e.message ?: "Could not start match") }

    fun observe(roomCode: String, onChange: (OnlineGameSnapshot) -> Unit, onError: (String) -> Unit): ListenerRegistration =
        ref(roomCode).addSnapshotListener { snap, error ->
            when {
                error != null -> onError("Connection lost. Reconnecting…")
                snap == null || !snap.exists() -> onError("Match not found")
                else -> onChange(snap.toObject(OnlineGameSnapshot::class.java) ?: OnlineGameSnapshot(roomCode = roomCode))
            }
        }

    suspend fun submitAction(roomCode: String, action: OnlineAction, playerColor: PlayerColor): OnlineResult<Unit> = try {
        db.runTransaction { transaction ->
            val current = transaction.get(ref(roomCode)).toObject(OnlineGameSnapshot::class.java) ?: error("Match not found")
            require(current.status == "ACTIVE") { "Match is not active" }
            require(current.currentPlayer == playerColor.name) { "It is not your turn" }
            require(current.version == action.version) { "This turn is stale" }
            require(current.lastActionId != action.actionId) { "Duplicate action" }
            transaction.set(ref(roomCode), current.copy(lastActionId = action.actionId, version = current.version + 1), SetOptions.merge())
        }.await()
        OnlineResult.Success(Unit)
    } catch (e: Exception) { OnlineResult.Failure(e.message ?: "Action rejected") }

    suspend fun leave(roomCode: String, playerId: String): OnlineResult<Unit> = try {
        ref(roomCode).set(mapOf("connected" to mapOf(playerId to false)), SetOptions.merge()).await()
        OnlineResult.Success(Unit)
    } catch (e: Exception) { OnlineResult.Failure("Could not leave match") }
}
