package com.ludoroyale.app.data

data class PlayerProfile(val name: String, val level: Int, val coins: Int)
enum class GameMode(val label: String) { LOCAL("Local Multiplayer"), BOT("Play vs Bot"), ONLINE("Online Multiplayer") }
