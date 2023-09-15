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
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.imageview.ShapeableImageView
import com.google.gson.Gson
import com.practicum.playlist_maker.R
import com.practicum.playlist_maker.creationPlaylist.domain.model.Playlist
import com.practicum.playlist_maker.creationPlaylist.ui.CreationPlaylistState
import com.practicum.playlist_maker.creationPlaylist.ui.EditPlaylistState
import com.practicum.playlist_maker.creationPlaylist.ui.view_model.CreationPlaylistViewModel
import com.practicum.playlist_maker.databinding.FragmentCreatePlaylistBinding
import com.practicum.playlist_maker.playlist.ui.activity.PlaylistScreenFragment.Companion.PLAYLIST
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.File
import java.io.FileOutputStream

class CreationPlaylistFragment : Fragment() {

    private var _binding: FragmentCreatePlaylistBinding? = null
    private val binding get() = _binding!!
    private val creationPlaylistViewModel: CreationPlaylistViewModel by viewModel {
        parametersOf(
            playlist
        )
    }

    private var backButton: ImageView? = null
    private var title: TextView? = null
    private var playlistImage: ShapeableImageView? = null
    private var playlistName: EditText? = null
    private var playlistDescription: EditText? = null
    private var createButton: AppCompatButton? = null

    private var isImageChanged = false

    private var dialog: MaterialAlertDialogBuilder? = null

    private var playlist: Playlist? = null

    private var imageUri: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreatePlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlist = Gson().fromJson(arguments?.getString(PLAYLIST), Playlist::class.java)

        backButton = binding.backButton
        title = binding.titleTextView
        playlistImage = binding.addPhotoImageView
        playlistName = binding.playlistNameEditText
        playlistDescription = binding.descriptionEditText
        createButton = binding.createNewPlaylistButton

        creationPlaylistViewModel.creationState.observe(viewLifecycleOwner) {
            renderCreationPlaylist(it)
        }
        creationPlaylistViewModel.editState.observe(viewLifecycleOwner) {
            renderEditPlaylist(it)
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
                if (playlist == null) {
                    if (isImageChanged || playlistName?.text?.isNotEmpty() == true || playlistDescription?.text?.isNotEmpty() == true) {
                        dialog?.show()
                    } else {
                        findNavController().navigateUp()
                    }
                } else
                    findNavController().navigateUp()
            }
        }

    private val textWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            if (p0.isNullOrEmpty())
                renderCreationPlaylist(CreationPlaylistState.EmptyName())
            else
                renderCreationPlaylist(CreationPlaylistState.ContentName())
        }

        override fun afterTextChanged(p0: Editable?) {}

    }

    private fun setOnClickListeners() {

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    playlistImage?.let {
                        Glide.with(it)
                            .load(uri)
                            .placeholder(R.drawable.album)
                            .centerCrop()
                            .into(playlistImage!!)
                    }
                    imageUri = uri.toString()
                    isImageChanged = true
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }

        playlistImage?.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        backButton?.setOnClickListener {
            if (playlist == null)
                showDialogBeforeExit()
            else
                findNavController().navigateUp()
        }

        playlistName?.addTextChangedListener(textWatcher)

        createButton?.setOnClickListener {
            if (playlist == null) {
                imageUri?.let { it1 -> saveImageToPrivateStorage(Uri.parse(it1)) }
                creationPlaylistViewModel.savePlaylist(
                    Playlist(
                        name = playlistName!!.text.toString().trim(),
                        description = playlistDescription?.text.toString().trim(),
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
            } else {
                creationPlaylistViewModel.updatePlaylist(
                    playlist!!.copy(
                        name = playlistName!!.text.toString().trim(),
                        description = playlistDescription?.text.toString().trim(),
                        pathUri = imageUri,
                    )
                )
                findNavController().navigateUp()
            }
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
            findNavController().navigateUp()
        }
    }

    private fun renderCreationPlaylist(state: CreationPlaylistState) {
        when (state) {
            is CreationPlaylistState.EmptyName -> {
                createButton?.isEnabled = false
            }
            is CreationPlaylistState.ContentName -> {
                createButton?.isEnabled = true
            }
        }
    }

    private fun renderEditPlaylist(state: EditPlaylistState) {
        when (state) {
            is EditPlaylistState.EmptyName -> {
                createButton?.isEnabled = false
            }
            is EditPlaylistState.ContentName -> {
                createButton?.isEnabled = true
            }
            is EditPlaylistState.JustOpened -> {
                imageUri = playlist?.pathUri
                createButton?.isEnabled = true
                createButton?.text = getString(R.string.save)
                title?.text = getString(R.string.edit)
                playlistName?.setText(state.playlist.name)
                playlistDescription?.setText(state.playlist.description)
                playlistImage?.let {
                    Glide.with(it)
                        .load(state.playlist.pathUri)
                        .placeholder(R.drawable.album)
                        .centerCrop()
                        .into(playlistImage!!)
                }

            }
        }
    }


}