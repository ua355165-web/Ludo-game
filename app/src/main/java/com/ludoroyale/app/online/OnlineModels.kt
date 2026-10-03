package com.ludoroyale.app.online

enum class RoomState { WAITING, STARTED, CLOSED }
data class OnlinePlayer(val playerId:String,val username:String,val avatar:String="default",val level:Int=1,val xp:Int=0,val online:Boolean=true,val ready:Boolean=false)
data class OnlineRoom(val code:String,val hostId:String,val players:List<OnlinePlayer>,val capacity:Int=4,val state:RoomState=RoomState.WAITING){val isFull get()=players.size>=capacity}
data class OnlineGame(val roomCode:String,val participantIds:List<String>,val stateJson:String="",val started:Boolean=false)
data class PlayerConnection(val playerId:String,val connected:Boolean,val lastSeenMillis:Long)
sealed class OnlineResult<out T>{data class Success<T>(val value:T):OnlineResult<T>();data class Failure(val message:String):OnlineResult<Nothing>()}
