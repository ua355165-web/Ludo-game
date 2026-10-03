package com.ludoroyale.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ludoroyale.app.rewards.*
import com.ludoroyale.app.progression.ProgressionViewModel

@Composable
fun DailyRewardScreen(vm: RewardsViewModel, progression: ProgressionViewModel, onBack: () -> Unit) {
    val state by vm.dailyState.collectAsState()
    Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        TextButton(onClick = onBack) { Text("‹  Back") }
        Text("Daily reward", fontSize = 30.sp, fontWeight = FontWeight.ExtraBold)
        Text("${state.streak} day streak  ·  best ${state.bestStreak}", color = MaterialTheme.colorScheme.primary)
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            DailyRewards.cycle.forEach { reward ->
                Surface(shape = MaterialTheme.shapes.medium, color = if (reward.day == state.day) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant) {
                    Text("Day ${reward.day}\n${reward.label}", Modifier.padding(8.dp), fontSize = 11.sp)
                }
            }
        }
        Text("Today: ${DailyRewards.today(state.day).label}", fontWeight = FontWeight.Bold)
        Text("Next: ${DailyRewards.today((state.day % 7) + 1).label}", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Button(enabled = !state.claimedToday, onClick = { val reward = DailyRewards.today(state.day); vm.claimDaily(); progression.grantReward(reward.coins, reward.xp) }, modifier = Modifier.fillMaxWidth()) { Text(if (state.claimedToday) "Claimed today" else "Claim reward") }
    }
}

@Composable
fun MissionsScreen(vm: RewardsViewModel, progression: ProgressionViewModel, onBack: () -> Unit) {
    val missions by vm.missions.collectAsState()
    LazyColumn(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item {
            TextButton(onClick = onBack) { Text("‹  Back") }
            Text("Missions", fontSize = 30.sp, fontWeight = FontWeight.ExtraBold)
            Text("Complete missions for virtual rewards.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        items(missions.size) { index ->
            val mission = missions[index]
            ElevatedCard {
                Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(mission.title, fontWeight = FontWeight.Bold)
                    Text(mission.description, fontSize = 13.sp)
                    LinearProgressIndicator({ (mission.progress.toFloat() / mission.target).coerceIn(0f, 1f) }, Modifier.fillMaxWidth())
                    Text("${mission.progress.coerceAtMost(mission.target)}/${mission.target}  ·  ${mission.rewardCoins} coins + ${mission.rewardXp} XP", fontSize = 12.sp)
                    if (mission.completed && !mission.claimed) TextButton(onClick = { vm.claimMission(mission.id)?.let { progression.grantReward(it.rewardCoins, it.rewardXp) } }) { Text("Claim reward") }
                    if (mission.claimed) Text("Reward claimed", color = MaterialTheme.colorScheme.primary, fontSize = 12.sp)
                }
            }
        }
    }
}
