package com.ludoroyale.app.progression

import org.junit.Assert.*
import org.junit.Test

class ProgressionTest {
    @Test fun levelsAdvanceEvery250Xp() { assertEquals(1, LevelSystem.levelFor(0)); assertEquals(2, LevelSystem.levelFor(250)); assertEquals(5, LevelSystem.levelFor(1000)) }
    @Test fun progressCalculatesWinsAndLosses() { val p = PlayerProgress(xp = 300, games = 4, wins = 3); assertEquals(1, p.level); assertEquals(3, p.losses); assertEquals(75, p.winPercent) }
    @Test fun achievementsUnlockFromStats() { val p = PlayerProgress(games = 1, wins = 5, captures = 10); val a = AchievementCatalog.all(p); assertTrue(a.first { it.id == "first_game" }.unlocked); assertTrue(a.first { it.id == "five_wins" }.unlocked); assertTrue(a.first { it.id == "ten_captures" }.unlocked) }
    @Test fun lockedAchievementReportsProgress() { val p = PlayerProgress(games = 2); val a = AchievementCatalog.all(p).first { it.id == "twentyfive_games" }; assertFalse(a.unlocked); assertEquals(2, a.progress) }
}
