package com.whalez.reservationlive.ui.single_service_details

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.whalez.reservationlive.util.Utils.Companion.SEOUL_LOCATION_X
import com.whalez.reservationlive.util.Utils.Companion.SEOUL_LOCATION_Y
import com.whalez.reservationlive.R
import com.whalez.reservationlive.data.api.ServiceDBClient
import com.whalez.reservationlive.data.api.ServiceDBInterface
import com.whalez.reservationlive.data.repository.NetworkState
import com.whalez.reservationlive.data.repository.ServiceDetailsRepository
import com.whalez.reservationlive.data.vo.service_detail.ServiceDetails
import kotlinx.android.synthetic.main.activity_single_service.*
import java.util.*

class SingleServiceActivity : AppCompatActivity() {

    private lateinit var viewModel: SingleServiceViewModel
    private lateinit var serviceDetailsRepository: ServiceDetailsRepository

    private var xLocation: Double = SEOUL_LOCATION_X
    private var yLocation: Double = SEOUL_LOCATION_Y


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_service)

        btn_back.setOnClickListener { finish() }

        btn_view_on_map.setOnClickListener {
            val intent = Intent(this, ViewMapActivity::class.java)
            intent.putExtra("xLocation", xLocation)
            intent.putExtra("yLocation", yLocation)
            intent.putExtra("serviceName", tv_service_name.text)
            startActivity(intent)
        }

        val serviceId = intent.getStringExtra("id")
        val serviceUrl = intent.getStringExtra("serviceUrl")

        val apiService: ServiceDBInterface = ServiceDBClient.getClient()
        serviceDetailsRepository = ServiceDetailsRepository(apiService)

        viewModel = getViewModel(serviceId!!)

        viewModel.serviceDetails.observe(this, Observer {
            bindUI(it, serviceUrl)
        })

        viewModel.networkState.observe(this, Observer {
            progress_bar.visibility = if (it == NetworkState.LOADING) View.VISIBLE else View.GONE
            tv_error.visibility = if (it == NetworkState.ERROR) View.VISIBLE else View.GONE
        })


    }

    @SuppressLint("SetTextI18n")
    fun bindUI(it: ServiceDetails, serviceUrl: String?) {
        // null 이 뜨는 경우가 분명히 있음 지우면 안됨.

        if(it.detailList == null) {
            main_layout.visibility = View.GONE
            no_data_layout.visibility = View.VISIBLE
            btn_temp_goto_website.setOnClickListener {
                if(serviceUrl == null){
                    Toast.makeText(this, "사이트 링크가 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(serviceUrl))
                startActivity(intent)
            }
            return
        }

        val detail = it.detailList.detailMovieList[0]
        val serviceImgUrl = detail.imgPath
        val requestOptions = RequestOptions()
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.no_image)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .dontAnimate()
            .signature(ObjectKey(Calendar.DAY_OF_MONTH))
        Glide.with(this)
            .load(serviceImgUrl)
            .thumbnail(Glide.with(this).load(serviceImgUrl).apply(RequestOptions().override(100)))
            .apply(requestOptions)
            .into(iv_service_image)
        tv_service_name.text = detail.serviceName
        tv_area_name.text = detail.areaName
        tv_place_name.text = detail.placeName
        tv_code_name.text = detail.codeName
        tv_address.text = detail.address

        val serviceStatus = detail.serviceStatus
        tv_service_status.text = serviceStatus
        val statusColor = when(serviceStatus){
            "접수중" -> R.color.reservationAvailable
            "예약일시중지" -> R.color.reservationPaused
            "접수종료" -> R.color.reservationFinished
            else -> R.color.colorAccent
        }
        tv_service_status.setTextColor(ContextCompat.getColor(applicationContext, statusColor))

        tv_pay_at.text = detail.payAt
        tv_available_time.text = "${detail.serviceBegin} ~ ${detail.serviceEnd}"
        tv_notice.text = detail.notice.htmlToString()
        tv_detail_content.text = detail.detailContent.htmlToString()

        xLocation = detail.xLocation.toDouble()
        yLocation = detail.yLocation.toDouble()
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
