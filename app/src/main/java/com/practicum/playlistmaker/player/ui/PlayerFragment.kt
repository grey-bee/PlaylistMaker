package com.practicum.playlistmaker.player.ui

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.ContextCompat
import androidx.core.os.BundleCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.dpToPx
import com.practicum.playlistmaker.favorites.ui.FavoritesState
import com.practicum.playlistmaker.playlist.domain.model.Playlist
import com.practicum.playlistmaker.playlist.ui.list.PlaylistsState
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.ui.player.PlayerScreen
import com.practicum.playlistmaker.util.InternetConnectionReceiver
import com.practicum.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.getValue
import kotlin.requireNotNull

class PlayerFragment : Fragment() {
    private val internetConnectionReceiver = InternetConnectionReceiver()
    private lateinit var playlistClickDebounce: (Playlist) -> Unit
    private val track by lazy {
        requireNotNull(
            BundleCompat.getParcelable(
                requireArguments(),
                ARGS_TRACK,
                Track::class.java
            )
        ) { "Track is required" }
    }
    val viewModel: PlayerViewModel by viewModel() {
        parametersOf(track)
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as PlayerService.PlayerServiceBinder
            viewModel.setPlayerControl(binder.getService())
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            viewModel.removePlayerControl()
        }
    }

    override fun onResume() {
        super.onResume()
        ContextCompat.registerReceiver(
            requireContext(),
            internetConnectionReceiver,
            IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"),
            ContextCompat.RECEIVER_EXPORTED
        )
        viewModel.screenOpen()
    }

    override fun onPause() {
        super.onPause()
        requireContext().unregisterReceiver(internetConnectionReceiver)
        viewModel.screenClose()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val playerScreenState by viewModel.observePlayerScreenState().observeAsState(
                    PlayerState.Prepared()
                )
                val isFavorite by viewModel.observeIsFavorite().observeAsState(
                    false
                )
                PlayerScreen(
                    track,
                    playerScreenState,
                    isFavorite,
                    { viewModel.playbackControl() },
                    onFavoriteClick = { viewModel.onFavoriteClicked(it) },
                    onAddToPlaylist = { }, { findNavController().navigateUp() }
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
        bindMusicService()

        viewModel.observeToastMessage().observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { text ->
                Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()
            }
        }

//        val bottomSheetContainer = binding.playlistBottomSheet
//        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
//            state = BottomSheetBehavior.STATE_HIDDEN
//        }
//        binding.addToPlaylistButton.setOnClickListener {
//            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
//            binding.darkScreen.visibility = View.VISIBLE
//            binding.darkScreen.alpha = 1f
//        }

//        bottomSheetBehavior.addBottomSheetCallback(object :
//            BottomSheetBehavior.BottomSheetCallback() {
//            override fun onStateChanged(bottomSheet: View, newState: Int) {
//                when (newState) {
//                    BottomSheetBehavior.STATE_HIDDEN -> {
//                        binding.darkScreen.visibility = View.GONE
//                    }
//
//                    BottomSheetBehavior.STATE_COLLAPSED -> {
//                        binding.darkScreen.visibility = View.VISIBLE
//                    }
//                }
//            }
//
//            override fun onSlide(bottomSheet: View, slideOffset: Float) {
//                if (slideOffset < 0) binding.darkScreen.alpha = (slideOffset + 1) / 2
//            }
//        })

        playlistClickDebounce =
            debounce<Playlist>(
                CLICK_DEBOUNCE_DELAY,
                viewLifecycleOwner.lifecycleScope,
                false
            ) { playlist ->
                if (viewModel.onAddToPlaylistClicked(playlist)) {
//                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                }
            }


//        viewModel.observePlaylists().observe(viewLifecycleOwner) { state ->
//            when (state) {
//                is PlaylistsState.Empty -> {}
//                is PlaylistsState.Content -> {
//                    playlistsAdapter.updatePlaylists(state.playlists)
//                }
//            }
//        }

//        binding.newPlaylist.setOnClickListener {
//            findNavController().navigate(R.id.action_audioPlayerFragment_to_newPlaylistFragment)
//        }
//        setFragmentResultListener("new_playlist") { _, bundle ->
//            val title = bundle.getString("title")
//            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
//            Toast.makeText(requireContext(), "Плейлист $title создан", Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbindMusicService()
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (!isGranted) {
            Toast.makeText(requireContext(), "Can't start foreground service!", Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun bindMusicService() {
        val intent = Intent(requireContext(), PlayerService::class.java).apply {
            putExtra("preview_url", track.previewUrl)
            putExtra("track_name", track.trackName)
            putExtra("artist_name", track.artistName)
        }
        requireContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    private fun unbindMusicService() {
        requireContext().unbindService(serviceConnection)
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 500L
        private const val ARGS_TRACK = "track"
        fun createArgs(track: Track): Bundle =
            bundleOf(ARGS_TRACK to track)
    }
}