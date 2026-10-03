package com.ludoroyale.app.ui.screens
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ludoroyale.app.progression.*

@Composable fun ProfileScreen(p: PlayerProgress, onBack: () -> Unit) { Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) { TextButton(onClick = onBack) { Text("‹  Back") }; Text(p.username, fontSize = 30.sp, fontWeight = FontWeight.ExtraBold); Text(p.playerId, color = MaterialTheme.colorScheme.onSurfaceVariant); Text("Level ${p.level}  ·  ${p.xp} XP", fontWeight = FontWeight.Bold); LinearProgressIndicator({ p.levelProgress }, Modifier.fillMaxWidth()); Text("${p.nextLevel - p.xp} XP to level ${p.level + 1}", color = MaterialTheme.colorScheme.onSurfaceVariant); StatLine("Games", p.games.toString()); StatLine("Wins / losses", "${p.wins} / ${p.losses}"); StatLine("Win percentage", "${p.winPercent}%"); StatLine("Tokens home", p.tokensHome.toString()); StatLine("Tokens captured", p.captures.toString()); StatLine("Favorite mode", p.favoriteMode); Text("Virtual coins  ${p.coins}", fontWeight = FontWeight.Bold) } }
@Composable fun AchievementsScreen(p: PlayerProgress, onBack: () -> Unit) { val list = AchievementCatalog.all(p); LazyColumn(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(10.dp), contentPadding = PaddingValues(bottom = 24.dp)) { item { TextButton(onClick = onBack) { Text("‹  Back") }; Text("Achievements", fontSize = 30.sp, fontWeight = FontWeight.ExtraBold); Text("${list.count { it.unlocked }}/${list.size} unlocked", color = MaterialTheme.colorScheme.onSurfaceVariant) }; items(list.size) { i -> val a = list[i]; ElevatedCard { Column(Modifier.fillMaxWidth().padding(16.dp)) { Text("${a.icon}  ${a.name}", fontWeight = FontWeight.Bold); Text(a.description, fontSize = 13.sp); Text("${a.progress.coerceAtMost(a.target)}/${a.target}${if (a.unlocked) "  • Unlocked" else ""}", color = MaterialTheme.colorScheme.primary) } } } } }
@Composable private fun StatLine(label: String, value: String) { Row(Modifier.fillMaxWidth()) { Text(label, Modifier.weight(1f), color = MaterialTheme.colorScheme.onSurfaceVariant); Text(value, fontWeight = FontWeight.Bold) } }
