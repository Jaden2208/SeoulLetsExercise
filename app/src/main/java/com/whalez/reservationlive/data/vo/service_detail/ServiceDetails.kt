package com.whalez.reservationlive.data.vo.service_detail


import com.google.gson.annotations.SerializedName
import com.whalez.reservationlive.data.vo.service_detail.DetailList

data class ServiceDetails(
    @SerializedName("ListPublicReservationDetail")
    val detailList: DetailList
)