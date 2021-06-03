package com.whalez.reservationlive.ui.service_list.filtered_services

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.whalez.reservationlive.R
import com.whalez.reservationlive.data.vo.service_list.Service
import com.whalez.reservationlive.databinding.ServiceListItemBinding
import com.whalez.reservationlive.ui.single_service_details.SingleServiceActivity
import com.whalez.reservationlive.util.isDoubleClicked
import java.util.*

class AreaFilteredAdapter(private val serviceList: List<Service>, val context: Context, val areaName: String) :
    RecyclerView.Adapter<AreaFilteredAdapter.FilteredViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilteredViewHolder {
        val binding = ServiceListItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return FilteredViewHolder(binding)
    }

    override fun getItemCount(): Int = serviceList.count()

    override fun onBindViewHolder(holder: FilteredViewHolder, position: Int) {
        holder.bind(serviceList[position], context, position)

    }

    inner class FilteredViewHolder(private val binding: ServiceListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(service: Service?, context: Context, position: Int) {
            if (service == null) return
            val serviceImgUrl = service.imageUrl
            val requestOptions = RequestOptions()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.no_image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .signature(ObjectKey(Calendar.DAY_OF_MONTH))
            Glide.with(binding.cardView.context)
                .load(serviceImgUrl)
                .thumbnail(
                    Glide.with(context).load(serviceImgUrl).apply(RequestOptions().override(100))
                )
                .apply(requestOptions)
                .into(binding.cvIvServiceImage)
            binding.apply {
                cvTvServiceName.text = service.serviceName
                cvTvAreaName.text = service.areaName
                cvTvPlaceName.text = service.placeName
                val serviceStatus = service.serviceStatus
                cvTvServiceStatus.text = serviceStatus
                val statusColor = when (serviceStatus) {
                    "접수중" -> R.color.reservationAvailable
                    "예약일시중지" -> R.color.reservationPaused
                    "접수종료" -> R.color.reservationFinished
                    else -> R.color.colorAccent
                }
                cvTvServiceStatus.setTextColor(ContextCompat.getColor(context, statusColor))
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