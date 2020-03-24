package com.whalez.reservationlive.data.vo.service_list


import com.google.gson.annotations.SerializedName

data class ServiceResponse(
    @SerializedName("ListPublicReservationSport")
    val listPublicReservationSport: ListPublicReservationSport
)