package com.whalez.reservationlive.data.vo.service_list


import com.google.gson.annotations.SerializedName

data class ListPublicReservationSport(
    @SerializedName("list_total_count")
    val listTotalCount: Int,
//    @SerializedName("RESULT")
//    val result: RESULT,
    @SerializedName("row")
    val serviceList: List<Service>
)