package com.ludoroyale.app.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.ui.platform.LocalContext
import com.ludoroyale.app.game.GameAudioManager
import com.ludoroyale.app.viewmodel.GameViewModel
import com.ludoroyale.app.progression.*
import com.ludoroyale.app.rewards.*
import com.ludoroyale.app.online.OnlineViewModel
import com.ludoroyale.app.social.SocialViewModel
import com.ludoroyale.app.store.*
import com.ludoroyale.app.milestones.*
import com.ludoroyale.app.settings.*
import com.ludoroyale.app.progression.*
import com.ludoroyale.app.rewards.*
import com.ludoroyale.app.online.OnlineViewModel
import com.ludoroyale.app.social.SocialViewModel
import com.ludoroyale.app.store.*
import com.ludoroyale.app.milestones.*
import com.ludoroyale.app.settings.*
import com.ludoroyale.app.ui.screens.*
import com.ludoroyale.app.viewmodel.AppViewModel

sealed class Route(val path: String) { data object Splash: Route("splash"); data object Home: Route("home"); data object BotSetup: Route("bot_setup"); data object Missions: Route("missions"); data object Store: Route("store"); data object Customize: Route("customize"); data object Online: Route("online"); data object Leaderboard: Route("leaderboard"); data object Friends: Route("friends"); data object Notifications: Route("notifications"); data object Profile: Route("profile"); data object Settings: Route("settings"); data object Achievements: Route("achievements"); data object DailyReward: Route("daily_reward"); data object Game: Route("game") }

@Composable fun LudoApp(vm: AppViewModel) { val nav = rememberNavController(); val audio = remember { GameAudioManager(LocalContext.current) }; val settings: SettingsViewModel = androidx.lifecycle.viewmodel.compose.viewModel(factory = SettingsFactory(LocalContext.current)); val gameVm: GameViewModel = androidx.lifecycle.viewmodel.compose.viewModel(); val progression: ProgressionViewModel = androidx.lifecycle.viewmodel.compose.viewModel(factory = ProgressionViewModelFactory(LocalContext.current)); val rewards: RewardsViewModel = androidx.lifecycle.viewmodel.compose.viewModel(factory = RewardsViewModelFactory(LocalContext.current)); val online: OnlineViewModel = androidx.lifecycle.viewmodel.compose.viewModel(); val social: SocialViewModel = androidx.lifecycle.viewmodel.compose.viewModel(); val wallet: WalletViewModel = androidx.lifecycle.viewmodel.compose.viewModel(factory = WalletViewModelFactory(LocalContext.current)); LaunchedEffect(progression.progress.collectAsState().value) { val p = progression.progress.value; rewards.sync(p.xp, p.games, p.wins, p.captures, p.tokensHome) }; Surface(Modifier.fillMaxSize()) { NavHost(nav, startDestination = Route.Splash.path) { composable(Route.Splash.path) { SplashScreen { nav.navigate(Route.Home.path) { popUpTo(Route.Splash.path) { inclusive = true } } } }; composable(Route.Home.path) { HomeScreen(vm.profile.collectAsState().value, progression.progress.collectAsState().value, wallet.wallet.collectAsState().value, rewards.dailyState.collectAsState().value, onNavigate = { nav.navigate(it) }) }; composable("online_match/{code}") { backStack -> val code = backStack.arguments?.getString("code").orEmpty(); val room = (online.state.value as? com.ludoroyale.app.online.OnlineUiState.Ready)?.room; if (room != null) OnlineMatchScreen(room, online.player.playerId, social) { nav.popBackStack() } else SimpleScreen("Room unavailable", "The room is no longer available.", Icons.Rounded.ErrorOutline) { nav.popBackStack() } }; composable(Route.Online.path) { OnlineHubScreen(online, onOpenMatch = { room -> nav.navigate("online_match/${room.code}") }, onBack = { nav.popBackStack() }) }; composable(Route.Store.path) { StoreScreen(wallet) { nav.popBackStack() } }; composable(Route.Customize.path) { CustomizationScreen(wallet) { nav.popBackStack() } }; composable(Route.BotSetup.path) { BotSetupScreen(onStart = { config -> gameVm.configurePractice(config); nav.navigate(Route.Game.path) }, onBack = { nav.popBackStack() }) }; composable(Route.Game.path) { LudoGameScreen(onBack = { nav.popBackStack() }, audio = audio, vm = gameVm, progression = progression) }; composable(Route.Leaderboard.path) { SimpleScreen("Leaderboard", "Your next victory starts here.", Icons.Rounded.EmojiEvents) { nav.popBackStack() } }; composable(Route.Friends.path) { FriendsSocialScreen(social) { nav.popBackStack() } }; composable(Route.Notifications.path) { NotificationsScreen(social) { nav.popBackStack() } }; composable(Route.Profile.path) { Column { ProfileScreen(progression.progress.collectAsState().value) { nav.popBackStack() }; TextButton(onClick={nav.navigate(Route.Customize.path)}) { Text("Customize profile") }; TextButton(onClick={nav.navigate("profile_settings")}) { Text("Edit profile") } } }; composable(Route.Settings.path) { SettingsScreen(settings, audio) { nav.popBackStack() } }; composable("profile_settings") { ProfileSettingsScreen(progression.progress.collectAsState().value, wallet) { nav.popBackStack() } }; composable(Route.Achievements.path) { val milestones: MilestoneViewModel = androidx.lifecycle.viewmodel.compose.viewModel(factory = MilestoneFactory(LocalContext.current)); MilestoneScreen(progression.progress.collectAsState().value, milestones) { nav.popBackStack() } }; composable(Route.DailyReward.path) { DailyRewardScreen(rewards, progression) { nav.popBackStack() } }; composable(Route.Missions.path) { MissionsScreen(rewards, progression) { nav.popBackStack() } } } } }
