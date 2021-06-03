package com.whalez.reservationlive.ui.service_list.filtered_services

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.whalez.reservationlive.R
import com.whalez.reservationlive.data.api.ServiceDBClient
import com.whalez.reservationlive.data.api.ServiceDBInterface
import com.whalez.reservationlive.data.vo.service_list.Service
import com.whalez.reservationlive.data.vo.service_list.ServiceResponse
import com.whalez.reservationlive.databinding.ActivityFilteredServiceBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AreaFilteredActivity : AppCompatActivity() {

    companion object {
        const val NO_NEED_CODE_IMG = -999
        const val MAX_INDEX = 1000
    }

    lateinit var codeName: String
    lateinit var areaName: String

    private lateinit var binding: ActivityFilteredServiceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilteredServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressBar.visibility = View.VISIBLE

        codeName = intent.getStringExtra("codeName")!!
        areaName = intent.getStringExtra("areaName")!!
        setCodeAndArea(areaName)

        binding.btnBack.setOnClickListener { finish() }

        val apiService: ServiceDBInterface = ServiceDBClient.getClient()
        apiService.getFilteredServices(codeName).enqueue(object : Callback<ServiceResponse> {
            override fun onFailure(call: Call<ServiceResponse>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                binding.tvError.visibility = View.VISIBLE
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
                        if (filteredServices.isEmpty()) binding.tvNoDataMsg.visibility = View.VISIBLE
                        else setAdapter(filteredServices)
                        binding.progressBar.visibility = View.GONE
                    }
                } else {
                    Log.d("kkkResponseCode", response.code().toString())
                    Log.d("kkkResponseMessage", response.message().toString())
                }
            }

        })

    }

    private fun setAdapter(services: List<Service>) {
        val mAdapter = AreaFilteredAdapter(services, this, areaName)
        binding.rvServiceList.adapter = mAdapter
        binding.rvServiceList.layoutManager = LinearLayoutManager(this)
        binding.rvServiceList.setHasFixedSize(false)
    }

    @SuppressLint("SetTextI18n")
    private fun setCodeAndArea(areaName: String) {
        binding.tvCodeName.text = " $codeName"
        binding.tvAreaName.text = areaName
        val codeIcon: Int
        val codeImage: Int
        when(codeName){
            "축구" -> {
                codeIcon = R.drawable.ic_football_white
                codeImage = R.drawable.soccer_logo
            }
            "풋살" -> {
                codeIcon = R.drawable.ic_futsal_white
                codeImage = R.drawable.futsal_logo
            }
            "야구" -> {
                codeIcon = R.drawable.ic_baseball_white
                codeImage = R.drawable.baseball_logo
            }
            "농구" -> {
                codeIcon = R.drawable.ic_basketball_white
                codeImage = R.drawable.basketball_logo
            }
            "테니스" -> {
                codeIcon = R.drawable.ic_tennis_white
                codeImage = R.drawable.tennis_logo
            }
            "배드민턴" -> {
                codeIcon = R.drawable.ic_badminton_white
                codeImage = R.drawable.badminton_logo
            }
            "배구" -> {
                codeIcon = R.drawable.ic_volleyball_white
                codeImage = R.drawable.volleyball_logo
            }
            "다목적경기" -> {
                codeIcon = R.drawable.ic_multipurpose_white
                codeImage = R.drawable.multipurpose_logo
            }
            "족구" -> {
                codeIcon = R.drawable.ic_jokgu_white
                codeImage = R.drawable.jokgu_logo
            }
            "전체" -> {
                codeName = ""
                codeIcon = NO_NEED_CODE_IMG
                codeImage = R.drawable.only_players
            }
            else -> {
                codeIcon = NO_NEED_CODE_IMG
                codeImage = R.drawable.only_players
            }
        }
        if(codeIcon != NO_NEED_CODE_IMG) binding.tvCodeName.setCompoundDrawablesWithIntrinsicBounds(codeIcon, 0, 0, 0)
        Glide.with(this.applicationContext)
            .load(codeImage)
            .placeholder(R.drawable.placeholder)
            .into(binding.ivSportImg)

    }
}
