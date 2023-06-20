package com.practicum.playlist_maker.search.ui.activity


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.practicum.playlist_maker.*
import com.practicum.playlist_maker.player.domain.model.Track
import com.practicum.playlist_maker.player.ui.activity.AudioPlayerActivity
import com.practicum.playlist_maker.search.ui.SearchHistoryAdapter
import com.practicum.playlist_maker.search.ui.SearchState
import com.practicum.playlist_maker.search.ui.TrackAdapter
import com.practicum.playlist_maker.search.ui.TracksClickListener
import com.practicum.playlist_maker.search.ui.view_model.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchActivity : AppCompatActivity() {

    private lateinit var searchEditText: EditText
    private lateinit var crossButton: ImageView
    private lateinit var backButton: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var historyRecyclerView: RecyclerView
    private lateinit var placeholderMessage: TextView
    private lateinit var placeholderImage: ImageView
    private lateinit var refreshButton: Button
    private lateinit var clearHistoryButton: Button
    private lateinit var historyLayout: LinearLayout
    private lateinit var progressBar: ProgressBar

    private val viewModel by viewModel<SearchViewModel>()

    private lateinit var trackAdapter: TrackAdapter
    private lateinit var searchHistoryAdapter: SearchHistoryAdapter


    var editTextText = ""

    private lateinit var handler: Handler

    private var isClickAllowed = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)


        searchEditText = findViewById(R.id.searchEditText)
        crossButton = findViewById(R.id.crossButton)
        backButton = findViewById(R.id.backButton)
        recyclerView = findViewById(R.id.searchRecyclerView)
        historyRecyclerView = findViewById(R.id.historyRecyclerView)
        placeholderMessage = findViewById(R.id.placeholderMessage)
        placeholderImage = findViewById(R.id.placeholderImage)
        refreshButton = findViewById(R.id.refreshButton)
        clearHistoryButton = findViewById(R.id.clearHistoryButton)
        historyLayout = findViewById(R.id.historyLayout)
        progressBar = findViewById(R.id.progressBar)


        val onClickListener = object : TracksClickListener {
            override fun onTrackClick(track: Track) {
                if (clickDebounce()) {
                    val intent = Intent(this@SearchActivity, AudioPlayerActivity::class.java)
                    intent.putExtra(TRACK, Gson().toJson(track))
                    startActivity(intent)

                    viewModel.addTrackToSearchHistory(track)
                    searchHistoryAdapter.tracks = viewModel.getTracksFromSearchHistory()
                }
            }

        }

        trackAdapter = TrackAdapter(onClickListener)
        recyclerView.adapter = trackAdapter

        searchHistoryAdapter = SearchHistoryAdapter(onClickListener)


        searchHistoryAdapter.tracks = viewModel.getTracksFromSearchHistory()
        historyRecyclerView.adapter = searchHistoryAdapter
        if (viewModel.getTracksFromSearchHistory().isEmpty()) historyLayout.visibility = View.GONE

        searchEditText.addTextChangedListener(searchButtonTextWatcher)
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE && editTextText.isNotEmpty()) {
                viewModel.searchDebounce(editTextText)
                recyclerView.visibility = View.VISIBLE
                true
            }
            false
        }
        searchEditText.setOnFocusChangeListener { view, hasFocus ->
            historyLayout.visibility =
                if (hasFocus && editTextText.isEmpty() && viewModel.getTracksFromSearchHistory()
                        .isNotEmpty()
                ) View.VISIBLE else View.GONE
        }

        setOnClickListeners()

        handler = Handler(Looper.getMainLooper())

        viewModel.observeState().observe(this) {
            render(it)
        }

    }

    private fun render(state: SearchState) {
        when (state) {
            is SearchState.Content -> showContent(state.tracks)
            is SearchState.Empty -> showEmpty(state.emptyMessageResId)
            is SearchState.Error -> showError(state.errorMessageResId)
            is SearchState.Loading -> showLoading()
        }
    }

    private fun showContent(tracks: List<Track>) {
        progressBar.visibility = View.GONE
        placeholderMessage.visibility = View.GONE
        placeholderImage.visibility = View.GONE
        refreshButton.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE

        trackAdapter.tracks.clear()
        trackAdapter.tracks.addAll(tracks)
        trackAdapter.notifyDataSetChanged()
    }

    private fun showEmpty(emptyMessageResId: Int) {
        progressBar.visibility = View.GONE
        refreshButton.visibility = View.GONE
        placeholderMessage.visibility = View.VISIBLE
        placeholderImage.visibility = View.VISIBLE

        trackAdapter.tracks.clear()
        trackAdapter.notifyDataSetChanged()

        placeholderMessage.text = getString(emptyMessageResId)
        placeholderImage.setImageResource(R.drawable.nothing_found)
    }

    private fun showError(errorMessageResId: Int) {
        progressBar.visibility = View.GONE
        placeholderMessage.visibility = View.VISIBLE
        placeholderImage.visibility = View.VISIBLE
        refreshButton.visibility = View.VISIBLE

        trackAdapter.tracks.clear()
        trackAdapter.notifyDataSetChanged()

        placeholderMessage.text = getString(errorMessageResId)
        placeholderImage.setImageResource(R.drawable.internet_issues)

    }

    private fun showLoading() {
        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        placeholderMessage.visibility = View.GONE
        placeholderImage.visibility = View.GONE
        refreshButton.visibility = View.GONE
    }

    override fun onStart() {
        super.onStart()
        searchHistoryAdapter.notifyDataSetChanged()
    }

    private fun setOnClickListeners() {
        crossButton.setOnClickListener {
            searchEditText.setText("")
            trackAdapter.tracks.clear()
            trackAdapter.notifyDataSetChanged()
            placeholderMessage.visibility = View.GONE
            placeholderImage.visibility = View.GONE
            refreshButton.visibility = View.GONE
            val inputMethod = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethod.hideSoftInputFromWindow(window.decorView.windowToken, 0)
        }

        backButton.setOnClickListener {
            onBackPressed()
        }

        refreshButton.setOnClickListener {
            refreshButton.visibility = View.GONE
            if (editTextText.isNotEmpty()) viewModel.searchDebounce(editTextText)
        }

        clearHistoryButton.setOnClickListener {
            viewModel.clearSearchHistory()
            searchHistoryAdapter.tracks = ArrayList()
            searchHistoryAdapter.notifyDataSetChanged()
            historyLayout.visibility = View.GONE
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

            if (searchEditText.hasFocus() && s?.isEmpty() == true) {
                if (viewModel.getTracksFromSearchHistory().isNotEmpty())
                    historyLayout.visibility = View.VISIBLE
                else
                    historyLayout.visibility = View.GONE

                viewModel.removeAllCallbacks()


                recyclerView.visibility = View.GONE
                placeholderMessage.visibility = View.GONE
                placeholderImage.visibility = View.GONE
                refreshButton.visibility = View.GONE
            } else {
                historyLayout.visibility = View.GONE
                editTextText = s.toString()
                viewModel.searchDebounce(s?.toString() ?: "")
            }

            crossButton.visibility = clearButtonVisibility(s)
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
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    companion object {
        const val TRACK = "TRACK"

        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

}


