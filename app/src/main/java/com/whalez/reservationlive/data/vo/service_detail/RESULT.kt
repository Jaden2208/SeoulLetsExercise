package com.whalez.reservationlive.data.vo.service_detail


import com.google.gson.annotations.SerializedName

data class RESULT(
    @SerializedName("CODE")
    val cODE: String,
    @SerializedName("MESSAGE")
    val mESSAGE: String
)