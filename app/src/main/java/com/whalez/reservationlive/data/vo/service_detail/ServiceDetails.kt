package com.whalez.reservationlive.data.vo.service_detail


import com.google.gson.annotations.SerializedName

data class ServiceDetails(
    @SerializedName("ListPublicReservationDetail")
    val detailList: DetailList?
)