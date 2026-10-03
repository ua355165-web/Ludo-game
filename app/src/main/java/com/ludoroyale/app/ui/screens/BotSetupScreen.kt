package com.ludoroyale.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ludoroyale.app.bot.BotDifficulty
import com.ludoroyale.app.bot.PracticeConfig

@Composable
fun BotSetupScreen(onStart: (PracticeConfig) -> Unit, onBack: () -> Unit) {
    var bots by remember { mutableIntStateOf(3) }
    var difficulty by remember { mutableStateOf(BotDifficulty.NORMAL) }
    Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        TextButton(onClick = onBack) { Text("‹  Back") }
        Text("Practice mode", fontSize = 30.sp, fontWeight = FontWeight.ExtraBold)
        Text("Sharpen your game against the house bots.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text("Opponents", fontWeight = FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) { (1..3).forEach { count -> FilterChip(selected = bots == count, onClick = { bots = count }, label = { Text("$count bot${if (count == 1) "" else "s"}") }) } }
        Text("Difficulty", fontWeight = FontWeight.Bold)
        BotDifficulty.entries.forEach { level -> ElevatedCard(onClick = { difficulty = level }, colors = CardDefaults.elevatedCardColors(containerColor = if (difficulty == level) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface)) { Column(Modifier.fillMaxWidth().padding(16.dp)) { Text(level.label, fontWeight = FontWeight.Bold); Text(level.description, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp) } } }
        Spacer(Modifier.weight(1f))
        Button(onClick = { onStart(PracticeConfig(bots, difficulty)) }, modifier = Modifier.fillMaxWidth().height(54.dp), shape = MaterialTheme.shapes.large) { Text("Start practice match") }
    }
}
