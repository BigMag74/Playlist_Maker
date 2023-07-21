package com.practicum.playlist_maker.settings.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.navigation.fragment.findNavController
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlist_maker.R
import com.practicum.playlist_maker.databinding.FragmentSettingsBinding
import com.practicum.playlist_maker.main.ui.MainFragment
import com.practicum.playlist_maker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsFragment : Fragment() {

    private lateinit var backButton: ImageView
    private lateinit var shareButton: FrameLayout
    private lateinit var supportButton: FrameLayout
    private lateinit var userAgreementButton: FrameLayout
    private lateinit var themeSwitcher: SwitchMaterial

    private val viewModel: SettingsViewModel by viewModel<SettingsViewModel>()

    private lateinit var binding: FragmentSettingsBinding



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        shareButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.yandex_practicum_URL))

            startActivityOrShowErrorMessage(intent,getString(R.string.there_is_no_app_on_the_device_to_make_this_request))
        }

        supportButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.my_email)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.email_text))
            }

            startActivityOrShowErrorMessage(intent, getString(R.string.there_is_no_app_on_the_device_to_send_email))
        }
        userAgreementButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(getString(R.string.user_agreement_URL))

            startActivityOrShowErrorMessage(intent,getString(R.string.there_is_no_browser_on_the_device))
        }

        themeSwitcher.isChecked = viewModel.isDarkTheme

        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            viewModel.switchTheme(checked)
        }

    }


    private fun initViews() {
        backButton = binding.backButton
        shareButton = binding.shareButton
        supportButton = binding.supportButton
        userAgreementButton = binding.userAgreementButton
        themeSwitcher = binding.themeSwitcher
    }

    private fun startActivityOrShowErrorMessage(intent: Intent, message: String) {
        try {
            startActivity(intent)
        }
        catch (e:Exception){
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        }

    }


}