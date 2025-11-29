package com.practicum.playlistmaker

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import  com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.appbar.MaterialToolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        val darkThemeSwitch = findViewById<SwitchMaterial>(R.id.themeSwitcher)
        val shareApp = findViewById<TextView>(R.id.shareApp)
        val writeToSupport = findViewById<TextView>(R.id.writeToSupport)
        val userAgreement = findViewById<TextView>(R.id.userAgreement)

        darkThemeSwitch.isChecked = (currentNightMode == Configuration.UI_MODE_NIGHT_YES)

        darkThemeSwitch.setOnCheckedChangeListener { _, checked ->
            if (checked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        toolbar.setNavigationOnClickListener {
            finish()
        }

        shareApp.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            val chooserTitle = getString(R.string.share_app)

            intent.type = "text/plain"
            intent.putExtra(
                Intent.EXTRA_TEXT,
                getString(R.string.course_link)
            )
            startActivity(Intent.createChooser(intent, chooserTitle))
        }

        writeToSupport.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            val chooserTitle = getString(R.string.write_to_support)

            intent.data = Uri.parse("mailto:" + getString(R.string.my_email))
            intent.putExtra(
                Intent.EXTRA_SUBJECT,
                getString(R.string.subj_for_developers)
            )
            intent.putExtra(
                Intent.EXTRA_TEXT,
                getString(R.string.message_for_developers)
            )
            startActivity(Intent.createChooser(intent, chooserTitle))
        }

        userAgreement.setOnClickListener {
            val url = Uri.parse(getString(R.string.practicum_offer))
            val intent = Intent(Intent.ACTION_VIEW, url)
            startActivity(intent)
        }


    }
}