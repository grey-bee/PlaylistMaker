package com.practicum.playlistmaker

import android.content.Intent
import android.os.Bundle
import  com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.appbar.MaterialToolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        val darkThemeSwitch = findViewById<SwitchMaterial>(R.id.themeSwitcher)
        val currentNightMode = AppCompatDelegate.getDefaultNightMode()
        darkThemeSwitch.isChecked = (currentNightMode == AppCompatDelegate.MODE_NIGHT_YES)

        darkThemeSwitch.setOnCheckedChangeListener { _, checked ->
            if (checked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        toolbar.setNavigationOnClickListener {
            val mainScreenIntent = Intent(this, MainActivity::class.java)
            startActivity(mainScreenIntent)
        }


    }
}