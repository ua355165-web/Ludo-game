package com.ludoroyale.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ludoroyale.app.online.*

@Composable
fun OnlineHubScreen(vm: OnlineViewModel, onOpenMatch: (OnlineRoom) -> Unit, onBack: () -> Unit) {
    var code by remember { mutableStateOf("") }
    var capacity by remember { mutableIntStateOf(4) }
    val state by vm.state.collectAsState()
    Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        TextButton(onClick = onBack) { Text("‹  Back") }
        Text("Online royale", fontSize = 30.sp, fontWeight = FontWeight.ExtraBold)
        Text("Private rooms are optional. Local and Practice stay available offline.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        OutlinedButton(onClick = vm::guest, modifier = Modifier.fillMaxWidth()) { Text("Continue as guest") }
        Text("Room capacity", fontWeight = FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { (2..4).forEach { count -> FilterChip(selected = capacity == count, onClick = { capacity = count }, label = { Text("$count players") }) } }
        Button(onClick = { vm.create(capacity) }, modifier = Modifier.fillMaxWidth()) { Text("Create private room") }
        OutlinedTextField(code, { code = it }, label = { Text("Room code") }, modifier = Modifier.fillMaxWidth())
        OutlinedButton(onClick = { vm.join(code) }, modifier = Modifier.fillMaxWidth()) { Text("Join room") }
        when (val current = state) {
            OnlineUiState.Loading -> CircularProgressIndicator()
            is OnlineUiState.Error -> Text(current.message, color = MaterialTheme.colorScheme.error)
            is OnlineUiState.Ready -> current.room?.let { RoomCard(it, vm.player.playerId, onOpenMatch) }
            else -> Text("Sign in to create or join a room.")
        }
    }
}

@Composable
private fun RoomCard(room: OnlineRoom, currentPlayerId: String, onOpenMatch: (OnlineRoom) -> Unit) {
    ElevatedCard {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Room ${room.code}", fontWeight = FontWeight.Bold)
            Text("${room.players.size}/${room.capacity} players  ·  ${room.state}")
            room.players.forEach { Text("• ${it.username}${if (it.playerId == room.hostId) "  Host" else ""}") }
            Button(enabled = room.hostId == currentPlayerId && room.players.size >= 2, onClick = { onOpenMatch(room) }) { Text("Start game") }
            TextButton(onClick = { onOpenMatch(room) }) { Text("Open room") }
        }
    }
}

@Composable
fun OnlineMatchScreen(room: OnlineRoom, playerId: String, social: com.ludoroyale.app.social.SocialViewModel, onBack: () -> Unit) {
    val match = remember { OnlineGameViewModel() }
    val snapshot by match.snapshot.collectAsState()
    val message by match.message.collectAsState()
    LaunchedEffect(room.code) { match.observe(room.code) }
    Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        TextButton(onClick = { match.leave(playerId); onBack() }) { Text("‹  Leave match") }
        Text("Room ${room.code}", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
        Text(if (snapshot?.status == "ACTIVE") "Match is live" else "Waiting for the host to start", color = MaterialTheme.colorScheme.primary)
        room.players.forEach { player -> Text("${player.username}  ·  ${if (snapshot?.connected?.get(player.playerId) != false) "Connected" else "Disconnected"}") }
        if (room.hostId == playerId && snapshot?.status != "ACTIVE") Button(onClick = { match.start(room, playerId) }) { Text("Start match") }
        snapshot?.let { live -> Text("Turn: ${live.currentPlayer}  ·  Dice: ${live.dice ?: "waiting"}") }
        message?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        QuickChatPanel(social, room.code, playerId, false) {}
        Text("The live match uses the existing Ludo engine and board in the next online gameplay pass.", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
    }
}

@Composable
fun FriendsScreen(onBack: () -> Unit) {
    SimpleScreen("Friends", "Player ID: guest-local\nSearch, requests and friends sync when Firebase is configured.", androidx.compose.material.icons.Icons.Rounded.People, onBack)
}
