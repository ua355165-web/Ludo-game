package com.ludoroyale.app.ui.screens
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ludoroyale.app.progression.PlayerProgress
import com.ludoroyale.app.store.WalletViewModel
@Composable fun ProfileSettingsScreen(progress:PlayerProgress,wallet:WalletViewModel,onBack:()->Unit){var name by remember{mutableStateOf(progress.username)};Column(Modifier.fillMaxSize().padding(20.dp),verticalArrangement=Arrangement.spacedBy(14.dp)){TextButton(onClick=onBack){Text("‹  Back")};Text("Profile settings",style=MaterialTheme.typography.headlineMedium);OutlinedTextField(name,{name=it},label={Text("Username")},singleLine=true,modifier=Modifier.fillMaxWidth());Text("Avatar and profile frame",style=MaterialTheme.typography.titleMedium);Text("Choose these from Customize to keep one source of truth.",color=MaterialTheme.colorScheme.onSurfaceVariant);Button(onClick=onBack,modifier=Modifier.fillMaxWidth()){Text("Save profile")}}}
