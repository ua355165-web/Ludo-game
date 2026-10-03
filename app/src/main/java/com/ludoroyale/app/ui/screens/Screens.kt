package com.ludoroyale.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ludoroyale.app.data.PlayerProfile
import com.ludoroyale.app.ui.Route
import com.ludoroyale.app.ui.components.*
import com.ludoroyale.app.progression.PlayerProgress

@Composable fun SplashScreen(onDone: () -> Unit) { LaunchedEffect(Unit) { kotlinx.coroutines.delay(900); onDone() }; Box(Modifier.fillMaxSize().background(Brush.linearGradient(listOf(Color(0xFF6C5CE7), Color(0xFFFF6B5F)))), contentAlignment = Alignment.Center) { Column(horizontalAlignment = Alignment.CenterHorizontally) { Text("✦", color = Color.White, fontSize = 54.sp); Text("LUDO ROYALE", color = Color.White, fontWeight = FontWeight.Black, fontSize = 30.sp, letterSpacing = 3.sp); Text("Play bold. Play together.", color = Color.White.copy(.8f)) } } }

@Composable
fun HomeScreen(profile: PlayerProfile, progress: PlayerProgress, wallet: com.ludoroyale.app.store.WalletState, daily: com.ludoroyale.app.rewards.DailyState, onNavigate: (String) -> Unit) {
    var selected by remember { mutableStateOf("Home") }; var showQuickPlay by remember { mutableStateOf(false) }
    val latestAchievement = com.ludoroyale.app.progression.AchievementCatalog.all(progress).firstOrNull { it.unlocked }
    Scaffold(bottomBar = { BottomNavigationBar(selected) { selected = it; onNavigate(when (it) { "Leaderboard" -> Route.Leaderboard.path; "Friends" -> Route.Friends.path; "Profile" -> Route.Profile.path; else -> Route.Home.path }) } }) { pad ->
        LazyColumn(Modifier.fillMaxSize().padding(pad).padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(16.dp), contentPadding = PaddingValues(top = 18.dp, bottom = 26.dp)) {
            item { Row(verticalAlignment = Alignment.CenterVertically) { Column(Modifier.weight(1f)) { Text("LUDO ROYALE", color = Color(0xFF6C5CE7), fontWeight = FontWeight.Black, letterSpacing = 2.sp); Text("Welcome back, ${profile.name.substringBefore(' ')}", fontSize = 25.sp, fontWeight = FontWeight.ExtraBold) }; IconButton(onClick = { onNavigate(Route.Notifications.path) }) { Icon(Icons.Rounded.Notifications, "Open notifications", tint = Color(0xFF6C5CE7)) }; PlayerAvatar() } }
            item { ElevatedCard(shape = RoundedCornerShape(26.dp), colors = CardDefaults.elevatedCardColors(containerColor = Color(0xFF242237)), elevation = CardDefaults.elevatedCardElevation(4.dp)) { Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) { Row(verticalAlignment = Alignment.CenterVertically) { Column(Modifier.weight(1f)) { Text("Level ${progress.level}", color = Color.White, fontWeight = FontWeight.Black, fontSize = 19.sp); Text("${progress.xp} XP  ·  ${progress.nextLevel - progress.xp} to next level", color = Color.White.copy(.72f), fontSize = 12.sp) }; Text("✦ ${wallet.coins}", color = Color(0xFFFFD477), fontWeight = FontWeight.Black, fontSize = 17.sp) }; LinearProgressIndicator({ progress.levelProgress }, Modifier.fillMaxWidth(), color = Color(0xFFFFD477), trackColor = Color.White.copy(.16f)); Text("${daily.streak} day streak  ·  best ${daily.bestStreak}", color = Color(0xFFFFD477), fontWeight = FontWeight.Bold, fontSize = 12.sp) } } }
            item { AppButton("Quick Play", Icons.Rounded.Bolt, Modifier.fillMaxWidth()) { showQuickPlay = true } }
            item { Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) { GameCard("Bot", "Practice", Icons.Rounded.SmartToy, Color(0xFF3BAA88), Modifier.weight(1f)) { onNavigate(Route.BotSetup.path) }; GameCard("Local", "Pass & play", Icons.Rounded.Groups, Color(0xFFFF6B5F), Modifier.weight(1f)) { onNavigate(Route.Game.path) } } }
            item { SectionHeader("Your royale hub") }
            item { Column(verticalArrangement = Arrangement.spacedBy(10.dp)) { GameCard("Online multiplayer", "Private rooms", Icons.Rounded.Public, Color(0xFF6C5CE7)) { onNavigate(Route.Online.path) }; Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) { GameCard("Friends", "See your crew", Icons.Rounded.People, Color(0xFF242237), Modifier.weight(1f)) { onNavigate(Route.Friends.path) }; GameCard("Store", "Spend virtual coins", Icons.Rounded.ShoppingBag, Color(0xFF242237), Modifier.weight(1f)) { onNavigate(Route.Store.path) } }; Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) { GameCard("Customize", "Make it yours", Icons.Rounded.Palette, Color(0xFF242237), Modifier.weight(1f)) { onNavigate(Route.Customize.path) }; GameCard("Daily reward", if (daily.claimedToday) "Claimed today" else "Ready to claim", Icons.Rounded.CardGiftcard, Color(0xFF242237), Modifier.weight(1f)) { onNavigate(Route.DailyReward.path) } }; Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) { GameCard("Missions", "Earn more rewards", Icons.Rounded.Flag, Color(0xFF242237), Modifier.weight(1f)) { onNavigate(Route.Missions.path) }; GameCard("Leaderboard", "Climb the ranks", Icons.Rounded.EmojiEvents, Color(0xFF242237), Modifier.weight(1f)) { onNavigate(Route.Leaderboard.path) } } } }
            item { ElevatedCard(shape = RoundedCornerShape(22.dp)) { Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) { Text(latestAchievement?.icon ?: "✦", fontSize = 28.sp, color = Color(0xFFF4B942)); Spacer(Modifier.width(12.dp)); Column { Text("Latest achievement", color = Color.Gray, fontSize = 12.sp); Text(latestAchievement?.name ?: "Your first badge is waiting", fontWeight = FontWeight.Bold); Text(latestAchievement?.description ?: "Play a match to start your collection", fontSize = 12.sp, color = Color.Gray) } } } }
        }
    }
    if (showQuickPlay) ModalBottomSheet(onDismissRequest = { showQuickPlay = false }) { Column(Modifier.padding(horizontal = 24.dp, vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) { Text("Choose your table", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold); Text("Pick an available mode. Your existing game screens stay the same.", color = Color.Gray); AppButton("Local multiplayer", Icons.Rounded.Groups, Modifier.fillMaxWidth()) { showQuickPlay = false; onNavigate(Route.Game.path) }; AppButton("Practice vs bot", Icons.Rounded.SmartToy, Modifier.fillMaxWidth()) { showQuickPlay = false; onNavigate(Route.BotSetup.path) }; AppButton("Online private room", Icons.Rounded.Public, Modifier.fillMaxWidth()) { showQuickPlay = false; onNavigate(Route.Online.path) }; Spacer(Modifier.height(16.dp)) } }
}

@Composable fun GamePlaceholder(onBack: () -> Unit) { Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) { IconButton(onClick = onBack) { Icon(Icons.Rounded.ArrowBack, "Back") }; Text("Ludo table", fontSize = 30.sp, fontWeight = FontWeight.ExtraBold); Text("Game board foundation coming next", color = Color.Gray); Box(Modifier.fillMaxWidth().aspectRatio(1f).background(Color(0xFFF0EAFE), RoundedCornerShape(28.dp)), contentAlignment = Alignment.Center) { Text("BOARD READY FOR STEP 2", color = Color(0xFF6C5CE7), fontWeight = FontWeight.Black) } }
}
@Composable fun SimpleScreen(title: String, subtitle: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onBack: () -> Unit) { Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) { IconButton(onClick = onBack) { Icon(Icons.Rounded.ArrowBack, "Back") }; Icon(icon, null, tint = Color(0xFF6C5CE7), modifier = Modifier.size(42.dp)); Text(title, fontSize = 30.sp, fontWeight = FontWeight.ExtraBold); Text(subtitle, color = Color.Gray) } }
