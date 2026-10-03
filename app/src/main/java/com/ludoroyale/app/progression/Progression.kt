package com.ludoroyale.app.progression

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class PlayerProgress(val playerId: String = "LR-246104", val username: String = "Alex Morgan", val xp: Int = 0, val coins: Int = 2480, val games: Int = 0, val wins: Int = 0, val captures: Int = 0, val tokensHome: Int = 0, val favoriteMode: String = "Practice") {
    val level: Int get() = LevelSystem.levelFor(xp)
    val levelStart: Int get() = LevelSystem.xpForLevel(level)
    val nextLevel: Int get() = LevelSystem.xpForLevel(level + 1)
    val levelProgress: Float get() = ((xp - levelStart).toFloat() / (nextLevel - levelStart)).coerceIn(0f, 1f)
    val losses: Int get() = (games - wins).coerceAtLeast(0)
    val winPercent: Int get() = if (games == 0) 0 else wins * 100 / games
}
object XpRewards { const val GAME = 40; const val WIN = 120; const val CAPTURE = 20; const val HOME = 15; const val ACHIEVEMENT = 60 }
object LevelSystem { fun xpForLevel(level: Int) = ((level.coerceAtLeast(1) - 1) * 250); fun levelFor(xp: Int) = (xp / 250) + 1 }
data class Achievement(val id: String, val name: String, val description: String, val icon: String, val target: Int, val progress: Int, val unlocked: Boolean)
object AchievementCatalog {
    fun all(p: PlayerProgress): List<Achievement> = listOf(
        Achievement("first_game", "First Game", "Complete your first match", "✦", 1, p.games, p.games >= 1), Achievement("first_win", "First Victory", "Win your first match", "♛", 1, p.wins, p.wins >= 1), Achievement("five_wins", "On Fire", "Win 5 matches", "🔥", 5, p.wins, p.wins >= 5), Achievement("ten_wins", "Unstoppable", "Win 10 matches", "⚡", 10, p.wins, p.wins >= 10), Achievement("twentyfive_games", "Regular", "Play 25 matches", "★", 25, p.games, p.games >= 25), Achievement("first_capture", "First Capture", "Capture an opponent", "◎", 1, p.captures, p.captures >= 1), Achievement("ten_captures", "Hunter", "Capture 10 tokens", "◉", 10, p.captures, p.captures >= 10), Achievement("token_master", "Token Master", "Bring 16 tokens home", "◆", 16, p.tokensHome, p.tokensHome >= 16), Achievement("level_five", "Rising Royalty", "Reach level 5", "♜", 5, p.level, p.level >= 5), Achievement("level_ten", "Royal Legend", "Reach level 10", "♕", 10, p.level, p.level >= 10)
    )
}
class ProgressRepository(context: Context) {
    private val prefs = context.getSharedPreferences("ludo_progress", Context.MODE_PRIVATE)
    fun load() = PlayerProgress(xp = prefs.getInt("xp", 0), coins = prefs.getInt("coins", 2480), games = prefs.getInt("games", 0), wins = prefs.getInt("wins", 0), captures = prefs.getInt("captures", 0), tokensHome = prefs.getInt("home", 0))
    fun save(p: PlayerProgress) { prefs.edit().putInt("xp", p.xp).putInt("coins", p.coins).putInt("games", p.games).putInt("wins", p.wins).putInt("captures", p.captures).putInt("home", p.tokensHome).apply() }
}
class ProgressionViewModel(private val repository: ProgressRepository) : androidx.lifecycle.ViewModel() {
    private val _progress = MutableStateFlow(repository.load()); val progress: StateFlow<PlayerProgress> = _progress.asStateFlow()
    fun grantReward(coins: Int = 0, xp: Int = 0) { val next = _progress.value.copy(coins = _progress.value.coins + coins, xp = _progress.value.xp + xp); repository.save(next); _progress.value = next }
    fun completeGame(won: Boolean, captures: Int = 0, home: Int = 0) { val old = _progress.value; val next = old.copy(xp = old.xp + XpRewards.GAME + (if (won) XpRewards.WIN else 0) + captures * XpRewards.CAPTURE + home * XpRewards.HOME, games = old.games + 1, wins = old.wins + if (won) 1 else 0, captures = old.captures + captures, tokensHome = old.tokensHome + home); repository.save(next); _progress.value = next }
}
