package com.ludoroyale.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ludoroyale.app.game.GameAudioManager

@Composable
fun GameSettingsScreen(audio: GameAudioManager, onBack: () -> Unit) {
    val sound = androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(audio.soundEnabled) }
    val music = androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(audio.musicEnabled) }
    val vibration = androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(audio.vibrationEnabled) }
    Column(
        Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        TextButton(onClick = onBack) { Text("‹  Back") }
        Text("Game settings", fontSize = 30.sp, fontWeight = FontWeight.ExtraBold)
        Text("Make the table feel yours.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        SettingRow("Sound effects", sound.value) { sound.value = it; audio.soundEnabled = it }
        SettingRow("Music", music.value) { music.value = it; audio.musicEnabled = it }
        SettingRow("Vibration", vibration.value) { vibration.value = it; audio.vibrationEnabled = it }
    }
}

@Composable
private fun SettingRow(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Surface(shape = MaterialTheme.shapes.large, tonalElevation = 2.dp) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 14.dp)) {
            Text(label, Modifier.weight(1f), fontWeight = FontWeight.SemiBold)
            Switch(checked = checked, onCheckedChange = onCheckedChange)
        }
    }
}
