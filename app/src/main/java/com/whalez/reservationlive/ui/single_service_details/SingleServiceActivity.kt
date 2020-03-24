package com.whalez.reservationlive.ui.single_service_details

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.whalez.reservationlive.R
import com.whalez.reservationlive.data.api.ServiceDBClient
import com.whalez.reservationlive.data.api.ServiceDBInterface
import com.whalez.reservationlive.data.repository.NetworkState
import com.whalez.reservationlive.data.repository.ServiceDetailsRepository
import com.whalez.reservationlive.data.vo.service_detail.ServiceDetails
import kotlinx.android.synthetic.main.activity_single_service.*

class SingleServiceActivity : AppCompatActivity() {

    private lateinit var viewModel: SingleServiceViewModel
    private lateinit var serviceDetailsRepository: ServiceDetailsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_service)

        val serviceId = intent.getStringExtra("id")

        val apiService: ServiceDBInterface = ServiceDBClient.getClient()
        serviceDetailsRepository = ServiceDetailsRepository(apiService)

        viewModel = getViewModel(serviceId!!)

        viewModel.serviceDetails.observe(this, Observer {
            bindUI(it)
        })

        viewModel.networkState.observe(this, Observer {
            progress_bar.visibility = if (it == NetworkState.LOADING) View.VISIBLE else View.GONE
            tv_error.visibility = if (it == NetworkState.ERROR) View.VISIBLE else View.GONE
        })

    }

    @SuppressLint("SetTextI18n")
    fun bindUI(it: ServiceDetails) {
        val detail = it.detailList.detailMovieList[0]

        val serviceImageURL = detail.imgPath
        Glide.with(this)
            .load(serviceImageURL)
            .into(iv_service_image)

        tv_service_name.text = detail.serviceName
        tv_area_name.text = detail.areaName
        tv_place_name.text = detail.placeName
        tv_code_name.text = detail.codeName
        tv_address.text = detail.address
        tv_service_status.text = detail.serviceStatus
        tv_pay_at.text = detail.payAt
        tv_available_time.text = "${detail.serviceBegin} ~ ${detail.serviceEnd}"
        tv_reception_end_date.text = "~${detail.receptionEndDate}"
        tv_notice.text = detail.notice.htmlToString()
        tv_detail_content.text = detail.detailContent.htmlToString()
    }

    private fun String.htmlToString() : String {
        return if(Build.VERSION.SDK_INT >= 24)
            Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY).toString()
        else Html.fromHtml(this).toString()
    }

    private fun getViewModel(serviceId: String): SingleServiceViewModel {
        return ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return SingleServiceViewModel(serviceDetailsRepository, serviceId) as T
            }
        })[SingleServiceViewModel::class.java]
    }
}
