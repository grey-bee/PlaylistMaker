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
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.playlist.ui.list.PlaylistsState
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.ui.player.PlayerScreen
import com.practicum.playlistmaker.util.InternetConnectionReceiver
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayerFragment : Fragment() {
    private val internetConnectionReceiver = InternetConnectionReceiver()
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
        inflater: LayoutInflater,
        container: ViewGroup?,
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
                val playlistsState by viewModel.observePlaylists()
                    .observeAsState(PlaylistsState.Empty)
                PlayerScreen(
                    track,
                    playerScreenState,
                    playlistsState,
                    isFavorite,
                    { viewModel.playbackControl() },
                    { viewModel.onFavoriteClicked(it) },
                    { viewModel.onAddToPlaylistClicked(it) },
                    { findNavController().navigateUp() },
                    { findNavController().navigate(R.id.action_audioPlayerFragment_to_newPlaylistFragment) }
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
        private const val ARGS_TRACK = "track"
        fun createArgs(track: Track): Bundle =
            bundleOf(ARGS_TRACK to track)
    }
}