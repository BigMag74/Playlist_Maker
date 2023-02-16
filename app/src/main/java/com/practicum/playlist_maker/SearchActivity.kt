package com.practicum.playlist_maker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView

class SearchActivity : AppCompatActivity() {
    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
    }

    var editTextText = ""

    lateinit var searchEditText: EditText

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

        searchEditText = findViewById<EditText>(R.id.searchEditText)
        val crossButton = findViewById<ImageView>(R.id.crossButton)
        val backButton = findViewById<ImageView>(R.id.backButton)

        val searchButtonTextWatcher = object : TextWatcher {
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

        searchEditText.addTextChangedListener(searchButtonTextWatcher)

        crossButton.setOnClickListener {
            searchEditText.setText("")
        }

        backButton.setOnClickListener {
            onBackPressed()
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
