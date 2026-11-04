package com.matheus.fastcharge

import android.content.Intent
import android.os.Build
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.widget.Toast

class FastChargingTileService : TileService() {

    private val controller by lazy { FastChargingController(this) }

    override fun onStartListening() {
        super.onStartListening()
        refreshTile()
    }

    override fun onClick() {
        super.onClick()
        if (!controller.hasPermission()) {
            val intent = Intent(this, MainActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            @Suppress("DEPRECATION")
            startActivityAndCollapse(intent)
            return
        }
        if (!controller.toggle()) {
            Toast.makeText(this, R.string.toggle_failed, Toast.LENGTH_SHORT).show()
        }
        refreshTile()
    }

    private fun refreshTile() {
        val tile = qsTile ?: return
        val hasPermission = controller.hasPermission()
        val enabled = controller.isEnabled()

        tile.label = getString(R.string.tile_label)
        tile.state = when {
            !hasPermission -> Tile.STATE_UNAVAILABLE
            enabled -> Tile.STATE_ACTIVE
            else -> Tile.STATE_INACTIVE
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            tile.subtitle = getString(
                when {
                    !hasPermission -> R.string.state_no_permission
                    enabled -> R.string.state_on
                    else -> R.string.state_off
                }
            )
        }
        tile.updateTile()
    }
}
