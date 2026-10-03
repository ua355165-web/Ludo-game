package com.ludoroyale.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ludoroyale.app.store.*

@Composable
fun StoreScreen(walletVm: WalletViewModel, onBack: () -> Unit) {
    val wallet by walletVm.wallet.collectAsState()
    var category by remember { mutableStateOf(StoreCategory.TOKEN_SKIN) }
    var pending by remember { mutableStateOf<StoreItem?>(null) }
    var message by remember { mutableStateOf<String?>(null) }
    val visible = StoreCatalog.items.filter { it.category == category }
    Column(Modifier.fillMaxSize().padding(horizontal = 20.dp, vertical = 12.dp)) {
        Row { TextButton(onClick = onBack) { Text("‹  Back") }; Spacer(Modifier.weight(1f)); TextButton(onClick = { /* customization is reached from Profile */ }) { Text("Preview") } }
        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) { Text("Royale store", fontSize = 30.sp, fontWeight = FontWeight.ExtraBold); Text("Cosmetics only. Coins stay virtual.", color = MaterialTheme.colorScheme.onSurfaceVariant) }
            Text("✦ ${wallet.coins}", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Black)
        }
        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) { StoreCategory.entries.forEach { c -> FilterChip(selected = category == c, onClick = { category = c }, label = { Text(c.name.replace('_',' ').lowercase().replaceFirstChar { it.uppercase() }, fontSize = 11.sp) }) } }
        Spacer(Modifier.height(10.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp), contentPadding = PaddingValues(bottom = 24.dp)) {
            items(visible.size) { index ->
                val item = visible[index]
                val owned = item.id in wallet.owned
                val equipped = wallet.equipped[item.category] == item.id
                ElevatedCard {
                    Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                        Text(item.preview, fontSize = 34.sp, color = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.width(14.dp))
                        Column(Modifier.weight(1f)) { Text(item.name, fontWeight = FontWeight.Bold); Text(item.description, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant); Text(if (item.price == 0) "Included" else "${item.price} coins", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                        when { equipped -> OutlinedButton(onClick = { walletVm.unequip(item.category) }) { Text("Unequip") }; owned -> Button(onClick = { walletVm.equip(item.id); message = "${item.name} equipped" }) { Text("Equip") }; else -> Button(onClick = { pending = item }) { Text("Buy") } }
                    }
                }
            }
        }
        message?.let { Text(it, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(vertical = 6.dp)) }
    }
    pending?.let { item -> AlertDialog(onDismissRequest = { pending = null }, title = { Text("Buy ${item.name}?") }, text = { Text("Spend ${item.price} virtual coins? Your balance will never go negative.") }, confirmButton = { TextButton(onClick = { val ok = walletVm.purchase(item.id); message = if (ok) "${item.name} added to inventory" else "Not enough virtual coins"; pending = null }) { Text("Confirm") } }, dismissButton = { TextButton(onClick = { pending = null }) { Text("Cancel") } }) }
}
