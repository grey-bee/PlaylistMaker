package com.practicum.playlistmaker.playlist.ui.create

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentNewPlaylistBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class NewPlaylistFragment : Fragment() {
    private lateinit var binding: FragmentNewPlaylistBinding
    private val viewModel: NewPlaylistViewModel by viewModel()
    private var titleRequest = ""
    private var descriptorRequest = ""

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
        binding.buttonCreate.isEnabled = false
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.playlistName.editText?.textCursorDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.cursor_color)

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    binding.pickerImage.setImageURI(uri)
                    viewModel.saveCoverAlbum(uri)
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
            viewModel.savePlaylistInfo(
                binding.playlistName.editText?.text.toString(),
                binding.description.editText?.text.toString()
            )
            findNavController().navigateUp()
        }
    }
}