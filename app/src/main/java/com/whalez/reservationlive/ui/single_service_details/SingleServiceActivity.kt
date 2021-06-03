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
import com.whalez.reservationlive.R
import com.whalez.reservationlive.data.api.ServiceDBClient
import com.whalez.reservationlive.data.api.ServiceDBInterface
import com.whalez.reservationlive.data.repository.NetworkState
import com.whalez.reservationlive.data.repository.ServiceDetailsRepository
import com.whalez.reservationlive.data.vo.service_detail.ServiceDetails
import com.whalez.reservationlive.databinding.ActivitySingleServiceBinding
import com.whalez.reservationlive.util.Utils.Companion.NO_LOCATION_X
import com.whalez.reservationlive.util.Utils.Companion.NO_LOCATION_Y
import com.whalez.reservationlive.util.isDoubleClicked
import java.util.*

class SingleServiceActivity : AppCompatActivity() {

    private lateinit var viewModel: SingleServiceViewModel
    private lateinit var serviceDetailsRepository: ServiceDetailsRepository

    private var xLocation = NO_LOCATION_X
    private var yLocation = NO_LOCATION_Y
    private var address: String? = null

    private var serviceId: String? = null
    private var serviceUrl: String? = null

    private lateinit var binding: ActivitySingleServiceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingleServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        binding.btnViewOnMap.setOnClickListener {
            if (isDoubleClicked()) return@setOnClickListener
            val intent = Intent(this, ViewMapActivity::class.java)
            intent.putExtra("xLocation", xLocation)
            intent.putExtra("yLocation", yLocation)
            intent.putExtra("serviceName", binding.tvServiceName.text)
            intent.putExtra("address", address)
            startActivity(intent)
        }

        serviceId = intent.getStringExtra("id")
        serviceUrl = intent.getStringExtra("serviceUrl")
        if (serviceUrl!!.contains("yeyak.seoul")) {
            serviceUrl = "http://yeyak.seoul.go.kr/mobile/detailView.web?rsvsvcid=$serviceId"
        }

        val apiService: ServiceDBInterface = ServiceDBClient.getClient()
        serviceDetailsRepository = ServiceDetailsRepository(apiService)

        viewModel = getViewModel(serviceId!!)

        viewModel.serviceDetails.observe(this, Observer {
            bindUI(it)
        })

        viewModel.networkState.observe(this, Observer {
            binding.progressBar.visibility = if (it == NetworkState.LOADING) View.VISIBLE else View.GONE
            binding.tvError.visibility = if (it == NetworkState.ERROR) View.VISIBLE else View.GONE
        })

    }

    fun gotoWebsite(view: View) {
        if (serviceUrl == null) {
            Toast.makeText(this, "사이트 링크가 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(serviceUrl))
        startActivity(intent)
    }

    @SuppressLint("SetTextI18n")
    fun bindUI(it: ServiceDetails) {
        if (it.detailList == null) {
            binding.mainLayout.visibility = View.GONE
            binding.noDataLayout.visibility = View.VISIBLE
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
            .into(binding.ivServiceImage)
        with(binding) {
            tvServiceName.text = detail.serviceName
            tvAreaName.text = detail.areaName
            tvPlaceName.text = detail.placeName
            tvCodeName.text = detail.codeName
            tvAddress.text = detail.address

            val serviceStatus = detail.serviceStatus
            tvServiceStatus.text = serviceStatus
            val statusColor = when (serviceStatus) {
                "접수중" -> R.color.reservationAvailable
                "예약일시중지" -> R.color.reservationPaused
                "접수종료" -> R.color.reservationFinished
                else -> R.color.colorAccent
            }
            tvServiceStatus.setTextColor(ContextCompat.getColor(applicationContext, statusColor))

            tvPayAt.text = detail.payAt
            tvAvailableTime.text = "${detail.serviceBegin} ~ ${detail.serviceEnd}"
            tvNotice.text = detail.notice.htmlToString()
            tvDetailContent.text = detail.detailContent.htmlToString()

            if (detail.xLocation.isNotEmpty() && detail.yLocation.isNotEmpty()) {
                xLocation = detail.xLocation.toDouble()
                yLocation = detail.yLocation.toDouble()
            }
            if (detail.address.isNotEmpty()) {
                address = detail.address
            }
        }

    }

    private fun String.htmlToString(): String {
        return if (Build.VERSION.SDK_INT >= 24)
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
