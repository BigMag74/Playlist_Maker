package com.practicum.playlist_maker


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
    }

    private val itunesBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesService = retrofit.create(ItunesApi::class.java)
    private val tracks = ArrayList<Track>()
    val adapter = TrackAdapter()

    var editTextText = ""

    private val searchButtonTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(
            s: CharSequence?,
            start: Int,
            count: Int,
            after: Int
        ) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            crossButton.visibility = clearButtonVisibility(s)
            editTextText = s.toString()
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    private lateinit var searchEditText: EditText
    private lateinit var crossButton: ImageView
    private lateinit var backButton: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var placeholderMessage: TextView
    private lateinit var placeholderImage: ImageView
    private lateinit var refreshButton: Button

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, editTextText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchEditText.setText(savedInstanceState.getString(SEARCH_TEXT, ""))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchEditText = findViewById(R.id.searchEditText)
        crossButton = findViewById(R.id.crossButton)
        backButton = findViewById(R.id.backButton)
        recyclerView = findViewById(R.id.searchRecyclerView)
        placeholderMessage = findViewById(R.id.placeholderMessage)
        placeholderImage = findViewById(R.id.placeholderImage)
        refreshButton = findViewById(R.id.refreshButton)

        adapter.tracks = tracks
        recyclerView.adapter = adapter

        setOnClickListeners()

        searchEditText.addTextChangedListener(searchButtonTextWatcher)
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search()
                true
            }
            false
        }
    }

    private fun setOnClickListeners() {
        crossButton.setOnClickListener {
            searchEditText.setText("")
            tracks.clear()
            adapter.notifyDataSetChanged()
            placeholderMessage.visibility = View.GONE
            placeholderImage.visibility = View.GONE
            refreshButton.visibility = View.GONE
        }

        backButton.setOnClickListener {
            onBackPressed()
        }

        refreshButton.setOnClickListener {
            refreshButton.visibility = View.GONE
            search()
        }
    }

    private fun search() {

        if (editTextText.isNotEmpty()) {
            itunesService.getTrack(editTextText).enqueue(object : Callback<TrackResponse> {
                override fun onResponse(
                    call: Call<TrackResponse>,
                    response: Response<TrackResponse>
                ) {
                    if (response.code() == 200) {
                        tracks.clear()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            tracks.addAll(response.body()?.results!!)
                            adapter.notifyDataSetChanged()
                        }
                        if (tracks.isEmpty()) {
                            showMessage(
                                Message.EMPTY,
                                getString(R.string.nothing_found),
                                ""
                            )
                        } else {
                            showMessage(Message.SUCCESS, "", "")
                        }
                    } else {
                        showMessage(
                            Message.ERROR,
                            getString(R.string.internet_issues),
                            response.code().toString()
                        )
                    }
                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    showMessage(
                        Message.ERROR,
                        getString(R.string.internet_issues),
                        t.message.toString()
                    )
                }
            })
        }
    }

    enum class Message {
        SUCCESS,
        ERROR,
        EMPTY
    }

    private fun showMessage(message: Message, text: String, additionalMessage: String) {
        when (message) {
            Message.SUCCESS -> {
                placeholderMessage.visibility = View.GONE
                placeholderImage.visibility = View.GONE
                refreshButton.visibility = View.GONE
            }
            Message.EMPTY -> {
                if (text.isNotEmpty()) {
                    refreshButton.visibility = View.GONE

                    placeholderMessage.visibility = View.VISIBLE
                    placeholderImage.visibility = View.VISIBLE

                    tracks.clear()
                    adapter.notifyDataSetChanged()

                    placeholderMessage.text = text
                    placeholderImage.setImageResource(R.drawable.nothing_found)

                    if (additionalMessage.isNotEmpty()) {
                        Toast.makeText(applicationContext, additionalMessage, Toast.LENGTH_LONG)
                            .show()
                    }

                }
            }
            Message.ERROR -> {
                if (text.isNotEmpty()) {
                    placeholderMessage.visibility = View.VISIBLE
                    placeholderImage.visibility = View.VISIBLE
                    refreshButton.visibility = View.VISIBLE

                    tracks.clear()
                    adapter.notifyDataSetChanged()

                    placeholderMessage.text = text
                    placeholderImage.setImageResource(R.drawable.internet_issues)

                    if (additionalMessage.isNotEmpty()) {
                        Toast.makeText(applicationContext, additionalMessage, Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }

        }
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

}


