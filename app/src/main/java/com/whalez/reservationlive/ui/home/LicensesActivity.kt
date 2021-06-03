package com.whalez.reservationlive.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.whalez.reservationlive.databinding.ActivityLicensesBinding

class LicensesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLicensesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLicensesBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }
    }
}
