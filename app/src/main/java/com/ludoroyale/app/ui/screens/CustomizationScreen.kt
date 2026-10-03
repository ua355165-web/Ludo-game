package com.ludoroyale.app.ui.screens
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ludoroyale.app.store.*
@Composable fun CustomizationScreen(vm:WalletViewModel,onBack:()->Unit){val wallet by vm.wallet.collectAsState();var category by remember{mutableStateOf(StoreCategory.TOKEN_SKIN)};var preview by remember{mutableStateOf(StoreCatalog.item("token_classic"))};val items=StoreCatalog.items.filter{it.category==category};Column(Modifier.fillMaxSize().padding(20.dp)){TextButton(onClick=onBack){Text("‹  Back")};Text("Customize",fontSize=30.sp,fontWeight=FontWeight.ExtraBold);Text("Your look, your royale.",color=MaterialTheme.colorScheme.onSurfaceVariant);Spacer(Modifier.height(12.dp));Row(horizontalArrangement=Arrangement.spacedBy(6.dp)){StoreCategory.entries.forEach{c->FilterChip(selected=category==c,onClick={category=c},label={Text(c.name.replace('_',' ').lowercase().replaceFirstChar{it.uppercase()},fontSize=11.sp)})}};Spacer(Modifier.height(12.dp));ElevatedCard{Column(Modifier.fillMaxWidth().padding(20.dp),horizontalAlignment=Alignment.CenterHorizontally){Text(preview.preview,fontSize=64.sp,color=MaterialTheme.colorScheme.primary);Text("Preview: ${preview.name}",fontWeight=FontWeight.Bold);Text(if(preview.id in wallet.owned)"Owned" else "Locked · ${preview.price} coins",color=MaterialTheme.colorScheme.onSurfaceVariant)}};Spacer(Modifier.height(12.dp));LazyColumn(verticalArrangement=Arrangement.spacedBy(10.dp)){items(items.size){i->val item=items[i];val owned=item.id in wallet.owned;val equipped=wallet.equipped[item.category]==item.id;ElevatedCard(onClick={preview=item}){Row(Modifier.fillMaxWidth().padding(14.dp),verticalAlignment=Alignment.CenterVertically){Text(item.preview,fontSize=30.sp);Spacer(Modifier.width(12.dp));Column(Modifier.weight(1f)){Text(item.name,fontWeight=FontWeight.Bold);Text(if(owned)if(equipped)"Equipped" else "Owned" else "Locked · ${item.price} coins",fontSize=12.sp)};if(owned)Button(onClick={if(equipped)vm.unequip(item.category)else vm.equip(item.id)}){Text(if(equipped)"Unequip" else "Equip")}}}}}}}
