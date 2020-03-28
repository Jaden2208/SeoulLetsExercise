package com.whalez.reservationlive.ui

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import androidx.annotation.RequiresApi
import com.whalez.reservationlive.ui.home.HomeActivity

class SplashActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        SystemClock.sleep(1000)
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

}
