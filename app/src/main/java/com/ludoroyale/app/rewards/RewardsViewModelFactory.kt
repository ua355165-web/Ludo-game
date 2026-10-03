package com.ludoroyale.app.rewards
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
class RewardsViewModelFactory(private val context:Context):ViewModelProvider.Factory{override fun <T:ViewModel>create(c:Class<T>):T=RewardsViewModel(DailyRewardManager(DailyRewardRepository(context)),MissionRepository(context)) as T}
