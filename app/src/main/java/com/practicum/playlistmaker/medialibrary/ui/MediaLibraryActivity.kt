package com.practicum.playlistmaker.medialibrary.ui

import android.os.Bundle

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityMediaLibraryBinding

class MediaLibraryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMediaLibraryBinding
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMediaLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.viewPager.adapter = MediaLibraryPagerAdapter(this)
        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) {
            tab, position ->
            when(position) {
                0 -> tab.text = getString(R.string.featured_tracks)
                1 -> tab.text = getString(R.string.playlists)
            }
        }
        tabMediator.attach()

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

}