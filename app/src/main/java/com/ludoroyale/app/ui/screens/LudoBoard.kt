package com.ludoroyale.app.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ludoroyale.app.game.*
import com.ludoroyale.app.viewmodel.GameViewModel
import com.ludoroyale.app.progression.ProgressionViewModel

private val trackCoordinates = buildList {
    for (c in 1..13) add(BoardCoordinate(6, c))
    for (r in 7..13) add(BoardCoordinate(r, 13))
    for (c in 12 downTo 1) add(BoardCoordinate(13, c))
    for (r in 12 downTo 1) add(BoardCoordinate(r, 1))
    addAll(listOf(BoardCoordinate(1, 2), BoardCoordinate(1, 3), BoardCoordinate(1, 4), BoardCoordinate(1, 5)))
}
private val safeIndices = setOf(0, 8, 13, 21, 26, 34, 39, 47)
private fun playerColor(color: PlayerColor) = when (color) { PlayerColor.RED -> Color(0xFFE85D75); PlayerColor.GREEN -> Color(0xFF31A77A); PlayerColor.YELLOW -> Color(0xFFE4A82E); PlayerColor.BLUE -> Color(0xFF4E79D9) }
private fun soft(color: PlayerColor) = playerColor(color).copy(alpha = .18f)

@Composable
fun LudoGameScreen(onBack: () -> Unit, audio: GameAudioManager, vm: GameViewModel = viewModel(), progression: ProgressionViewModel? = null, wallet: com.ludoroyale.app.store.WalletViewModel? = null) {
    val state by vm.state.collectAsState()
    var showPause by remember { mutableStateOf(false) }
    var rolling by remember { mutableStateOf(false) }
    var lastDice by remember { mutableIntStateOf(0) }
    var recordedWinner by remember { mutableStateOf<PlayerColor?>(null) }
    val scope = rememberCoroutineScope()
    LaunchedEffect(state, vm.practiceConfig) { vm.playBotTurnIfNeeded() }
    LaunchedEffect(state.winner) { state.winner?.let { if (it != recordedWinner) { progression?.completeGame(it == PlayerColor.RED, state.lastCapture?.let { 1 } ?: 0, state.lastHome?.let { 1 } ?: 0); recordedWinner = it } } }
    Column(Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) { Text("‹", fontSize = 34.sp, color = Color(0xFF6C5CE7)) }
            Column(Modifier.weight(1f)) { Text("Ludo table", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold); Text("${state.currentPlayer.name.lowercase().replaceFirstChar { it.uppercase() }}'s turn", color = playerColor(state.currentPlayer), fontSize = 13.sp) }
            IconButton(onClick = { showPause = true }) { Text("Ⅱ", fontWeight = FontWeight.Black, color = Color(0xFF6C5CE7)) }
            Surface(shape = RoundedCornerShape(18.dp), color = soft(state.currentPlayer)) { Text(state.currentPlayer.name, color = playerColor(state.currentPlayer), fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)) }
        }
        LudoBoard(state, vm::move, wallet?.wallet?.collectAsState()?.value?.equipped ?: emptyMap())
        PlayerStatusCard(state)
        Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(22.dp)).background(soft(state.currentPlayer)).padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) { Text("${state.currentPlayer.name} to roll", fontWeight = FontWeight.Bold); Text(if (vm.botThinking) "Bot is thinking..." else if (state.legalTokenIds.isNotEmpty()) "Choose a highlighted token" else "Roll a 1 to 6", color = if (vm.botThinking) playerColor(state.currentPlayer) else Color.Gray, fontSize = 12.sp) }
            val diceRotation by animateFloatAsState(if (rolling) 360f else 0f, animationSpec = spring(), label = "diceRotation")
            Surface(shape = RoundedCornerShape(14.dp), color = Color.White, shadowElevation = 6.dp) { Text(" ${state.dice.value ?: "•"} ", fontSize = 25.sp, fontWeight = FontWeight.Black, modifier = Modifier.padding(8.dp).graphicsLayer { rotationZ = diceRotation }) }
            Spacer(Modifier.width(10.dp)); Button(enabled = state.dice.value == null && state.winner == null && !state.paused && !rolling && !vm.isBotTurn(), onClick = { audio.buttonClick(); audio.diceRoll(); rolling = true; scope.launch { delay(420); vm.roll(); rolling = false } }, shape = RoundedCornerShape(16.dp)) { Text(if (rolling) "..." else "Roll") }
        }
    LaunchedEffect(state.lastCapture) { if (state.lastCapture != null) audio.capture() }
    LaunchedEffect(state.lastHome) { if (state.lastHome != null) audio.tokenHome() }
    LaunchedEffect(state.winner) { if (state.winner != null) audio.win() }
    state.lastCapture?.let { (color, id) -> Text("Capture! ${color.name} token $id returned to base.", color = Color(0xFFE85D75), fontWeight = FontWeight.Bold, fontSize = 12.sp) }
        state.lastHome?.let { (color, _) -> Text("Home run! ${color.name} reached the finish.", color = Color(0xFF31A77A), fontWeight = FontWeight.Bold, fontSize = 12.sp) }
        state.winner?.let { winner -> Text("${winner.name} wins! Restart to play again.", color = playerColor(winner), fontWeight = FontWeight.Bold) }
    }
    state.winner?.let { winner -> AlertDialog(onDismissRequest = {}, title = { Text("${winner.name} wins the royale!") }, text = { Text("All four tokens reached home.") }, confirmButton = { TextButton(onClick = vm::restart) { Text("Play again") } }, dismissButton = { TextButton(onClick = onBack) { Text("Back to Home") } }) }
    if (showPause) AlertDialog(onDismissRequest = { showPause = false }, title = { Text("Game paused") }, text = { Text("Take a breath, then jump back in.") }, confirmButton = { TextButton(onClick = { showPause = false; vm.pause() }) { Text("Resume") } }, dismissButton = { TextButton(onClick = { vm.restart(); showPause = false }) { Text("Restart") } })
}

