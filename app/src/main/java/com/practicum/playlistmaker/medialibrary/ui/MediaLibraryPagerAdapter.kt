package com.practicum.playlistmaker.medialibrary.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MediaLibraryPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FeaturedTracksFragment()
            else -> PlaylistsFragment()
        }
    }

    override fun getItemCount(): Int {
        return 2
    }
}