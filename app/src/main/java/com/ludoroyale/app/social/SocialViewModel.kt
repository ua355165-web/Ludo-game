package com.ludoroyale.app.social
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
class SocialViewModel(private val repo:SocialRepository=SocialRepository()):ViewModel(){private val _message=MutableStateFlow<String?>(null);val message=_message.asStateFlow();private val _notifications=MutableStateFlow<List<AppNotification>>(emptyList());val notifications=_notifications.asStateFlow();fun sendRequest(from:String,to:String){viewModelScope.launch{repo.sendFriendRequest(from,to).onSuccess{_message.value="Friend request sent"}.onFailure{_message.value=it.message}}};fun accept(id:String)=updateRequest(id,"ACCEPTED");fun decline(id:String)=updateRequest(id,"DECLINED");fun updateRequest(id:String,status:String){viewModelScope.launch{repo.updateRequest(id,status).onSuccess{_message.value="Request updated"}.onFailure{_message.value=it.message}}};fun invite(from:String,to:String,room:String){viewModelScope.launch{repo.createInvite(from,to,room).onSuccess{_message.value="Room invite sent"}.onFailure{_message.value=it.message}}};fun markRead(id:String){viewModelScope.launch{repo.markRead(id)}};fun quickChat(room:String,from:String,chat:QuickChat){viewModelScope.launch{repo.sendQuickChat(room,from,chat)}}}
