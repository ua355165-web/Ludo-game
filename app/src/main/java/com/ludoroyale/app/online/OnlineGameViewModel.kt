package com.ludoroyale.app.online

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ListenerRegistration
import com.ludoroyale.app.game.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class OnlineGameViewModel(private val repository: OnlineGameRepository = OnlineGameRepository()) : ViewModel() {
    private val _snapshot = MutableStateFlow<OnlineGameSnapshot?>(null)
    val snapshot = _snapshot.asStateFlow()
    private val _message = MutableStateFlow<String?>(null)
    val message = _message.asStateFlow()
    private var listener: ListenerRegistration? = null
    fun observe(roomCode: String) { listener?.remove(); listener = repository.observe(roomCode, { _snapshot.value = it; _message.value = null }, { _message.value = it }) }
    fun start(room: OnlineRoom, hostId: String) = viewModelScope.launch { when (val r = repository.startGame(room, hostId)) { is OnlineResult.Success -> _snapshot.value = r.value; is OnlineResult.Failure -> _message.value = r.message } }
    fun submitRoll(playerId: String, color: PlayerColor, version: Long, dice: Int) = submit(OnlineAction(UUID.randomUUID().toString(), playerId, "ROLL", dice = dice, version = version), color)
    fun submitMove(playerId: String, color: PlayerColor, version: Long, tokenId: Int) = submit(OnlineAction(UUID.randomUUID().toString(), playerId, "MOVE", tokenId = tokenId, version = version), color)
    private fun submit(action: OnlineAction, color: PlayerColor) = viewModelScope.launch { val room = _snapshot.value?.roomCode ?: return@launch; when (val r = repository.submitAction(room, action, color)) { is OnlineResult.Success -> Unit; is OnlineResult.Failure -> _message.value = r.message } }
    fun leave(playerId: String) { _snapshot.value?.roomCode?.let { code -> viewModelScope.launch { repository.leave(code, playerId) } } }
    override fun onCleared() { listener?.remove() }
}
