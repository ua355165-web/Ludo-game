package com.ludoroyale.app.game

enum class PlayerColor { RED, GREEN, YELLOW, BLUE }
enum class TokenStatus { BASE, ACTIVE, HOME }

data class TokenState(val id: Int, val player: PlayerColor, val progress: Int = -1) {
    val status: TokenStatus get() = when { progress < 0 -> TokenStatus.BASE; progress >= GameRules.finishProgress -> TokenStatus.HOME; else -> TokenStatus.ACTIVE }
}
data class PlayerState(val color: PlayerColor, val tokens: List<TokenState> = (0..3).map { TokenState(it, color) })
data class DiceState(val value: Int? = null, val rolling: Boolean = false)
data class GameState(
    val players: List<PlayerState> = PlayerColor.entries.map { PlayerState(it) },
    val currentPlayer: PlayerColor = PlayerColor.RED,
    val dice: DiceState = DiceState(),
    val legalTokenIds: Set<Int> = emptySet(),
    val winner: PlayerColor? = null,
    val paused: Boolean = false,
    val lastCapture: Pair<PlayerColor, Int>? = null,
    val lastHome: Pair<PlayerColor, Int>? = null
)

object GameRules {
    const val trackLength = 52
    const val finishProgress = 56
    const val releaseRoll = 6
    val safeTrackPositions = setOf(0, 8, 13, 21, 26, 34, 39, 47)
    val startOffsets = mapOf(PlayerColor.RED to 0, PlayerColor.GREEN to 13, PlayerColor.YELLOW to 26, PlayerColor.BLUE to 39)
    fun isSafe(player: PlayerColor, progress: Int): Boolean {
        if (progress < 0 || progress >= trackLength) return true
        return ((startOffsets.getValue(player) + progress) % trackLength) in safeTrackPositions
    }
}

object BoardPath {
    fun trackPosition(player: PlayerColor, progress: Int): Int? =
        if (progress in 0 until GameRules.trackLength) ((GameRules.startOffsets.getValue(player) + progress) % GameRules.trackLength) else null
}

object MoveValidator {
    fun legalTokens(state: GameState, roll: Int): Set<Int> = state.players.first { it.color == state.currentPlayer }.tokens.filter { canMove(it, roll) }.map { it.id }.toSet()
    fun canMove(token: TokenState, roll: Int): Boolean = roll in 1..6 && when { token.status == TokenStatus.BASE -> roll == GameRules.releaseRoll; token.status == TokenStatus.HOME -> false; else -> token.progress + roll <= GameRules.finishProgress }
}

object WinnerDetector { fun winner(player: PlayerState): PlayerColor? = player.takeIf { it.tokens.all { token -> token.status == TokenStatus.HOME } }?.color }

object TurnManager {
    fun next(current: PlayerColor): PlayerColor = PlayerColor.entries[(current.ordinal + 1) % PlayerColor.entries.size]
    fun afterMove(state: GameState, rolled: Int): GameState = state.copy(currentPlayer = if (rolled == 6) state.currentPlayer else next(state.currentPlayer), dice = DiceState())
}

class LudoGameEngine(private val random: () -> Int = { (1..6).random() }) {
    var state: GameState = GameState(); private set
    fun reset() { state = GameState() }
    fun togglePause() { state = state.copy(paused = !state.paused) }
    fun passTurn() { if (state.dice.value != null && state.legalTokenIds.isEmpty()) state = TurnManager.afterMove(state, state.dice.value!!) }
    fun roll(): GameState {
        if (state.paused || state.winner != null || state.dice.rolling || state.dice.value != null) return state
        val value = random().coerceIn(1, 6)
        state = state.copy(dice = DiceState(value), legalTokenIds = MoveValidator.legalTokens(state, value), lastCapture = null, lastHome = null)
        return state
    }
    fun move(tokenId: Int): GameState {
        val roll = state.dice.value ?: return state
        if (tokenId !in state.legalTokenIds) return state
        val player = state.players.first { it.color == state.currentPlayer }
        val token = player.tokens.first { it.id == tokenId }
        val newProgress = if (token.status == TokenStatus.BASE) 0 else token.progress + roll
        val updatedToken = token.copy(progress = newProgress)
        val captured = capture(updatedToken, state.players)
        val players = state.players.map { p ->
            when (p.color) { state.currentPlayer -> p.copy(tokens = p.tokens.map { if (it.id == tokenId) updatedToken else it }); captured?.first -> p.copy(tokens = p.tokens.map { if (it.id == captured.second) it.copy(progress = -1) else it }); else -> p }
        }
        val updated = state.copy(players = players, legalTokenIds = emptySet(), lastCapture = captured, lastHome = if (newProgress == GameRules.finishProgress) state.currentPlayer to tokenId else null)
        val winner = WinnerDetector.winner(players.first { it.color == state.currentPlayer })
        state = if (winner != null) updated.copy(winner = winner, dice = DiceState()) else TurnManager.afterMove(updated, roll)
        return state
    }
    private fun capture(token: TokenState, players: List<PlayerState>): Pair<PlayerColor, Int>? {
        val pos = BoardPath.trackPosition(token.player, token.progress) ?: return null
        if (GameRules.isSafe(token.player, token.progress)) return null
        return players.asSequence().filter { it.color != token.player }.flatMap { it.tokens.asSequence() }.firstOrNull { BoardPath.trackPosition(it.player, it.progress) == pos }?.let { it.player to it.id }
    }
}
