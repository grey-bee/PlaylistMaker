package com.practicum.playlistmaker.playlist.ui.edit

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.core.os.BundleCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentEditPlaylistBinding
import com.practicum.playlistmaker.playlist.domain.model.Playlist
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.getValue

class PlaylistEditFragment : Fragment() {
    private lateinit var binding: FragmentEditPlaylistBinding
    private val viewModel: PlaylistEditViewModel by viewModel {
        parametersOf(playlist)
    }
    private var selectedImageUri: Uri? = null

    private val playlist by lazy {
        requireNotNull(
            BundleCompat.getParcelable(
                requireArguments(),
                PlaylistEditFragment.Companion.ARGS_PLAYLIST,
                Playlist::class.java
            )
        ) { R.string.playlist_is_required }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            pickerImage.setImageURI(playlist.imagePath?.toUri())
            playlistName.editText?.setText(playlist.name)
            description.editText?.setText(playlist.description)
            toolbar.setOnClickListener { findNavController().navigateUp() }
            buttonSave.setOnClickListener {
                val title = binding.playlistName.editText?.text.toString()
                val description = binding.description.editText?.text.toString()
                viewModel.savePlaylistInfo(title, description, selectedImageUri)
            }
        }
        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    binding.pickerImage.setImageURI(uri)
                    selectedImageUri = uri
                }
            }
        val titleWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.buttonSave.isEnabled = !s.isNullOrEmpty()
            }
            override fun afterTextChanged(p0: Editable?) {}
        }
        binding.playlistName.editText?.addTextChangedListener(titleWatcher)

        binding.pickerImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        viewModel.observePlaylistSaved().observe(viewLifecycleOwner) { check ->
            if (check) findNavController().navigateUp()
        }
    }


    companion object {
        private const val ARGS_PLAYLIST = "playlist"
        fun createArgs(playlist: Playlist): Bundle =
            bundleOf(ARGS_PLAYLIST to playlist)
    }
}