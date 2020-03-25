package com.whalez.reservationlive.ui.service_list

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.whalez.reservationlive.R
import com.whalez.reservationlive.data.repository.NetworkState
import com.whalez.reservationlive.data.vo.service_list.Service
import com.whalez.reservationlive.ui.single_service_details.SingleServiceActivity
import kotlinx.android.synthetic.main.network_state_item.view.*
import kotlinx.android.synthetic.main.service_list_item.view.*

class ServicePagedListAdapter(private val context: Context) :
    PagedListAdapter<Service, RecyclerView.ViewHolder>(ServiceDiffCallback()) {

    val SERVICE_VIEW_TYPE = 1
    val NETWORK_VIEW_TYPE = 2

    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View

        return if (viewType == SERVICE_VIEW_TYPE) {
            view = layoutInflater.inflate(R.layout.service_list_item, parent, false)
            ServiceItemViewHolder(view)
        } else {
            view = layoutInflater.inflate(R.layout.network_state_item, parent, false)
            NetworkStateItemViewHolder(view)
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

    class ServiceItemViewHolder (view: View) : RecyclerView.ViewHolder(view) {

        companion object{
            const val CLICK_TIME_INTERVAL = 500
        }
        private var mLastClickTime = System.currentTimeMillis()


        fun bind(service: Service?, context: Context) {
            if (service == null) return
            val serviceImgUrl = service.imageUrl
            Glide.with(itemView.context)
                .load(serviceImgUrl)
                .placeholder(R.drawable.image_placeholder)
                .into(itemView.cv_iv_service_image)
            itemView.apply {
                cv_tv_service_name.text = service.serviceName
                cv_tv_area_name.text = service.areaName
                cv_tv_place_name.text = service.placeName
                val serviceStatus = service.serviceStatus
                cv_tv_service_status.text = serviceStatus
                val statusColor = when(serviceStatus){
                    "접수중" -> R.color.reservationAvailable
                    "예약일시중지" -> R.color.reservationPaused
                    "접수종료" -> R.color.reservationFinished
                    else -> R.color.colorAccent
                }
                cv_tv_service_status.setTextColor(ContextCompat.getColor(context, statusColor))
            }

            itemView.setOnClickListener {
                val clickedTime = System.currentTimeMillis()
                Log.d("kkk clickedTime", clickedTime.toString())
                if(clickedTime - mLastClickTime < CLICK_TIME_INTERVAL) return@setOnClickListener
                mLastClickTime = clickedTime

                val intent = Intent(context, SingleServiceActivity::class.java)
                intent.putExtra("id", service.serviceId)
                intent.putExtra("serviceUrl", service.serviceUrl)
                context.startActivity(intent)
            }
        }
    }

    class NetworkStateItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(networkState: NetworkState?) {
            if (networkState != null && networkState == NetworkState.LOADING) {
                itemView.progress_bar_item.visibility = View.VISIBLE
            } else {
                itemView.progress_bar_item.visibility = View.GONE
            }

            if (networkState != null && networkState == NetworkState.ERROR) {
                itemView.error_msg_item.visibility = View.VISIBLE
                itemView.error_msg_item.text = networkState.msg
            } else if (networkState != null && networkState == NetworkState.ENDOFLIST) {
                itemView.error_msg_item.visibility = View.VISIBLE
                itemView.error_msg_item.text = networkState.msg
            } else {
                itemView.error_msg_item.visibility = View.GONE
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