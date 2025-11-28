package com.practicum.playlistmaker

import android.content.Intent
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
        val currentNightMode = AppCompatDelegate.getDefaultNightMode()

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        val darkThemeSwitch = findViewById<SwitchMaterial>(R.id.themeSwitcher)
        val shareApp = findViewById<TextView>(R.id.shareApp)
        val writeToSupport = findViewById<TextView>(R.id.writeToSupport)
        val userAgreement = findViewById<TextView>(R.id.userAgreement)

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

        shareApp.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            val chooserTitle = getString(R.string.share_app)

            intent.type = "text/plain"
            intent.putExtra(
                Intent.EXTRA_TEXT,
                "Ссылка на курс: https://practicum.yandex.ru/android-developer/"
            )
            startActivity(Intent.createChooser(intent, chooserTitle))
        }

        writeToSupport.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            val chooserTitle = getString(R.string.write_to_support)

            intent.data = Uri.parse("mailto:andrey@izoor.com")
            intent.putExtra(
                Intent.EXTRA_SUBJECT,
                "Сообщение разработчикам и разработчицам приложения Playlist Maker"
            )
            intent.putExtra(
                Intent.EXTRA_TEXT,
                "Спасибо разработчикам и разработчицам за крутое приложение!"
            )
            startActivity(Intent.createChooser(intent, chooserTitle))
        }

        userAgreement.setOnClickListener {
            val url = Uri.parse("https://yandex.ru/legal/practicum_offer/")
            val intent = Intent(Intent.ACTION_VIEW, url)
            startActivity(intent)
        }
    }
}