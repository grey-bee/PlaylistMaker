package com.practicum.playlistmaker.search.ui

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.player.ui.PlayerFragment
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class SearchFragment : Fragment() {
    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private var _binding: FragmentSearchBinding? = null
    private val binding = _binding!!
    private val viewModel: SearchViewModel by viewModel()
    private var searchRequest = ""
    private lateinit var trackClickDebounce: (Track) -> Unit
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (viewModel.observeState().value == null) {
            viewModel.getSearchHistory()
        }


        val searchWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (s.isNullOrEmpty()) {
                    binding.searchClearButton.isVisible = false
                } else {
                    binding.searchClearButton.isVisible = true
                    viewModel.onTextChanged(s.toString())
                }
            }

            override fun afterTextChanged(s: Editable?) {
                searchRequest = s.toString()
            }
        }

        binding.searchInput.addTextChangedListener(searchWatcher)

        viewModel.observeState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is SearchScreenState.Empty -> showNothingFound()
                is SearchScreenState.Error -> showError()
                is SearchScreenState.Loading -> showLoading()
                is SearchScreenState.Content -> showContent(state.tracks)
                is SearchScreenState.History -> showHistory(state.tracks)
            }
        }

        binding.searchInput.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.searchInput.text.isNullOrEmpty()
            ) viewModel.getSearchHistory()
            else closeAll()
        }

        binding.searchClearButton.setOnClickListener {
            binding.searchInput.text.clear()
            val inputMethodManager =
                requireContext().getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.searchInput.windowToken, 0)
            viewModel.getSearchHistory()
        }
        binding.refreshButton.setOnClickListener {
            viewModel.onTextChanged(binding.searchInput.text.toString())
        }

        binding.historyClearButton.setOnClickListener {
            viewModel.historyClear()
            closeAll()
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
        historyAdapter = TrackAdapter(emptyList(), { item -> trackClickDebounce(item) })

        binding.searchRecyclerView.adapter = trackAdapter
        binding.historyRecyclerView.adapter = historyAdapter
    }


    private fun showContent(data: List<Track>) {
        trackAdapter.updateTracks(data)
        trackAdapter.notifyDataSetChanged()

        binding.apply {
            searchRecyclerView.isVisible = true
            historyLayout.isVisible = false
            nothingFoundPlaceholder.isVisible = false
            noConnectionPlaceholder.isVisible = false
            progressBar.isVisible = false
        }
    }

    private fun showHistory(data: List<Track>) {
        historyAdapter.updateTracks(data)
        historyAdapter.notifyDataSetChanged()
        binding.apply {
            searchRecyclerView.isVisible = false
            if (data.isNotEmpty()) historyLayout.isVisible = true
            nothingFoundPlaceholder.isVisible = false
            noConnectionPlaceholder.isVisible = false
            progressBar.isVisible = false
        }
    }

    private fun showLoading() {
        binding.apply {
            searchRecyclerView.isVisible = false
            historyLayout.isVisible = false
            nothingFoundPlaceholder.isVisible = false
            noConnectionPlaceholder.isVisible = false
            progressBar.isVisible = true
        }
    }

    private fun showNothingFound() {
        binding.apply {
            searchRecyclerView.isVisible = false
            historyLayout.isVisible = false
            nothingFoundPlaceholder.isVisible = true
            noConnectionPlaceholder.isVisible = false
            progressBar.isVisible = false
        }
    }

    private fun showError() {
        binding.apply {
            searchRecyclerView.isVisible = false
            historyLayout.isVisible = false
            nothingFoundPlaceholder.isVisible = false
            noConnectionPlaceholder.isVisible = true
            progressBar.isVisible = false
        }
    }

    private fun closeAll() {
        binding.apply {
            searchRecyclerView.isVisible = false
            historyLayout.isVisible = false
            nothingFoundPlaceholder.isVisible = false
            noConnectionPlaceholder.isVisible = false
            progressBar.isVisible = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun openAudioPlayer(item: Track) {
        viewModel.addTrackToHistory(item)
        val bundle = PlayerFragment.createArgs(item)
        findNavController().navigate(R.id.action_searchFragment_to_audioPlayerFragment, bundle)
    }
}