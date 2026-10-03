package com.ludoroyale.app.settings

import org.junit.Assert.*
import org.junit.Test

class SettingsTest {
    @Test fun themeChoicesAreComplete() { assertEquals(setOf(ThemeChoice.SYSTEM, ThemeChoice.LIGHT, ThemeChoice.DARK), ThemeChoice.entries.toSet()) }
    @Test fun defaultsKeepNotificationsEnabled() { assertTrue(AppSettings().notifications) }
    @Test fun soundCanBeDisabledWithoutChangingOtherPreferences() { val updated = AppSettings().copy(sound = false); assertFalse(updated.sound); assertTrue(updated.music); assertTrue(updated.vibration) }
}
