package com.ludoroyale.app.bot

import com.ludoroyale.app.game.*
import org.junit.Assert.*
import org.junit.Test

class BotPlayerTest {
    @Test fun botOnlyChoosesLegalTokens() {
        val state = GameState(dice = DiceState(6), legalTokenIds = setOf(1, 3))
        val choice = BotPlayer(PlayerColor.RED, BotDifficulty.NORMAL).chooseToken(state)
        assertTrue(choice == 1 || choice == 3)
    }
    @Test fun practiceConfigSupportsOneTwoAndThreeBots() {
        assertEquals(1, PracticeConfig(1).botColors.size)
        assertEquals(2, PracticeConfig(2).botColors.size)
        assertEquals(3, PracticeConfig(3).botColors.size)
    }
    @Test fun easyStrategyNeverMovesHomeToken() {
        val tokens = listOf(TokenState(0, PlayerColor.GREEN, GameRules.finishProgress), TokenState(1, PlayerColor.GREEN, -1))
        val state = GameState(players = listOf(PlayerState(PlayerColor.GREEN, tokens)), currentPlayer = PlayerColor.GREEN, dice = DiceState(6), legalTokenIds = setOf(1))
        assertEquals(1, BotPlayer(PlayerColor.GREEN, BotDifficulty.EASY).chooseToken(state))
    }
}
