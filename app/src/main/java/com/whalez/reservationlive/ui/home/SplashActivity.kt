package com.whalez.reservationlive.ui.home

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import androidx.annotation.RequiresApi
import com.whalez.reservationlive.R

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

}
