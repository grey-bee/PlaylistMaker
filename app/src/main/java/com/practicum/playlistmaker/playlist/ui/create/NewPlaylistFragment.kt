package com.practicum.playlistmaker.playlist.ui.create

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentNewPlaylistBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue
import androidx.core.net.toUri

class NewPlaylistFragment : Fragment() {
    private lateinit var binding: FragmentNewPlaylistBinding
    private val viewModel: NewPlaylistViewModel by viewModel()
    private var titleRequest: String = ""
    private var imageSelected = false
    private var selectedImageUri: Uri? = null

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(KEY_IMAGE_SELECTED, imageSelected)
        outState.putString(KEY_TITLE_REQUEST, titleRequest)
        outState.putString(KEY_DESCRIPTION, binding.description.editText?.text.toString())
        outState.putString(KEY_URI, selectedImageUri?.toString())
        super.onSaveInstanceState(outState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        titleRequest = savedInstanceState?.getString(KEY_TITLE_REQUEST) ?: ""
        binding.playlistName.editText?.setText(titleRequest)
        binding.description.editText?.setText(savedInstanceState?.getString(KEY_DESCRIPTION) ?: "")
        imageSelected = savedInstanceState?.getBoolean(KEY_IMAGE_SELECTED) == true
        selectedImageUri = savedInstanceState?.getString(KEY_URI)?.toUri()
        if (selectedImageUri != null) binding.pickerImage.setImageURI(selectedImageUri)

        binding.buttonCreate.isEnabled = titleRequest.isNotBlank() == true

        fun backButtonProcess() {
            if (titleRequest.isNotBlank() || binding.description.editText?.text?.isNotEmpty() == true || imageSelected) {
                MaterialAlertDialogBuilder(requireContext(), R.style.PlaylistDialogTheme)
                    .setTitle(getString(R.string.finish_playlist_creating))
                    .setMessage(R.string.all_unsaved_data_will_be_loose)
                    .setNegativeButton(R.string.cancel) { _, _ -> }
                    .setPositiveButton(R.string.finish) { _, _ -> findNavController().navigateUp() }
                    .show()
            } else {
                findNavController().navigateUp()
            }
        }

        binding.toolbar.setNavigationOnClickListener {
            backButtonProcess()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            backButtonProcess()
        }

        binding.playlistName.editText?.textCursorDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.cursor_color)

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    binding.pickerImage.setImageURI(uri)
                    imageSelected = true
                    selectedImageUri = uri
                }
            }

        binding.pickerImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        val titleWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.buttonCreate.isEnabled = !s.isNullOrEmpty()
            }

            override fun afterTextChanged(p0: Editable?) {
                titleRequest = p0.toString()
            }
        }
        binding.playlistName.editText?.addTextChangedListener(titleWatcher)

        binding.buttonCreate.setOnClickListener {
            val title = binding.playlistName.editText?.text.toString()
            val description = binding.description.editText?.text.toString()
            viewModel.savePlaylistInfo(title, description, selectedImageUri)
            setFragmentResult(KEY_NEW_PLAYLIST, bundleOf(KEY_TITLE to title))
        }
        viewModel.observePlaylistSaved().observe(viewLifecycleOwner) { check ->
            if (check) findNavController().navigateUp()
        }
    }

    companion object {
        private const val KEY_NEW_PLAYLIST = "new_playlist"
        private const val KEY_TITLE = "title"
        private const val KEY_IMAGE_SELECTED = "imageSelected"
        private const val KEY_TITLE_REQUEST = "titleRequest"
        private const val KEY_DESCRIPTION = "description"
        private const val KEY_URI = "uri"

    }
}