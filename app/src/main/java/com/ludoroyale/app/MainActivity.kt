package com.ludoroyale.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import com.ludoroyale.app.ui.LudoApp
import com.ludoroyale.app.ui.theme.LudoRoyaleTheme
import com.ludoroyale.app.viewmodel.AppViewModel
import com.ludoroyale.app.settings.SettingsFactory
import com.ludoroyale.app.settings.SettingsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settings: SettingsViewModel = viewModel(factory = SettingsFactory(this))
            val choice = settings.settings.collectAsState().value.theme
            LudoRoyaleTheme(choice) { LudoApp(viewModel<AppViewModel>()) }
        }
    }
}
