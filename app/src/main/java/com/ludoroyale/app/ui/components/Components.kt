package com.ludoroyale.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable fun AppButton(text: String, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier = Modifier, onClick: () -> Unit) { Button(onClick = onClick, modifier = modifier.height(54.dp), shape = RoundedCornerShape(18.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C5CE7))) { Icon(icon, null); Spacer(Modifier.width(10.dp)); Text(text, fontWeight = FontWeight.Bold) } }
@Composable fun CoinBalance(coins: Int) { Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clip(RoundedCornerShape(20.dp)).background(Color(0xFFFFF2D2)).padding(horizontal = 12.dp, vertical = 8.dp)) { Text("✦", color = Color(0xFFF4B942), fontSize = 18.sp); Spacer(Modifier.width(5.dp)); Text("$coins", color = Color(0xFF8A5B00), fontWeight = FontWeight.Bold) } }
@Composable fun PlayerAvatar(modifier: Modifier = Modifier) { Box(modifier.size(54.dp).clip(CircleShape).background(Brush.linearGradient(listOf(Color(0xFFFFB26B), Color(0xFFFF6B5F)))), contentAlignment = Alignment.Center) { Text("AM", color = Color.White, fontWeight = FontWeight.ExtraBold) } }
@Composable fun GameCard(title: String, subtitle: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color, modifier: Modifier = Modifier, onClick: () -> Unit) { Card(modifier = modifier.fillMaxWidth().clickable(onClick = onClick), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = color)) { Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) { Icon(icon, null, tint = Color.White, modifier = Modifier.size(30.dp)); Spacer(Modifier.width(14.dp)); Column { Text(title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 17.sp); Text(subtitle, color = Color.White.copy(.78f), fontSize = 13.sp) }; Spacer(Modifier.weight(1f)); Icon(Icons.Rounded.ChevronRight, null, tint = Color.White) } } }
@Composable fun SectionHeader(title: String, action: String? = null, onAction: () -> Unit = {}) { Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) { Text(title, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp); Spacer(Modifier.weight(1f)); action?.let { Text(it, color = Color(0xFF6C5CE7), fontWeight = FontWeight.Bold, modifier = Modifier.clickable(onClick = onAction)) } } }
@Composable fun BottomNavigationBar(selected: String, onSelect: (String) -> Unit) { NavigationBar(containerColor = MaterialTheme.colorScheme.surface) { listOf("Home" to Icons.Rounded.Home, "Leaderboard" to Icons.Rounded.EmojiEvents, "Friends" to Icons.Rounded.People, "Profile" to Icons.Rounded.Person).forEach { (label, icon) -> NavigationBarItem(selected = selected == label, onClick = { onSelect(label) }, icon = { Icon(icon, label) }, label = { Text(label) }) } } }
