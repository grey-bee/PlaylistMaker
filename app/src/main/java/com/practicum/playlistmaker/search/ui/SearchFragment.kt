package com.practicum.playlistmaker.search.ui

import android.content.IntentFilter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.player.ui.PlayerFragment
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.ui.search.SearchScreen
import com.practicum.playlistmaker.util.InternetConnectionReceiver
import com.practicum.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class SearchFragment : Fragment() {
    private val internetConnectionReceiver = InternetConnectionReceiver()
    private val viewModel: SearchViewModel by viewModel()
    private lateinit var trackClickDebounce: (Track) -> Unit

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        trackClickDebounce =
            debounce<Track>(
                CLICK_DEBOUNCE_DELAY,
                viewLifecycleOwner.lifecycleScope,
                false
            ) { track ->
                openAudioPlayer(track)
            }
        return ComposeView(requireContext()).apply {
            setContent {
                val screenState by viewModel.observeState()
                    .observeAsState(SearchScreenState.History(emptyList()))
                val searchText by viewModel.observeSearchText().observeAsState("")
                SearchScreen(
                    searchText,
                    { viewModel.onTextChanged(it) },
                    screenState
                ) { trackClickDebounce(it) }
            }
        }
    }

    private fun openAudioPlayer(item: Track) {
        viewModel.addTrackToHistory(item)
        val bundle = PlayerFragment.createArgs(item)
        findNavController().navigate(R.id.action_searchFragment_to_audioPlayerFragment, bundle)
    }

    override fun onResume() {
        super.onResume()
        ContextCompat.registerReceiver(
            requireContext(),
            internetConnectionReceiver,
            IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"),
            ContextCompat.RECEIVER_EXPORTED
        )
    }

    override fun onPause() {
        super.onPause()
        requireContext().unregisterReceiver(internetConnectionReceiver)
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}