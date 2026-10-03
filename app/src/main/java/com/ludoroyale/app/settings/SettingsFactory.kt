package com.ludoroyale.app.settings
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
class SettingsFactory(private val context:Context):ViewModelProvider.Factory{override fun <T:ViewModel>create(c:Class<T>):T=SettingsViewModel(SettingsRepository(context)) as T}
