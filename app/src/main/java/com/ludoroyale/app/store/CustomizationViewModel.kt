package com.ludoroyale.app.store
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
class CustomizationViewModel(private val wallet:WalletViewModel,private val sync:CosmeticSyncRepository?=null):ViewModel(){val state get()=wallet.wallet;fun equip(id:String)=wallet.equip(id);fun unequip(c:StoreCategory)=wallet.unequip(c);fun sync(playerId:String){sync?.let{r->viewModelScope.launch{r.save(CosmeticProfile(playerId,state.value.owned,state.value.equipped))}}}}
