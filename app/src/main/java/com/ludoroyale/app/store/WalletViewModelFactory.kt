package com.ludoroyale.app.store
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
class WalletViewModelFactory(private val context:Context):ViewModelProvider.Factory{override fun <T:ViewModel>create(c:Class<T>):T=WalletViewModel(WalletRepository(context)) as T}
