package com.whalez.reservationlive.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.whalez.reservationlive.R
import com.whalez.reservationlive.ui.service_list.MainActivity
import com.whalez.reservationlive.util.Utils
import com.whalez.reservationlive.util.isDoubleClicked

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }

    fun onClickCode(view: View) {
        if(isDoubleClicked()) return

        val codeName = when(view.id){
            R.id.football -> "축구장"
            R.id.futsal -> "풋살장"
            R.id.baseball -> "야구장"
            R.id.basketball -> "농구장"
            R.id.tennis -> "테니스장"
            R.id.badminton -> "배드민턴장"
            R.id.volleyball -> "배구장"
            R.id.multipurpose -> "다목적경기장"
            R.id.allcodes -> "전체"
            else -> return
        }
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("codeName", codeName)
        startActivity(intent)
    }
}
