package com.matheus.fastcharge

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.provider.Settings
import android.util.Log

/**
 * Lê e grava a chave Samsung `adaptive_fast_charging` em Settings.System
 * (1 = carregamento rápido ligado, 0 = desligado).
 */
class FastChargingController(context: Context) {

    private val appContext = context.applicationContext
    private val resolver = appContext.contentResolver

    fun hasPermission(): Boolean =
        appContext.checkSelfPermission(Manifest.permission.WRITE_SECURE_SETTINGS) ==
            PackageManager.PERMISSION_GRANTED

    fun isEnabled(): Boolean = Settings.System.getInt(resolver, KEY, 1) == 1

    /** Grava o valor. Retorna true se a escrita funcionou. */
    fun setEnabled(enabled: Boolean): Boolean = try {
        Settings.System.putInt(resolver, KEY, if (enabled) 1 else 0)
    } catch (e: SecurityException) {
        Log.w(TAG, "Sem permissão para gravar $KEY", e)
        false
    } catch (e: IllegalArgumentException) {
        Log.w(TAG, "Sistema recusou gravar $KEY", e)
        false
    }

    /** Inverte o estado. Retorna true se a escrita funcionou. */
    fun toggle(): Boolean = setEnabled(!isEnabled())

    companion object {
        const val KEY = "adaptive_fast_charging"
        const val GRANT_COMMAND =
            "adb shell pm grant com.matheus.fastcharge android.permission.WRITE_SECURE_SETTINGS"
        private const val TAG = "FastCharge"
    }
}
