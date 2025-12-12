package com.practicum.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import  com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.appbar.MaterialToolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        val darkThemeSwitch = findViewById<SwitchMaterial>(R.id.themeSwitcher)
        val shareApp = findViewById<TextView>(R.id.shareApp)
        val writeToSupport = findViewById<TextView>(R.id.writeToSupport)
        val userAgreement = findViewById<TextView>(R.id.userAgreement)

        darkThemeSwitch.isChecked = (applicationContext as App).darkTheme

        darkThemeSwitch.setOnCheckedChangeListener { _, checked ->
            (applicationContext as App).switchTheme(checked)
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

            val mailto = "mailto:${getString(R.string.my_email)}" +
                    "?subject=${Uri.encode(getString(R.string.subj_for_developers))}" +
                    "&body=${Uri.encode(getString(R.string.message_for_developers))}"

            intent.data = mailto.toUri()
            startActivity(Intent.createChooser(intent, chooserTitle))
        }

        userAgreement.setOnClickListener {
            val url = getString(R.string.practicum_offer).toUri()
            val intent = Intent(Intent.ACTION_VIEW, url)
            startActivity(intent)
        }
    }
}