package com.practicum.playlist_maker.creationPlaylist.ui.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.imageview.ShapeableImageView
import com.practicum.playlist_maker.R
import com.practicum.playlist_maker.creationPlaylist.domain.model.Playlist
import com.practicum.playlist_maker.creationPlaylist.ui.CreationPlaylistState
import com.practicum.playlist_maker.creationPlaylist.ui.view_model.CreationPlaylistViewModel
import com.practicum.playlist_maker.databinding.FragmentCreatePlaylistBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

class CreationPlaylistFragment : Fragment() {

    private var _binding: FragmentCreatePlaylistBinding? = null
    private val binding get() = _binding!!
    private val creationPlaylistViewModel: CreationPlaylistViewModel by viewModel()

    private var backButton: ImageView? = null
    private var playlistImage: ShapeableImageView? = null
    private var playlistName: EditText? = null
    private var playlistDescription: EditText? = null
    private var createButton: AppCompatButton? = null

    private var isImageChanged = false

    private var dialog: MaterialAlertDialogBuilder? = null

    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreatePlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backButton = binding.backButton
        playlistImage = binding.addPhotoImageView
        playlistName = binding.playlistNameEditText
        playlistDescription = binding.descriptionEditText
        createButton = binding.createNewPlaylistButton

        creationPlaylistViewModel.state.observe(viewLifecycleOwner) {
            render(it)
        }

        isImageChanged = false
        imageUri = null

        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        dialog = MaterialAlertDialogBuilder(requireContext(), R.style.dialog_style)
            .setTitle(getString(R.string.finish_creating_a_playlist))
            .setNeutralButton(getString(R.string.cancel)) { _, _ ->
            }
            .setPositiveButton(getString(R.string.finish)) { _, _ ->
                findNavController().navigateUp()
            }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            callback
        )

        setOnClickListeners()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val sourceTreeUri = data?.data
            context?.contentResolver?.takePersistableUriPermission(
                sourceTreeUri!!,
                Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            )
        }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView).visibility =
            View.GONE
    }

    override fun onStop() {
        super.onStop()
        requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView).visibility =
            View.VISIBLE
    }

    private val callback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isImageChanged || playlistName?.text?.isNotEmpty() == true || playlistDescription?.text?.isNotEmpty() == true) {
                    dialog?.show()
                } else {
                    findNavController().navigateUp()
                }

            }
        }

    private val textWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            if (p0.isNullOrEmpty())
                render(CreationPlaylistState.EmptyName())
            else
                render(CreationPlaylistState.ContentName())
        }

        override fun afterTextChanged(p0: Editable?) {}

    }

    private fun setOnClickListeners() {

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    playlistImage?.setImageURI(uri)
                    imageUri = uri
                    isImageChanged = true
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }

        playlistImage?.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        backButton?.setOnClickListener { showDialogBeforeExit() }

        playlistName?.addTextChangedListener(textWatcher)

        createButton?.setOnClickListener {
            imageUri?.let { it1 -> saveImageToPrivateStorage(it1) }
            creationPlaylistViewModel.savePlaylist(
                Playlist(
                    name = playlistName!!.text.toString(),
                    description = playlistDescription?.text.toString(),
                    pathUri = imageUri,
                    trackIds = mutableListOf(),
                    countOfTracks = 0
                )
            )
            Toast.makeText(
                requireContext(),
                "${getString(R.string.playlist)} ${playlistName?.text} ${getString(R.string.created)}.",
                Toast.LENGTH_LONG
            ).show()
            findNavController().navigateUp()
        }
    }

    private fun saveImageToPrivateStorage(uri: Uri) {
        val filePath =
            File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myAlbum")
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath, "first_cover.jpg")
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)

    }

    private fun showDialogBeforeExit() {
        if (isImageChanged || playlistName?.text?.isNotEmpty() == true || playlistDescription?.text?.isNotEmpty() == true) {
            dialog?.show()
        } else {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun render(state: CreationPlaylistState) {
        when (state) {
            is CreationPlaylistState.EmptyName -> {
                createButton?.isEnabled = false
            }
            is CreationPlaylistState.ContentName -> {
                createButton?.isEnabled = true
            }
        }
    }


}