package com.ludoroyale.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ludoroyale.app.bot.*
import com.ludoroyale.app.game.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {
    private val engine = LudoGameEngine()
    private val _state = MutableStateFlow(engine.state)
    val state: StateFlow<GameState> = _state.asStateFlow()
    private var botJob: Job? = null
    var practiceConfig: PracticeConfig? = null
        private set
    var botThinking: Boolean = false
        private set

    fun configurePractice(config: PracticeConfig?) { practiceConfig = config; restart() }
    fun isBotTurn(): Boolean = practiceConfig?.botColors?.contains(_state.value.currentPlayer) == true
    fun roll() {
        if (isBotTurn()) return
        _state.value = engine.roll()
        if (_state.value.legalTokenIds.isEmpty()) viewModelScope.launch { delay(700); engine.passTurn(); _state.value = engine.state }
    }
    fun move(id: Int) { if (!isBotTurn()) { _state.value = engine.move(id) } }
    fun pause() { engine.togglePause(); _state.value = engine.state }
    fun restart() { botJob?.cancel(); botThinking = false; engine.reset(); _state.value = engine.state }
    fun playBotTurnIfNeeded() {
        if (!isBotTurn() || botJob?.isActive == true || _state.value.paused || _state.value.winner != null) return
        val bot = BotPlayer(_state.value.currentPlayer, practiceConfig!!.difficulty)
        botJob = viewModelScope.launch {
            botThinking = true
            delay(650)
            engine.roll(); _state.value = engine.state
            delay(500)
            val token = bot.chooseToken(engine.state)
            if (token != null) { delay(350); engine.move(token); _state.value = engine.state }
            else { delay(500); engine.passTurn(); _state.value = engine.state }
            botThinking = false
        }
    }
}
