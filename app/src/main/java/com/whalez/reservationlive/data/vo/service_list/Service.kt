package com.whalez.reservationlive.data.vo.service_list


import com.google.gson.annotations.SerializedName

data class Service(
    @SerializedName("AREANM")
    val areaName: String,
//    @SerializedName("DTLCONT")
//    val dTLCONT: String,
//    @SerializedName("GUBUN")
//    val gUBUN: String,
    @SerializedName("IMGURL")
    val imageUrl: String,
//    @SerializedName("MAXCLASSNM")
//    val mAXCLASSNM: String,
//    @SerializedName("MINCLASSNM")
//    val codeName: String,
//    @SerializedName("PAYATNM")
//    val pAYATNM: String,
    @SerializedName("PLACENM")
    val placeName: String,
//    @SerializedName("RCPTBGNDT")
//    val rCPTBGNDT: String,
//    @SerializedName("RCPTENDDT")
//    val rCPTENDDT: String,
//    @SerializedName("REVSTDDAY")
//    val rEVSTDDAY: String,
//    @SerializedName("REVSTDDAYNM")
//    val rEVSTDDAYNM: String,
    @SerializedName("SVCID")
    val serviceId: String,
    @SerializedName("SVCNM")
    val serviceName: String,
//    @SerializedName("SVCOPNBGNDT")
//    val sVCOPNBGNDT: String,
//    @SerializedName("SVCOPNENDDT")
//    val sVCOPNENDDT: String,
    @SerializedName("SVCSTATNM")
    val serviceStatus: String,
    @SerializedName("SVCURL")
    val serviceUrl: String
//    @SerializedName("TELNO")
//    val tELNO: String,
//    @SerializedName("USETGTINFO")
//    val uSETGTINFO: String,
//    @SerializedName("V_MAX")
//    val vMAX: String,
//    @SerializedName("V_MIN")
//    val vMIN: String,
//    @SerializedName("X")
//    val x: String,
//    @SerializedName("Y")
//    val y: String
)