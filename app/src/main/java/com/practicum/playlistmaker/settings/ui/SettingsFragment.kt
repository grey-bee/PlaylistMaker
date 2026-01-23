package com.practicum.playlistmaker.settings.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import com.practicum.playlistmaker.databinding.FragmentSettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class SettingsFragment : Fragment() {
    private val viewModel: SettingsViewModel by viewModel()

    private var _binding: FragmentSettingsBinding? = null
    private val binding = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeTheme()
            .observe(viewLifecycleOwner) { data ->
                binding.themeSwitcher.isChecked = data
            }

        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.changeDarkTheme(checked)
        }

        binding.shareLabel.setOnClickListener {
            val data = viewModel.shareApp()
            val intent = Intent(Intent.ACTION_SEND)
            val chooserTitle = data.title
            intent.type = "text/plain"
            intent.putExtra(
                Intent.EXTRA_TEXT,
                data.text
            )
            startActivity(Intent.createChooser(intent, chooserTitle))
        }

        binding.supportLabel.setOnClickListener {
            val data = viewModel.writeToSupport()
            val intent = Intent(Intent.ACTION_SENDTO)
            val chooserTitle = data.title

            val mailto = "mailto:${data.address}" +
                    "?subject=${Uri.encode(data.subj)}" +
                    "&body=${Uri.encode(data.body)}"

            intent.data = mailto.toUri()
            startActivity(Intent.createChooser(intent, chooserTitle))
        }

        binding.agreementLabel.setOnClickListener {
            val data = viewModel.userAgreement()
            val intent = Intent(Intent.ACTION_VIEW, data.value.toUri())
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}