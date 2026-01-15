package com.practicum.playlistmaker.medialibrary.ui

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import com.practicum.playlistmaker.favorites.ui.FavoritesFragment

class MediaLibraryPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FavoritesFragment()
            else -> PlaylistsFragment()
        }
    }

    override fun getItemCount(): Int {
        return 2
    }
}