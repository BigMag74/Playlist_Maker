package com.practicum.playlist_maker.settings.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlist_maker.App
import com.practicum.playlist_maker.KEY_FOR_SWITCH
import com.practicum.playlist_maker.R
import com.practicum.playlist_maker.SETTINGS_PREFERENCES
import com.practicum.playlist_maker.settings.view_model.SettingsViewModel


class SettingsActivity : AppCompatActivity() {
    lateinit var backButton: ImageView
    lateinit var shareButton: FrameLayout
    lateinit var supportButton: FrameLayout
    lateinit var userAgreementButton: FrameLayout
    lateinit var themeSwitcher: SwitchMaterial
    lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        viewModel = ViewModelProvider(
            this,
            SettingsViewModel.getViewModelFactory()
        )[SettingsViewModel::class.java]

        initViews()

        backButton.setOnClickListener {
            onBackPressed()
        }
        shareButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.yandex_practicum_URL))
            startActivity(intent)
        }
        supportButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.my_email)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.email_text))
            }
            startActivity(intent)
        }
        userAgreementButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(getString(R.string.user_agreement_URL))
            startActivity(intent)
        }

        themeSwitcher.isChecked = viewModel.isDarkTheme

        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            viewModel.switchTheme(checked)
        }
    }

    private fun initViews(){
        backButton = findViewById<ImageView>(R.id.backButton)
        shareButton = findViewById<FrameLayout>(R.id.shareButton)
        supportButton = findViewById<FrameLayout>(R.id.supportButton)
        userAgreementButton = findViewById<FrameLayout>(R.id.userAgreementButton)
        themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)
    }


}