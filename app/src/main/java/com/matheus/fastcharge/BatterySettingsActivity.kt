package com.matheus.fastcharge

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.util.Log

/**
 * Aberta pelo toque longo no tile (QS_TILE_PREFERENCES).
 * Tenta, em ordem: Configurações de carregamento (Samsung), Bateria e, por fim, a tela do app.
 */
class BatterySettingsActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val candidates = listOf(
            // Samsung Device care → "Configurações de carregamento" (One UI)
            Intent(ACTION_SAMSUNG_CHARGING_SETTINGS),
            Intent(Intent.ACTION_POWER_USAGE_SUMMARY),
            Intent(this, MainActivity::class.java),
        )
        for (intent in candidates) {
            try {
                startActivity(intent)
                break
            } catch (e: ActivityNotFoundException) {
                Log.d(TAG, "Indisponível: ${intent.action ?: intent.component}")
            } catch (e: SecurityException) {
                Log.d(TAG, "Bloqueado: ${intent.action ?: intent.component}")
            }
        }
        finish()
    }

    private companion object {
        const val ACTION_SAMSUNG_CHARGING_SETTINGS =
            "com.samsung.android.sm.ACTION_BATTERY_ADVANCED_MENU"
        const val TAG = "FastCharge"
    }
}
