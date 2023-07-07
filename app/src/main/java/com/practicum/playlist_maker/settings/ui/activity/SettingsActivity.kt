package com.practicum.playlist_maker.settings.ui.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlist_maker.R
import com.practicum.playlist_maker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsActivity : AppCompatActivity() {
    private lateinit var backButton: ImageView
    private lateinit var shareButton: FrameLayout
    private lateinit var supportButton: FrameLayout
    private lateinit var userAgreementButton: FrameLayout
    private lateinit var themeSwitcher: SwitchMaterial

    private val viewModel: SettingsViewModel by viewModel<SettingsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        initViews()

        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
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
        backButton = findViewById<ImageView>(R.id.backButton)
        shareButton = findViewById<FrameLayout>(R.id.shareButton)
        supportButton = findViewById<FrameLayout>(R.id.supportButton)
        userAgreementButton = findViewById<FrameLayout>(R.id.userAgreementButton)
        themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)
    }

    private fun startActivityOrShowErrorMessage(intent: Intent, message: String) {
        try {
            startActivity(intent)
        }
        catch (e:Exception){
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }

    }


}