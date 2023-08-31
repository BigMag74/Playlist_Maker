package com.practicum.playlist_maker.search.ui.activity


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.practicum.playlist_maker.*
import com.practicum.playlist_maker.databinding.FragmentSearchBinding
import com.practicum.playlist_maker.player.domain.model.Track
import com.practicum.playlist_maker.player.ui.activity.AudioPlayerActivity
import com.practicum.playlist_maker.search.ui.SearchHistoryAdapter
import com.practicum.playlist_maker.search.ui.SearchState
import com.practicum.playlist_maker.search.ui.TrackAdapter
import com.practicum.playlist_maker.search.ui.TracksClickListener
import com.practicum.playlist_maker.search.ui.view_model.SearchViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchFragment : Fragment() {

    private var searchEditText: EditText? = null
    private var crossButton: ImageView? = null
    private var recyclerView: RecyclerView? = null
    private var historyRecyclerView: RecyclerView? = null
    private var placeholderMessage: TextView? = null
    private var placeholderImage: ImageView? = null
    private var refreshButton: Button? = null
    private var clearHistoryButton: Button? = null
    private var historyLayout: LinearLayout? = null
    private var progressBar: ProgressBar? = null

    private val viewModel by viewModel<SearchViewModel>()

    private var trackAdapter: TrackAdapter? = null
    private var searchHistoryAdapter: SearchHistoryAdapter? = null

    private lateinit var binding: FragmentSearchBinding


    var editTextText = ""

    private lateinit var handler: Handler

    private var isClickAllowed = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

        val onClickListener = object : TracksClickListener {
            override fun onTrackClick(track: Track) {
                if (clickDebounce()) {
                    val intent = Intent(requireContext(), AudioPlayerActivity::class.java)
                    intent.putExtra(TRACK, Gson().toJson(track))
                    startActivity(intent)

                    viewModel.addTrackToSearchHistory(track)
                }
            }

        }

        trackAdapter = TrackAdapter(onClickListener)
        recyclerView?.adapter = trackAdapter

        searchHistoryAdapter = SearchHistoryAdapter(onClickListener)

        searchEditText?.addTextChangedListener(searchButtonTextWatcher)
        searchEditText?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE && editTextText.isNotEmpty()) {
                viewModel.searchDebounce(editTextText)
                recyclerView?.visibility = View.VISIBLE
            }
            false
        }

        searchEditText?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && editTextText.isEmpty())
                viewModel.showHistory()
        }

        setOnClickListeners()

        handler = Handler(Looper.getMainLooper())

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
    }


    private fun initViews() {
        searchEditText = binding.searchEditText
        crossButton = binding.crossButton
        recyclerView = binding.searchRecyclerView
        historyRecyclerView = binding.historyRecyclerView
        placeholderMessage = binding.placeholderMessage
        placeholderImage = binding.placeholderImage
        refreshButton = binding.refreshButton
        clearHistoryButton = binding.clearHistoryButton
        historyLayout = binding.historyLayout
        progressBar = binding.progressBar
    }


    private fun render(state: SearchState) {
        when (state) {
            is SearchState.Content -> showContent(state.tracks)
            is SearchState.Empty -> showEmpty(state.emptyMessageResId)
            is SearchState.Error -> showError(state.errorMessageResId)
            is SearchState.Loading -> showLoading()
            is SearchState.SearchHistory -> showSearchHistory(state.tracks)
        }
    }


    private fun showContent(tracks: List<Track>) {
        progressBar?.visibility = View.GONE
        placeholderMessage?.visibility = View.GONE
        placeholderImage?.visibility = View.GONE
        refreshButton?.visibility = View.GONE
        recyclerView?.visibility = View.VISIBLE

        trackAdapter?.tracks?.clear()
        trackAdapter?.tracks?.addAll(tracks)
        trackAdapter?.notifyDataSetChanged()
    }

    private fun showEmpty(emptyMessageResId: Int) {
        progressBar?.visibility = View.GONE
        refreshButton?.visibility = View.GONE
        placeholderMessage?.visibility = View.VISIBLE
        placeholderImage?.visibility = View.VISIBLE

        trackAdapter?.tracks?.clear()
        trackAdapter?.notifyDataSetChanged()

        placeholderMessage?.text = getString(emptyMessageResId)
        placeholderImage?.setImageResource(R.drawable.nothing_found)
    }

    private fun showError(errorMessageResId: Int) {
        progressBar?.visibility = View.GONE
        placeholderMessage?.visibility = View.VISIBLE
        placeholderImage?.visibility = View.VISIBLE
        refreshButton?.visibility = View.VISIBLE

        trackAdapter?.tracks?.clear()
        trackAdapter?.notifyDataSetChanged()

        placeholderMessage?.text = getString(errorMessageResId)
        placeholderImage?.setImageResource(R.drawable.internet_issues)

    }

    private fun showLoading() {
        progressBar?.visibility = View.VISIBLE
        recyclerView?.visibility = View.GONE
        placeholderMessage?.visibility = View.GONE
        placeholderImage?.visibility = View.GONE
        refreshButton?.visibility = View.GONE
    }

    private fun showSearchHistory(tracks: List<Track>?) {
        if (tracks.isNullOrEmpty()) {
            historyLayout?.visibility = View.GONE
        } else {
            historyLayout?.visibility = View.VISIBLE
            searchHistoryAdapter?.tracks = tracks as ArrayList<Track>
            historyRecyclerView?.adapter = searchHistoryAdapter
        }
    }


    private fun setOnClickListeners() {
        crossButton?.setOnClickListener {
            searchEditText?.setText("")
            trackAdapter?.tracks?.clear()
            trackAdapter?.notifyDataSetChanged()
            placeholderMessage?.visibility = View.GONE
            placeholderImage?.visibility = View.GONE
            refreshButton?.visibility = View.GONE
            val inputMethod =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethod.hideSoftInputFromWindow(requireActivity().window.decorView.windowToken, 0)
            viewModel.showHistory()

        }

        refreshButton?.setOnClickListener {
            refreshButton?.visibility = View.GONE
            if (editTextText.isNotEmpty()) viewModel.searchDebounce(editTextText)
        }

        clearHistoryButton?.setOnClickListener {
            viewModel.clearSearchHistory()
            searchHistoryAdapter?.tracks = ArrayList()
            searchHistoryAdapter?.notifyDataSetChanged()
            historyLayout?.visibility = View.GONE
        }
    }


    private val searchButtonTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(
            s: CharSequence?,
            start: Int,
            count: Int,
            after: Int
        ) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            if (searchEditText?.hasFocus() == true && s?.isEmpty() == true) {
                viewModel.showHistory()

                recyclerView?.visibility = View.GONE
                placeholderMessage?.visibility = View.GONE
                placeholderImage?.visibility = View.GONE
                refreshButton?.visibility = View.GONE
            } else {
                historyLayout?.visibility = View.GONE
                editTextText = s.toString()
            }
            viewModel.searchDebounce(s?.toString() ?: "")
            crossButton?.visibility = clearButtonVisibility(s)
        }

        override fun afterTextChanged(s: Editable?) {
            editTextText = s.toString()
        }
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }


    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY_MILLIS)
                isClickAllowed = true
            }
        }
        return current
    }

    companion object {
        const val TRACK = "TRACK"

        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L

        fun newInstance() = SearchFragment()
        const val TAG = "SearchFragment"
    }

}


