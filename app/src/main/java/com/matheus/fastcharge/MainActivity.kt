package com.matheus.fastcharge

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {

    private val controller by lazy { FastChargingController(this) }

    private lateinit var permissionText: TextView
    private lateinit var stateText: TextView
    private lateinit var commandText: TextView
    private lateinit var toggleButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        permissionText = findViewById(R.id.permission_text)
        stateText = findViewById(R.id.state_text)
        commandText = findViewById(R.id.command_text)
        toggleButton = findViewById(R.id.toggle_button)

        commandText.text = FastChargingController.GRANT_COMMAND
        toggleButton.setOnClickListener {
            if (!controller.toggle()) {
                Toast.makeText(this, R.string.toggle_failed, Toast.LENGTH_SHORT).show()
            }
            refresh()
        }
    }

    override fun onResume() {
        super.onResume()
        refresh()
    }

    private fun refresh() {
        val hasPermission = controller.hasPermission()
        permissionText.setText(
            if (hasPermission) R.string.permission_granted else R.string.permission_missing
        )
        stateText.text = getString(
            R.string.current_state,
            getString(if (controller.isEnabled()) R.string.state_on else R.string.state_off)
        )
        commandText.visibility = if (hasPermission) TextView.GONE else TextView.VISIBLE
        toggleButton.isEnabled = hasPermission
    }
}
