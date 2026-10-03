package com.ludoroyale.app.milestones
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
class MilestoneFactory(private val context:Context):ViewModelProvider.Factory{override fun <T:ViewModel>create(c:Class<T>):T=MilestoneViewModel(MilestoneRepository(context)) as T}
