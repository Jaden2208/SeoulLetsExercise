package com.whalez.reservationlive.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.whalez.reservationlive.R
import kotlinx.android.synthetic.main.activity_licenses.*

class LicensesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_licenses)

        btn_back.setOnClickListener { finish() }
    }
}
