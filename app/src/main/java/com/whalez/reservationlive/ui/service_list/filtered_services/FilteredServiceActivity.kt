package com.whalez.reservationlive.ui.service_list.filtered_services

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.whalez.reservationlive.R
import kotlinx.android.synthetic.main.activity_filtered_service.*

class FilteredServiceActivity : AppCompatActivity() {

    companion object{
        const val NO_NEED_CODE_IMG = -999
    }

    lateinit var codeName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filtered_service)

        codeName = intent.getStringExtra("codeName")!!
        val areaName = intent.getStringExtra("areaName")
        setCodeAndArea(areaName!!)


    }

    @SuppressLint("SetTextI18n")
    private fun setCodeAndArea(areaName: String) {
        tv_code_name.text = " $codeName"
        tv_areaName.text = areaName
        val codeImage = when(codeName){
            "축구장" -> R.drawable.ic_football_white
            "풋살장" -> R.drawable.ic_futsal_white
            "야구장" -> R.drawable.ic_baseball_white
            "농구장" -> R.drawable.ic_basketball_white
            "테니스장" -> R.drawable.ic_tennis_white
            "배드민턴장" -> R.drawable.ic_badminton_white
            "배구장" -> R.drawable.ic_volleyball_white
            "다목적경기장" -> R.drawable.ic_multipurpose_white
            "전체" -> {
                codeName = ""
                NO_NEED_CODE_IMG
            }
            else -> NO_NEED_CODE_IMG
        }
        if(codeImage != NO_NEED_CODE_IMG) tv_code_name.setCompoundDrawablesWithIntrinsicBounds(codeImage, 0, 0, 0)

    }
}
