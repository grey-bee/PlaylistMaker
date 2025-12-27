package com.practicum.playlistmaker.settings.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.App
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

        binding.themeSwitcher.isChecked = viewModel.isDarkTheme()

        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.changeDarkTheme(checked)
            (applicationContext as App).switchTheme(checked)
        }

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.shareApp.setOnClickListener {
            startActivity(viewModel.shareApp())
        }

        binding.writeToSupport.setOnClickListener {
            startActivity(viewModel.writeToSupport())
        }

        binding.userAgreement.setOnClickListener {
            startActivity(viewModel.userAgreement())
        }
    }
}