package com.ludoroyale.app.progression
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
class ProgressionViewModelFactory(private val context: Context) : ViewModelProvider.Factory { override fun <T : ViewModel> create(modelClass: Class<T>): T = ProgressionViewModel(ProgressRepository(context)) as T }
