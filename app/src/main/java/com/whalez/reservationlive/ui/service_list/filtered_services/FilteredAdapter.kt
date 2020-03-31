package com.whalez.reservationlive.ui.service_list.filtered_services

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.whalez.reservationlive.R
import com.whalez.reservationlive.data.vo.service_list.Service
import com.whalez.reservationlive.ui.single_service_details.SingleServiceActivity
import com.whalez.reservationlive.util.Utils
import com.whalez.reservationlive.util.Utils.Companion.CLICK_TIME_INTERVAL
import com.whalez.reservationlive.util.isDoubleClicked
import com.whalez.reservationlive.util.mLastClickTime
import kotlinx.android.synthetic.main.service_list_item.view.*
import java.util.*

class FilteredAdapter(private val serviceList: List<Service>, val context: Context, val areaName: String) :
    RecyclerView.Adapter<FilteredAdapter.FilteredViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilteredViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.service_list_item, parent, false)
        return FilteredViewHolder(view)
    }

    override fun getItemCount(): Int = serviceList.count()

    override fun onBindViewHolder(holder: FilteredViewHolder, position: Int) {
        holder.bind(serviceList[position], context, position)

    }

    inner class FilteredViewHolder(view: View) : RecyclerView.ViewHolder(view) {

//        private var mLastClickTime = System.currentTimeMillis()

        fun bind(service: Service?, context: Context, position: Int) {
            if (service == null) return
            val serviceImgUrl = service.imageUrl
            val requestOptions = RequestOptions()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.no_image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .signature(ObjectKey(Calendar.DAY_OF_MONTH))
            Glide.with(itemView.context)
                .load(serviceImgUrl)
                .thumbnail(
                    Glide.with(context).load(serviceImgUrl).apply(RequestOptions().override(100))
                )
                .apply(requestOptions)
                .into(itemView.cv_iv_service_image)
            itemView.apply {
                cv_tv_service_name.text = service.serviceName
                cv_tv_area_name.text = service.areaName
                cv_tv_place_name.text = service.placeName
                val serviceStatus = service.serviceStatus
                cv_tv_service_status.text = serviceStatus
                val statusColor = when (serviceStatus) {
                    "접수중" -> R.color.reservationAvailable
                    "예약일시중지" -> R.color.reservationPaused
                    "접수종료" -> R.color.reservationFinished
                    else -> R.color.colorAccent
                }
                cv_tv_service_status.setTextColor(ContextCompat.getColor(context, statusColor))
            }

            itemView.setOnClickListener {
                if (isDoubleClicked()) return@setOnClickListener
                val intent = Intent(context, SingleServiceActivity::class.java)
                intent.putExtra("id", service.serviceId)
                intent.putExtra("serviceUrl", service.serviceUrl)
                context.startActivity(intent)
            }
        }

    }
}