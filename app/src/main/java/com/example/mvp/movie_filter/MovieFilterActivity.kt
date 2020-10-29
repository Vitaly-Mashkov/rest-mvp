package com.example.mvp.movie_filter

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.mvp.R
import com.example.mvp.utils.Constants
import com.google.android.material.snackbar.Snackbar
import java.util.*

class MovieFilterActivity : AppCompatActivity() {
    private var tvFromReleaseDate: TextView? = null
    private var tvToReleaseDate: TextView? = null
    private var tvClerAll: TextView? = null
    private var fromDate = ""
    private var toDate = ""
    private var rlMainLayout: RelativeLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_filter)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        initUI()
        setListeners()
//        val mIntent = intent
        fromDate = intent.getStringExtra(Constants.KEY_RELEASE_FROM)!!
        toDate = intent.getStringExtra(Constants.KEY_RELEASE_TO)!!

        // If the data from import is not empty then set it release from and release to values
        if (!fromDate.isEmpty() && !toDate.isEmpty()) {
            tvFromReleaseDate!!.text = fromDate
            tvToReleaseDate!!.text = toDate
        }
    }

    private fun initUI() {
        tvFromReleaseDate = findViewById(R.id.tv_from_date)
        tvToReleaseDate = findViewById(R.id.tv_to_date)
        tvClerAll = findViewById(R.id.tv_clear_all)
        rlMainLayout = findViewById(R.id.rl_main_layout)
    }

    private fun setListeners() {
        tvFromReleaseDate!!.setOnClickListener {
            val now = Calendar.getInstance()

            val dpd = DatePickerDialog(this, {
                datePicker, year, month, dayOfMonth ->
                val date = year.toString() + "-" + String.format("%02d", month + 1) + "-" + String.format("%02d", dayOfMonth)
                        fromDate = date
                        tvFromReleaseDate!!.text = date
            },now[Calendar.YEAR],
                    now[Calendar.MONTH],
                    now[Calendar.DAY_OF_MONTH])
            dpd.show()
        }
        tvToReleaseDate!!.setOnClickListener {
            val now = Calendar.getInstance()
            val dpd = DatePickerDialog(this,
                    {datePicker, year, monthOfYear, dayOfMonth ->
                        val date = year.toString() + "-" + String.format("%02d", monthOfYear + 1) + "-" + String.format("%02d", dayOfMonth)
                        toDate = date
                        tvToReleaseDate!!.text = date
                    },
                    now[Calendar.YEAR],
                    now[Calendar.MONTH],
                    now[Calendar.DAY_OF_MONTH]
            )
            dpd.show()
        }
        tvClerAll!!.setOnClickListener {
            fromDate = ""
            toDate = ""
            tvFromReleaseDate!!.text = getString(R.string.from)
            tvToReleaseDate!!.text = getString(R.string.to)
        }
        tvToReleaseDate!!.addTextChangedListener(twDates)
        tvFromReleaseDate!!.addTextChangedListener(twDates)
    }

    /**
     * Handling the text change listeners on dates to hide/show clear all button.
     */
    private val twDates: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
        override fun afterTextChanged(editable: Editable) {
            if (!fromDate.isEmpty() || !toDate.isEmpty()) {
                tvClerAll!!.visibility = View.VISIBLE
            } else if (fromDate.isEmpty() && toDate.isEmpty()) {
                tvClerAll!!.visibility = View.GONE
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.filter_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.item_done) {
            if (fromDate.isEmpty() || toDate.isEmpty()) {
                Snackbar.make(rlMainLayout!!, getString(R.string.error_date_filter
                ),
                        Snackbar.LENGTH_LONG).show()
                return true
            }
            val returnIntent = Intent()
            returnIntent.putExtra(Constants.KEY_RELEASE_FROM, fromDate)
            returnIntent.putExtra(Constants.KEY_RELEASE_TO, toDate)
            setResult(RESULT_OK, returnIntent)
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}