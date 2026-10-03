package com.ludoroyale.app.viewmodel

import androidx.lifecycle.ViewModel
import com.ludoroyale.app.data.PlayerProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppViewModel : ViewModel() {
    private val _profile = MutableStateFlow(PlayerProfile("Alex Morgan", 12, 2480))
    val profile: StateFlow<PlayerProfile> = _profile.asStateFlow()
}