@Composable
private fun PlayerStatusCard(state: GameState) {
    val active = state.players.first { it.color == state.currentPlayer }
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        tonalElevation = 3.dp
    ) {
        Row(Modifier.padding(horizontal = 14.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(34.dp).clip(CircleShape).background(playerColor(state.currentPlayer)), contentAlignment = Alignment.Center) {
                Text(state.currentPlayer.name.take(1), color = Color.White, fontWeight = FontWeight.Black)
            }
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Text("${state.currentPlayer.name} • 4 tokens", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text("${active.tokens.count { it.status == TokenStatus.HOME }} home  ·  ${active.tokens.count { it.status == TokenStatus.ACTIVE }} on track", color = Color.Gray, fontSize = 11.sp)
            }
            Text(if (state.legalTokenIds.isEmpty()) "YOUR TURN" else "${state.legalTokenIds.size} MOVES", color = playerColor(state.currentPlayer), fontWeight = FontWeight.Black, fontSize = 10.sp)
        }
    }
}

@Composable
fun LudoBoard(state: GameState, onTokenSelected: (Int) -> Unit, equipped: Map<com.ludoroyale.app.store.StoreCategory,String> = emptyMap()) {
    BoxWithConstraints(Modifier.fillMaxWidth().aspectRatio(1f).clip(RoundedCornerShape(28.dp)).background(Brush.linearGradient(listOf(Color(0xFFFDFBFF), Color(0xFFEDE9FF)))).padding(6.dp)) {
        Column(Modifier.fillMaxSize()) { repeat(15) { row -> Row(Modifier.weight(1f)) { repeat(15) { column -> BoardCell(BoardCoordinate(row, column), state, onTokenSelected, equipped) } } } }
    }
}

