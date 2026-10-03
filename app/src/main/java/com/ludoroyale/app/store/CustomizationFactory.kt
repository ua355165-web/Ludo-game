package com.ludoroyale.app.store
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
class CustomizationFactory(private val context:Context):ViewModelProvider.Factory{override fun <T:ViewModel>create(c:Class<T>):T=CustomizationViewModel(WalletViewModel(WalletRepository(context))) as T}
