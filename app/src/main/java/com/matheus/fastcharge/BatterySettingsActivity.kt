package com.matheus.fastcharge

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle

/**
 * Aberta pelo toque longo no tile (QS_TILE_PREFERENCES).
 * Redireciona para as configurações de bateria; se não houver, abre a tela do app.
 */
class BatterySettingsActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            startActivity(Intent(Intent.ACTION_POWER_USAGE_SUMMARY))
        } catch (e: ActivityNotFoundException) {
            startActivity(Intent(this, MainActivity::class.java))
        }
        finish()
    }
}