@Composable
private fun BoardCell(coordinate: BoardCoordinate, state: GameState, onTokenSelected: (Int) -> Unit, equipped: Map<com.ludoroyale.app.store.StoreCategory,String>) {
    val trackIndex = trackCoordinates.indexOf(coordinate)
    val zone = when { coordinate.row < 6 && coordinate.column < 6 -> PlayerColor.RED; coordinate.row < 6 && coordinate.column > 8 -> PlayerColor.GREEN; coordinate.row > 8 && coordinate.column < 6 -> PlayerColor.BLUE; coordinate.row > 8 && coordinate.column > 8 -> PlayerColor.YELLOW; else -> null }
    val home = when { coordinate.column in 7..8 && coordinate.row in 1..5 -> PlayerColor.GREEN; coordinate.row in 7..8 && coordinate.column in 1..5 -> PlayerColor.RED; coordinate.column in 7..8 && coordinate.row in 9..13 -> PlayerColor.YELLOW; coordinate.row in 7..8 && coordinate.column in 9..13 -> PlayerColor.BLUE; else -> null }
    val center = coordinate.row in 6..8 && coordinate.column in 6..8
    val bg = when { center -> Color(0xFFF4F0FF); home != null -> soft(home); trackIndex >= 0 -> Color.White; else -> zone?.let(::soft) ?: Color.Transparent }
    Box(Modifier.weight(1f).fillMaxHeight().padding(1.dp).clip(RoundedCornerShape(3.dp)).background(bg), contentAlignment = Alignment.Center) {
        when { center -> Box(Modifier.size(16.dp).clip(CircleShape).background(Brush.linearGradient(listOf(Color(0xFFFF6B5F), Color(0xFF6C5CE7))))); trackIndex >= 0 && trackIndex in safeIndices -> Text("✦", color = Color(0xFF6C5CE7), fontSize = 9.sp); home != null -> Box(Modifier.size(4.dp).clip(CircleShape).background(playerColor(home))) }
        val onTrack = if (trackIndex >= 0) state.players.flatMap { it.tokens }.filter { BoardPath.trackPosition(it.player, it.progress) == trackIndex } else emptyList()
        onTrack.forEach { token -> LudoToken(token, token.player == state.currentPlayer && token.id in state.legalTokenIds, onTokenSelected, equipped[com.ludoroyale.app.store.StoreCategory.TOKEN_SKIN]) }
        val baseTokens = state.players.firstOrNull { it.color == zone }?.tokens?.filter { it.status == TokenStatus.BASE } ?: emptyList()
        if (zone != null && coordinate in baseSpots(zone)) baseTokens.getOrNull(baseSpots(zone).indexOf(coordinate))?.let { LudoToken(it, it.player == state.currentPlayer && it.id in state.legalTokenIds, onTokenSelected, equipped[com.ludoroyale.app.store.StoreCategory.TOKEN_SKIN]) }
    }
}
private fun baseSpots(color: PlayerColor) = when (color) { PlayerColor.RED -> listOf(BoardCoordinate(2,2), BoardCoordinate(2,4), BoardCoordinate(4,2), BoardCoordinate(4,4)); PlayerColor.GREEN -> listOf(BoardCoordinate(2,10), BoardCoordinate(2,12), BoardCoordinate(4,10), BoardCoordinate(4,12)); PlayerColor.BLUE -> listOf(BoardCoordinate(10,2), BoardCoordinate(10,4), BoardCoordinate(12,2), BoardCoordinate(12,4)); PlayerColor.YELLOW -> listOf(BoardCoordinate(10,10), BoardCoordinate(10,12), BoardCoordinate(12,10), BoardCoordinate(12,12)) }

@Composable private fun LudoToken(token: TokenState, legal: Boolean, onClick: (Int) -> Unit, skin: String? = null) { val c = playerColor(token.player); val tint by animateColorAsState(if (legal) Color.White else if (skin == "token_sunset") Color(0xFFFF8A65) else if (skin == "token_ocean") Color(0xFF49A7DE) else c, label = "token"); val scale by animateFloatAsState(if (legal) 1.25f else 1f, animationSpec = spring(), label = "tokenScale"); Box(Modifier.size(16.dp).graphicsLayer { scaleX = scale; scaleY = scale }.clip(CircleShape).background(tint).clickable(enabled = legal) { onClick(token.id) }, contentAlignment = Alignment.Center) { if (legal) Box(Modifier.size(5.dp).clip(CircleShape).background(c)) } }
