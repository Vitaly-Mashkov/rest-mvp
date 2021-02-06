package com.example.mvp.movie_filter

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.mvp.R
import com.example.mvp.utils.Constants
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_movie_filter.*
import java.util.*

class MovieFilterActivity : AppCompatActivity() {

    private var fromDate = ""
    private var toDate = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_filter)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
        }

        setListeners()
//        val mIntent = intent
        fromDate = intent.getStringExtra(Constants.KEY_RELEASE_FROM)!!
        toDate = intent.getStringExtra(Constants.KEY_RELEASE_TO)!!

        if (fromDate.isNotEmpty() && toDate.isNotEmpty()) {
            tv_from_date.text = fromDate
            tv_to_date.text = toDate
        }
    }

    private fun setListeners() {
        tv_from_date.setOnClickListener {
            val now = Calendar.getInstance()
            val dpd = DatePickerDialog(this, {
                    _, year, month, dayOfMonth ->
                val date = year.toString() + "-" + String.format("%02d", month + 1) + "-" + String.format("%02d", dayOfMonth)
                        fromDate = date
                        tv_from_date.text = date
            }, now[Calendar.YEAR],
                    now[Calendar.MONTH],
                    now[Calendar.DAY_OF_MONTH])
            dpd.show()
        }
        tv_to_date.setOnClickListener {
            val now = Calendar.getInstance()
            val dpd = DatePickerDialog(this,
                    { _, year, monthOfYear, dayOfMonth ->
                        val date = year.toString() + "-" + String.format("%02d", monthOfYear + 1) + "-" + String.format("%02d", dayOfMonth)
                        toDate = date
                        tv_to_date.text = date
                    },
                    now[Calendar.YEAR],
                    now[Calendar.MONTH],
                    now[Calendar.DAY_OF_MONTH]
            )
            dpd.show()
        }
        tv_clear_all.setOnClickListener {
            fromDate = ""
            toDate = ""
            tv_from_date.text = getString(R.string.from)
            tv_to_date.text = getString(R.string.to)
        }

        tv_from_date.addTextChangedListener(twDates)
        tv_to_date.addTextChangedListener(twDates)
    }

    private val twDates: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
        override fun afterTextChanged(editable: Editable) {
            if (fromDate.isNotEmpty() || toDate.isNotEmpty()) {
                tv_clear_all.visibility = View.VISIBLE
            } else if (fromDate.isEmpty() && toDate.isEmpty()) {
                tv_clear_all.visibility = View.GONE
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
                Snackbar.make(rl_main_layout, getString(R.string.error_date_filter
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