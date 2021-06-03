package com.whalez.reservationlive.ui.service_list

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.whalez.reservationlive.R
import com.whalez.reservationlive.data.repository.NetworkState
import com.whalez.reservationlive.data.vo.service_list.Service
import com.whalez.reservationlive.databinding.NetworkStateItemBinding
import com.whalez.reservationlive.databinding.ServiceListItemBinding
import com.whalez.reservationlive.ui.single_service_details.SingleServiceActivity
import com.whalez.reservationlive.util.isDoubleClicked
import java.util.*

class ServicePagedListAdapter(private val context: Context) :
    PagingDataAdapter<Service, RecyclerView.ViewHolder>(ServiceDiffCallback()) {

    companion object {
        const val SERVICE_VIEW_TYPE = 1
        const val NETWORK_VIEW_TYPE = 2
    }

    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return if (viewType == SERVICE_VIEW_TYPE) {
            val binding = ServiceListItemBinding.inflate(layoutInflater, parent, false)
            ServiceItemViewHolder(binding)
        } else {
            val binding = NetworkStateItemBinding.inflate(layoutInflater, parent, false)
            NetworkStateItemViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == SERVICE_VIEW_TYPE) {
            (holder as ServiceItemViewHolder).bind(getItem(position), context)
        } else {
            (holder as NetworkStateItemViewHolder).bind(networkState)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            NETWORK_VIEW_TYPE
        } else {
            SERVICE_VIEW_TYPE
        }
    }

    class ServiceDiffCallback : DiffUtil.ItemCallback<Service>() {
        override fun areItemsTheSame(oldItem: Service, newItem: Service): Boolean {
            return oldItem.serviceId == newItem.serviceId
        }

        override fun areContentsTheSame(oldItem: Service, newItem: Service): Boolean {
            return oldItem == newItem
        }

    }

    inner class ServiceItemViewHolder (private val binding: ServiceListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(service: Service?, context: Context) {
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
                .thumbnail(Glide.with(context).load(serviceImgUrl).apply(RequestOptions().override(100)))
                .apply(requestOptions)
                .into(binding.cvIvServiceImage)
            binding.apply {
                cvTvServiceName.text = service.serviceName
                cvTvAreaName.text = service.areaName
                cvTvPlaceName.text = service.placeName
                val serviceStatus = service.serviceStatus
                cvTvServiceStatus.text = serviceStatus
                val statusColor = when(serviceStatus){
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

    class NetworkStateItemViewHolder(private val binding: NetworkStateItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(networkState: NetworkState?) {
            if (networkState != null && networkState == NetworkState.LOADING) {
                binding.progressBarItem.visibility = View.VISIBLE
            } else {
                binding.progressBarItem.visibility = View.GONE
            }

            if (networkState != null && (networkState == NetworkState.ERROR || networkState == NetworkState.ENDOFLIST)) {
                binding.errorMsgItem.visibility = View.VISIBLE
                binding.errorMsgItem.text = networkState.msg
            }  else {
                binding.errorMsgItem.visibility = View.GONE
            }
        }
    }

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    fun setNetworkState(newNetworkState: NetworkState) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()

        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

}