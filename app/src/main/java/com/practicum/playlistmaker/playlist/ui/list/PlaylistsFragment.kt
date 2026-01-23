package com.practicum.playlistmaker.playlist.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import com.practicum.playlistmaker.playlist.domain.model.Playlist
import com.practicum.playlistmaker.playlist.ui.details.PlaylistFragment
import com.practicum.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {
    private var _binding: FragmentPlaylistsBinding? = null
    private val binding = _binding!!
    private val playlistsViewModel: PlaylistsViewModel by viewModel()
    private lateinit var playlistAdapter: PlaylistsAdapter
    private lateinit var playlistClickDebounce: (Playlist) -> Unit

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlistsViewModel.observeState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is PlaylistsState.Content -> {
                    playlistAdapter.updatePlaylists(state.playlists)
                    playlistAdapter.notifyDataSetChanged()
                    binding.noPlaylistsPlaceholder.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                }

                is PlaylistsState.Empty -> {
                    binding.noPlaylistsPlaceholder.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE
                }
            }
        }
        binding.recyclerView.layoutManager = GridLayoutManager(context, 2)

        binding.newPlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_mediaLibraryFragment_to_newPlaylistFragment)
        }

        playlistClickDebounce =
            debounce<Playlist>(
                CLICK_DEBOUNCE_DELAY,
                viewLifecycleOwner.lifecycleScope,
                false
            ) { playlist ->
                val bundle = PlaylistFragment.createArgs(playlist)
                findNavController().navigate(
                    R.id.action_mediaLibraryFragment_to_playlistFragment,
                    bundle
                )
            }

        playlistAdapter = PlaylistsAdapter(emptyList(), { item -> playlistClickDebounce(item) })
        binding.recyclerView.adapter = playlistAdapter


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}