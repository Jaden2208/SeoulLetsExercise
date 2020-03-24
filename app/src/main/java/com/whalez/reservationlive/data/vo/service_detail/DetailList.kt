package com.whalez.reservationlive.data.vo.service_detail


import com.google.gson.annotations.SerializedName

data class DetailList(
    @SerializedName("list_total_count")
    val listTotalCount: Int,
//    @SerializedName("RESULT")
//    val result: RESULT,
    @SerializedName("row")
    val detailMovieList: List<DetailService>
)