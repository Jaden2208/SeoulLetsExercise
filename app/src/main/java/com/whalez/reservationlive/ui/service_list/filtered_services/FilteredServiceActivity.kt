package com.whalez.reservationlive.ui.service_list.filtered_services

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.whalez.reservationlive.R
import com.whalez.reservationlive.data.api.ServiceDBClient
import com.whalez.reservationlive.data.api.ServiceDBInterface
import com.whalez.reservationlive.data.vo.service_list.Service
import com.whalez.reservationlive.data.vo.service_list.ServiceResponse
import kotlinx.android.synthetic.main.activity_filtered_service.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FilteredServiceActivity : AppCompatActivity() {

    companion object {
        const val NO_NEED_CODE_IMG = -999
        const val MAX_INDEX = 1000
    }

    lateinit var codeName: String
    lateinit var areaName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filtered_service)

        progress_bar.visibility = View.VISIBLE

        codeName = intent.getStringExtra("codeName")!!
        areaName = intent.getStringExtra("areaName")!!
        setCodeAndArea(areaName)

        val apiService: ServiceDBInterface = ServiceDBClient.getClient()
        apiService.getFilteredServices(codeName).enqueue(object : Callback<ServiceResponse> {
            override fun onFailure(call: Call<ServiceResponse>, t: Throwable) {
                progress_bar.visibility = View.GONE
                tv_error.visibility = View.VISIBLE
            }

            override fun onResponse(
                call: Call<ServiceResponse>,
                response: Response<ServiceResponse>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()
                    body?.let {
                        val filteredServices: MutableList<Service> = ArrayList()
                        for (service in it.listPublicReservationSport.serviceList) {
                            if (service.areaName == areaName) {
                                filteredServices.add(service)
                            }
                        }
                        if (filteredServices.isEmpty()) tv_no_data_msg.visibility = View.VISIBLE
                        else setAdapter(filteredServices)
                        progress_bar.visibility = View.GONE
                    }
                } else {
                    Log.d("kkkResponseCode", response.code().toString())
                    Log.d("kkkResponseMessage", response.message().toString())
                }
            }

        })

    }

    private fun setAdapter(services: List<Service>) {
        val mAdapter = FilteredAdapter(services, this, areaName)
        rv_service_list.adapter = mAdapter
        rv_service_list.layoutManager = LinearLayoutManager(this)
        rv_service_list.setHasFixedSize(false)
    }

    @SuppressLint("SetTextI18n")
    private fun setCodeAndArea(areaName: String) {
        tv_code_name.text = " $codeName"
        tv_areaName.text = areaName
        val codeImage = when (codeName) {
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
        if (codeImage != NO_NEED_CODE_IMG) tv_code_name.setCompoundDrawablesWithIntrinsicBounds(
            codeImage,
            0,
            0,
            0
        )

    }
}
