package com.ludoroyale.app.bot

import com.ludoroyale.app.game.*

enum class BotDifficulty(val label: String, val description: String) { EASY("Easy", "Relaxed, mostly random choices"), NORMAL("Normal", "Balances progress and captures"), HARD("Hard", "Targets the strongest legal move") }

data class PracticeConfig(val bots: Int = 3, val difficulty: BotDifficulty = BotDifficulty.NORMAL) {
    val botColors: Set<PlayerColor> get() = PlayerColor.entries.drop(1).take(bots).toSet()
}

class BotPlayer(val color: PlayerColor, val difficulty: BotDifficulty) {
    fun chooseToken(state: GameState): Int? {
        val player = state.players.firstOrNull { it.color == color } ?: return null
        val legal = player.tokens.filter { it.id in state.legalTokenIds }
        if (legal.isEmpty()) return null
        return when (difficulty) {
            BotDifficulty.EASY -> legal.random().id
            BotDifficulty.NORMAL -> legal.maxWithOrNull(compareBy<TokenState> { captureScore(it, state) }.thenBy { progressScore(it) })?.id
            BotDifficulty.HARD -> legal.maxWithOrNull(compareBy<TokenState> { homeScore(it) }.thenBy { captureScore(it, state) }.thenBy { progressScore(it) })?.id
        }
    }
    private fun progressScore(token: TokenState) = if (token.status == TokenStatus.BASE) 1 else token.progress
    private fun homeScore(token: TokenState) = if (token.progress >= GameRules.finishProgress - 6) 100 else progressScore(token)
    private fun captureScore(token: TokenState, state: GameState): Int {
        val position = BoardPath.trackPosition(token.player, token.progress) ?: return 0
        return if (!GameRules.isSafe(token.player, token.progress) && state.players.any { it.color != color && it.tokens.any { BoardPath.trackPosition(it.player, it.progress) == position } }) 100 else 0
    }
}
