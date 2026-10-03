package com.ludoroyale.app.online
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
sealed class OnlineUiState{data object Idle:OnlineUiState();data object Loading:OnlineUiState();data class Ready(val room:OnlineRoom?=null):OnlineUiState();data class Error(val message:String):OnlineUiState()}
class OnlineViewModel(private val repo:OnlineRepository=OfflineOnlineRepository()):ViewModel(){private val _state=MutableStateFlow<OnlineUiState>(OnlineUiState.Idle);val state=_state.asStateFlow();var player=OnlinePlayer("guest-local","Local Guest");fun guest(){viewModelScope.launch{_state.value=OnlineUiState.Loading;when(val r=repo.signInGuest()){is OnlineResult.Success->{player=r.value;_state.value=OnlineUiState.Ready()};is OnlineResult.Failure->_state.value=OnlineUiState.Error(r.message)}}};fun create(capacity:Int){viewModelScope.launch{_state.value=OnlineUiState.Loading;when(val r=repo.createRoom(player,capacity)){is OnlineResult.Success->_state.value=OnlineUiState.Ready(r.value);is OnlineResult.Failure->_state.value=OnlineUiState.Error(r.message)}}};fun join(code:String){viewModelScope.launch{_state.value=OnlineUiState.Loading;when(val r=repo.joinRoom(code,player)){is OnlineResult.Success->_state.value=OnlineUiState.Ready(r.value);is OnlineResult.Failure->_state.value=OnlineUiState.Error(r.message)}}}}
