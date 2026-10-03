package com.ludoroyale.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ludoroyale.app.milestones.*
import com.ludoroyale.app.progression.PlayerProgress

@Composable
fun MilestoneScreen(progress: PlayerProgress, vm: MilestoneViewModel, onBack: () -> Unit) {
    val items by vm.items.collectAsState()
    var category by remember { mutableStateOf<MilestoneCategory?>(null) }
    var detail by remember { mutableStateOf<Milestone?>(null) }
    LaunchedEffect(progress) { vm.refresh(progress) }
    val filtered = items.filter { category == null || it.category == category }
    Column(Modifier.fillMaxSize().padding(20.dp)) {
        TextButton(onClick = onBack) { Text("‹  Back") }
        Text("Achievement center", fontSize = 30.sp, fontWeight = FontWeight.ExtraBold)
        Text("${items.count { it.completed }}/${items.size} milestones complete", color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            FilterChip(category == null, { category = null }, label = { Text("All") })
            MilestoneCategory.entries.take(4).forEach { c -> FilterChip(category == c, { category = c }, label = { Text(c.name.lowercase().replaceFirstChar { it.uppercase() }, fontSize = 11.sp) }) }
        }
        Spacer(Modifier.height(10.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(9.dp)) {
            items(filtered.size) { i ->
                val m = filtered[i]
                ElevatedCard(onClick = { detail = m }) {
                    Column(Modifier.fillMaxWidth().padding(15.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                        Row {
                            Text("${m.icon}  ${m.name}", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                            Text(if (m.completed) "✓" else "Locked", color = if (m.completed) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Text(m.description, fontSize = 12.sp)
                        LinearProgressIndicator({ (m.progress.toFloat() / m.target).coerceIn(0f, 1f) }, Modifier.fillMaxWidth())
                        Text("${m.progress.coerceAtMost(m.target)}/${m.target}  ·  ${m.rewardCoins} coins + ${m.rewardXp} XP", fontSize = 11.sp)
                        if (m.completed && !m.claimed) TextButton(onClick = { vm.claim(m) }) { Text("Claim reward") }
                        if (m.completed) TextButton(onClick = { vm.selectBadge(m) }) { Text(if (vm.selectedBadge.value == m.id) "Profile badge selected" else "Set profile badge") }
                    }
                }
            }
        }
    }
    detail?.let { m ->
        AlertDialog(onDismissRequest = { detail = null }, title = { Text("${m.icon} ${m.name}") }, text = { Text("${m.description}\n\nProgress: ${m.progress}/${m.target}\nReward: ${m.rewardCoins} virtual coins + ${m.rewardXp} XP\nStatus: ${if (m.completed) "Completed" else "In progress"}") }, confirmButton = { TextButton(onClick = { detail = null }) { Text("Close") } })
    }
}
