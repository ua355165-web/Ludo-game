package com.ludoroyale.app.social

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.UUID

data class FriendProfile(val playerId:String,val username:String,val online:Boolean=false)
data class FriendRequest(val id:String,val fromId:String,val toId:String,val status:String="PENDING")
data class RoomInvite(val id:String,val roomCode:String,val fromId:String,val toId:String,val expiresAt:Long,val status:String="PENDING")
data class AppNotification(val id:String,val recipientId:String,val type:String,val title:String,val body:String,val read:Boolean=false,val createdAt:Long=System.currentTimeMillis())
data class QuickChat(val label:String,val emoji:String="")
object QuickChats { val all=listOf(QuickChat("Good game!","🎉"),QuickChat("Nice move!","👍"),QuickChat("Good luck!","😄"),QuickChat("Well played!","🔥"),QuickChat("Your turn!")) }
class SocialRepository(private val db:FirebaseFirestore=FirebaseFirestore.getInstance()) {
 suspend fun sendFriendRequest(from:String,to:String):Result<Unit>{if(from==to)return Result.failure(Exception("You cannot add yourself"));return try{val id="${from}_$to";val ref=db.collection("friendRequests").document(id);if(ref.get().await().exists())return Result.failure(Exception("Request already exists"));ref.set(FriendRequest(id,from,to)).await();Result.success(Unit)}catch(e:Exception){Result.failure(Exception("Request failed"))}}
 suspend fun updateRequest(id:String,status:String)=runCatching{db.collection("friendRequests").document(id).update("status",status).await()}
 suspend fun removeFriend(owner:String,friend:String)=runCatching{db.collection("friends").document("${owner}_$friend").delete().await()}
 suspend fun createInvite(from:String,to:String,room:String)=runCatching{val id=UUID.randomUUID().toString();db.collection("roomInvites").document(id).set(RoomInvite(id,room,from,to,System.currentTimeMillis()+3600000)).await()}
 suspend fun updateInvite(id:String,status:String)=runCatching{db.collection("roomInvites").document(id).update("status",status).await()}
 suspend fun markRead(id:String)=runCatching{db.collection("notifications").document(id).update("read",true).await()}
 suspend fun sendQuickChat(room:String,from:String,chat:QuickChat)=runCatching{db.collection("rooms").document(room).collection("quickChat").add(mapOf("fromId" to from,"label" to chat.label,"emoji" to chat.emoji,"createdAt" to System.currentTimeMillis())).await()}
}
