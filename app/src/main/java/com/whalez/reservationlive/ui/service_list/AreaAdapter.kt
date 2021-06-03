package com.whalez.reservationlive.ui.service_list

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.whalez.reservationlive.databinding.AreaItemBinding
import com.whalez.reservationlive.ui.service_list.filtered_services.AreaFilteredActivity
import com.whalez.reservationlive.util.isDoubleClicked

class AreaAdapter(private val context: Context, private val codeName: String): RecyclerView.Adapter<AreaAdapter.GuViewHolder>() {

    private val areaList: List<String> = arrayListOf(
        "강남구", "강동구", "강북구", "강서구", "관악구", "광진구", "구로구", "금천구", "노원구",
        "도봉구", "동대문구", "동작구", "마포구", "서대문구", "서초구", "성동구", "성북구",
        "송파구", "양천구", "영등포구", "용산구", "은평구", "종로구", "중구", "중랑구"
    )
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuViewHolder {
        val binding = AreaItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return GuViewHolder(binding)
    }

    override fun getItemCount(): Int = areaList.size

    override fun onBindViewHolder(holder: GuViewHolder, position: Int) {
        holder.bind(areaList[position])
    }

    inner class GuViewHolder(private val binding: AreaItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(areaName: String){
            binding.tvGu.text = areaName

            itemView.setOnClickListener {

                if(isDoubleClicked()) return@setOnClickListener

                val intent = Intent(context, AreaFilteredActivity::class.java)
                intent.putExtra("areaName", areaName)
                intent.putExtra("codeName", codeName)
                context.startActivity(intent)
            }
        }
    }
}