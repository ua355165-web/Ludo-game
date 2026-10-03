package com.ludoroyale.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ludoroyale.app.game.GameAudioManager
import com.ludoroyale.app.online.FirebaseAccountRepository
import com.ludoroyale.app.settings.*

@Composable
fun SettingsScreen(vm: SettingsViewModel, audio: GameAudioManager, onBack: () -> Unit) {
    val settings by vm.settings.collectAsState()
    var showAbout by remember { mutableStateOf(false) }
    var showPrivacy by remember { mutableStateOf(false) }
    var showDelete by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        TextButton(onClick = onBack) { Text("‹  Back") }
        Text("Settings", fontSize = 30.sp, fontWeight = FontWeight.ExtraBold)
        Text("Make Ludo Royale feel like yours.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        SettingRow("Sound effects", settings.sound) { vm.update(settings.copy(sound = it)); audio.soundEnabled = it }
        SettingRow("Music", settings.music) { vm.update(settings.copy(music = it)); audio.musicEnabled = it }
        SettingRow("Vibration", settings.vibration) { vm.update(settings.copy(vibration = it)); audio.vibrationEnabled = it }
        SettingRow("Notifications", settings.notifications) { vm.update(settings.copy(notifications = it)) }
        Text("Theme", fontWeight = FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) { ThemeChoice.entries.forEach { theme -> FilterChip(settings.theme == theme, { vm.update(settings.copy(theme = theme)) }, label = { Text(theme.name.lowercase().replaceFirstChar { it.uppercase() }) }) } }
        Text("Language", fontWeight = FontWeight.Bold)
        OutlinedButton(onClick = { vm.update(settings.copy(language = "English")) }) { Text(settings.language) }
        Text("Account", fontWeight = FontWeight.Bold)
        ElevatedCard { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) { Text("Guest account", fontWeight = FontWeight.Bold); Text("Your local progress remains available on this device.", fontSize = 12.sp); TextButton(onClick = { FirebaseAccountRepository().logout() }) { Text("Sign out") }; TextButton(onClick = { showDelete = true }) { Text("Delete account", color = MaterialTheme.colorScheme.error) } } }
        Text("About & support", fontWeight = FontWeight.Bold)
        TextButton(onClick = { showAbout = true }) { Text("About Ludo Royale") }
        TextButton(onClick = { showPrivacy = true }) { Text("Privacy, Terms & Help") }
    }
    if (showAbout) InfoDialog("Ludo Royale", "Version 1.0\nDeveloper: Ludo Royale Studio\nSupport: support placeholder") { showAbout = false }
    if (showPrivacy) InfoDialog("Privacy & help", "We store only account, progress, friends, room and notification data needed for the app. Account deletion removes your account data. Terms and Privacy Policy links will be connected before release.") { showPrivacy = false }
    if (showDelete) AlertDialog(onDismissRequest = { showDelete = false }, title = { Text("Delete account?") }, text = { Text("This removes your account data. Local guest progress may remain on this device until app data is cleared.") }, confirmButton = { TextButton(onClick = { FirebaseAccountRepository().logout(); showDelete = false }) { Text("Confirm") } }, dismissButton = { TextButton(onClick = { showDelete = false }) { Text("Cancel") } })
}

@Composable private fun SettingRow(label: String, checked: Boolean, onChange: (Boolean) -> Unit) { Surface(shape = MaterialTheme.shapes.large, tonalElevation = 2.dp) { Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp)) { Text(label, Modifier.weight(1f), fontWeight = FontWeight.SemiBold); Switch(checked, onChange) } } }
@Composable private fun InfoDialog(title: String, body: String, onClose: () -> Unit) { AlertDialog(onDismissRequest = onClose, title = { Text(title) }, text = { Text(body) }, confirmButton = { TextButton(onClick = onClose) { Text("Close") } }) }
