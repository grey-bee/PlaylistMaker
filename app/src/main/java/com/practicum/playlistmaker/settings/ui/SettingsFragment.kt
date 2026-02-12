package com.practicum.playlistmaker.settings.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.core.net.toUri
import com.practicum.playlistmaker.ui.settings.SettingsScreen
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ViewCompositionStrategy

class SettingsFragment : Fragment() {
    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val isDarkTheme by viewModel.observeTheme().observeAsState(false)
                SettingsScreen(
                    { shareApp() },
                    { writeToSupport() },
                    { userAgreement() },
                    isDarkTheme,
                    { viewModel.changeDarkTheme(it) }
                )
            }
        }
    }

    private fun shareApp() {
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

    private fun writeToSupport() {
        val data = viewModel.writeToSupport()
        val intent = Intent(Intent.ACTION_SENDTO)
        val chooserTitle = data.title

        val mailto = "mailto:${data.address}" +
                "?subject=${Uri.encode(data.subj)}" +
                "&body=${Uri.encode(data.body)}"

        intent.data = mailto.toUri()
        startActivity(Intent.createChooser(intent, chooserTitle))
    }

    private fun userAgreement() {
        val data = viewModel.userAgreement()
        val intent = Intent(Intent.ACTION_VIEW, data.value.toUri())
        startActivity(intent)
    }
}