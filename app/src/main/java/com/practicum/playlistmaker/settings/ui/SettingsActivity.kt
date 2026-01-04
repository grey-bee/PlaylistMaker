package com.practicum.playlistmaker.settings.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {
    private val viewModel: SettingsViewModel by viewModel()
    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        viewModel.observeTheme()
            .observe(this) { data ->
                binding.themeSwitcher.isChecked = data
            }

        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.changeDarkTheme(checked)
        }

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.shareApp.setOnClickListener {
            val data = viewModel.shareApp()
            val intent = Intent(Intent.ACTION_SEND)
            val chooserTitle = data.title
            intent.type = "text/plain"
            intent.putExtra(
                Intent.EXTRA_TEXT,
                data.text
            )
            startActivity(Intent.createChooser(intent, chooserTitle))
        }

        binding.writeToSupport.setOnClickListener {
            val data = viewModel.writeToSupport()
            val intent = Intent(Intent.ACTION_SENDTO)
            val chooserTitle = data.title

            val mailto = "mailto:${data.address}" +
                    "?subject=${Uri.encode(data.subj)}" +
                    "&body=${Uri.encode(data.body)}"

            intent.data = mailto.toUri()
            startActivity(Intent.createChooser(intent, chooserTitle))
        }

        binding.userAgreement.setOnClickListener {
            val data = viewModel.userAgreement()
            val intent = Intent(Intent.ACTION_VIEW, data.value.toUri())
            startActivity(intent)
        }
    }
}