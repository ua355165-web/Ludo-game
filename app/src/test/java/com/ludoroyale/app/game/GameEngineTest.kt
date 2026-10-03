package com.ludoroyale.app.game

import org.junit.Assert.*
import org.junit.Test

class GameEngineTest {
    @Test fun sixReleasesTokenAndKeepsTurn() { val e = LudoGameEngine { 6 }; e.roll(); assertEquals(setOf(0,1,2,3), e.state.legalTokenIds); e.move(0); assertEquals(0, e.state.players.first().tokens.first().progress); assertEquals(PlayerColor.RED, e.state.currentPlayer) }
    @Test fun nonSixPassesTurn() { val e = LudoGameEngine { 3 }; e.roll(); assertEquals(PlayerColor.RED, e.state.currentPlayer); assertTrue(e.state.legalTokenIds.isEmpty()) }
    @Test fun exactHomeEntryIsRequired() { val token = TokenState(0, PlayerColor.RED, GameRules.finishProgress - 1); assertTrue(MoveValidator.canMove(token, 1)); assertFalse(MoveValidator.canMove(token, 2)) }
    @Test fun winnerNeedsAllFourHome() { val p = PlayerState(PlayerColor.BLUE, (0..3).map { TokenState(it, PlayerColor.BLUE, GameRules.finishProgress) }); assertEquals(PlayerColor.BLUE, WinnerDetector.winner(p)) }
    @Test fun safeCellCannotCapture() { assertTrue(GameRules.isSafe(PlayerColor.RED, 0)); assertNull(BoardPath.trackPosition(PlayerColor.RED, GameRules.finishProgress)) }
}
