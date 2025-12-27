package com.practicum.playlistmaker.settings.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var viewModel: SettingsViewModel
    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val factory = SettingsViewModel.getFactory(this)
        viewModel = ViewModelProvider(this, factory)[SettingsViewModel::class.java]

        binding.themeSwitcher.isChecked = (applicationContext as App).darkTheme

        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            (applicationContext as App).switchTheme(checked)
        }

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.shareApp.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            val chooserTitle = getString(R.string.share_app)

            intent.type = "text/plain"
            intent.putExtra(
                Intent.EXTRA_TEXT,
                getString(R.string.course_link)
            )
            startActivity(Intent.createChooser(intent, chooserTitle))
        }

        binding.writeToSupport.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            val chooserTitle = getString(R.string.write_to_support)

            val mailto = "mailto:${getString(R.string.my_email)}" +
                    "?subject=${Uri.encode(getString(R.string.subj_for_developers))}" +
                    "&body=${Uri.encode(getString(R.string.message_for_developers))}"

            intent.data = mailto.toUri()
            startActivity(Intent.createChooser(intent, chooserTitle))
        }

        binding.userAgreement.setOnClickListener {
            val url = getString(R.string.practicum_offer).toUri()
            val intent = Intent(Intent.ACTION_VIEW, url)
            startActivity(intent)
        }
    }
}