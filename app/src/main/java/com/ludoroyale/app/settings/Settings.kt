package com.ludoroyale.app.settings

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class ThemeChoice { SYSTEM, LIGHT, DARK }
data class AppSettings(val sound:Boolean=true,val music:Boolean=true,val vibration:Boolean=true,val notifications:Boolean=true,val theme:ThemeChoice=ThemeChoice.SYSTEM,val language:String="English")
class SettingsRepository(context:Context){private val p=context.getSharedPreferences("app_settings",Context.MODE_PRIVATE);fun load()=AppSettings(p.getBoolean("sound",true),p.getBoolean("music",true),p.getBoolean("vibration",true),p.getBoolean("notifications",true),runCatching{ThemeChoice.valueOf(p.getString("theme",ThemeChoice.SYSTEM.name)!!)}.getOrDefault(ThemeChoice.SYSTEM),p.getString("language","English")!!);fun save(s:AppSettings){p.edit().putBoolean("sound",s.sound).putBoolean("music",s.music).putBoolean("vibration",s.vibration).putBoolean("notifications",s.notifications).putString("theme",s.theme.name).putString("language",s.language).apply()}}
class SettingsViewModel(private val repo:SettingsRepository):androidx.lifecycle.ViewModel(){private val _settings=MutableStateFlow(repo.load());val settings=_settings.asStateFlow();fun update(s:AppSettings){repo.save(s);_settings.value=s}}
