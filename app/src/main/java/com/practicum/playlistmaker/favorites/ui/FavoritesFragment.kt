package com.practicum.playlistmaker.favorites.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentFavoritesBinding
import com.practicum.playlistmaker.player.ui.PlayerFragment
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.search.ui.TrackAdapter
import com.practicum.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {
    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var trackClickDebounce: (Track) -> Unit
    private lateinit var trackAdapter: TrackAdapter
    private val viewModel: FavoritesViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observeState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is FavoritesState.Content -> {
                    trackAdapter.updateTracks(state.tracks)
                    trackAdapter.notifyDataSetChanged()
                    binding.noTracksPlaceholder.visibility = View.GONE
                    binding.tracksRecyclerView.visibility = View.VISIBLE
                }

                is FavoritesState.Empty -> {
                    binding.noTracksPlaceholder.visibility = View.VISIBLE
                    binding.tracksRecyclerView.visibility = View.GONE
                }
            }
        }

        trackClickDebounce =
            debounce<Track>(
                CLICK_DEBOUNCE_DELAY,
                viewLifecycleOwner.lifecycleScope,
                false
            ) { track ->
                openAudioPlayer(track)
            }

        trackAdapter = TrackAdapter(emptyList(), { item -> trackClickDebounce(item) })

        binding.tracksRecyclerView.adapter = trackAdapter
    }

    private fun openAudioPlayer(track: Track) {
        val bundle = PlayerFragment.createArgs(track)
        findNavController().navigate(
            R.id.action_mediaLibraryFragment_to_audioPlayerFragment,
            bundle
        )
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}