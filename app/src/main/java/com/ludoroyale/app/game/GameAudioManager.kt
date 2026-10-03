package com.ludoroyale.app.game

import android.content.Context
import android.media.AudioManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator

class GameAudioManager(private val context: Context) {
    var soundEnabled: Boolean = true
    var musicEnabled: Boolean = true
    var vibrationEnabled: Boolean = true

    fun diceRoll() = feedback(18)
    fun tokenMove() = feedback(8)
    fun capture() = feedback(35)
    fun tokenHome() = feedback(45)
    fun win() = feedback(80)
    fun buttonClick() = feedback(6)

    private fun feedback(duration: Long) {
        if (!vibrationEnabled) return
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator ?: return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(duration)
        }
    }
}
