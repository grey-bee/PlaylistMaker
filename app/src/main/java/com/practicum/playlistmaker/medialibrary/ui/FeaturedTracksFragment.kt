package com.practicum.playlistmaker.medialibrary.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentFeaturedTracksBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class FeaturedTracksFragment : Fragment() {
    private lateinit var binding: FragmentFeaturedTracksBinding
    private val featuredTracksViewModel: FeaturedTracksViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFeaturedTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        featuredTracksViewModel.observeState().observe(viewLifecycleOwner) { tracks ->
            if (tracks.isEmpty()) {
                binding.noTracksPlaceholder.visibility =
                    View.VISIBLE
            }
        }
    }
    companion object {
        fun newInstance() = FeaturedTracksFragment()
    }
}