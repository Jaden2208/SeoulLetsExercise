package com.whalez.reservationlive.data.vo.service_detail


import com.google.gson.annotations.SerializedName

data class DetailService(
    @SerializedName("ADRES")
    val address: String,
    @SerializedName("AREANM")
    val areaName: String,
//    @SerializedName("CODE")
//    val code: String,
    @SerializedName("CODENM")
    val codeName: String,
    @SerializedName("DTLCONT")
    val detailContent: String,
//    @SerializedName("EXTINFO")
//    val extInfo: String,
//    @SerializedName("FEEGUIDURL")
//    val fEEGUIDURL: String,
    @SerializedName("IMG_PATH")
    val imgPath: String,
    @SerializedName("NOTICE")
    val notice: String,
//    @SerializedName("ONEREQMINPR")
//    val oNEREQMINPR: Double,
//    @SerializedName("ONEREQMXMPR")
//    val oNEREQMXMPR: Double,
//    @SerializedName("ORGNM")
//    val oRGNM: String,
    @SerializedName("PAYAT")
    val payAt: String,
    @SerializedName("PLACENM")
    val placeName: String,
//    @SerializedName("PLACESN")
//    val pLACESN: String,
//    @SerializedName("RCEPTBEGDT")
//    val rCEPTBEGDT: String,
//    @SerializedName("RCEPTENDDT")
//    val receptionEndDate: String,
//    @SerializedName("RCEPTMTH_NM")
//    val rCEPTMTHNM: String,
//    @SerializedName("RCPTBGNDT")
//    val rCPTBGNDT: String,
//    @SerializedName("RCPTENDDT")
//    val rCPTENDDT: String,
//    @SerializedName("RCPTMTHD")
//    val rCPTMTHD: String,
//    @SerializedName("RCRPERCAP")
//    val rCRPERCAP: Double,
//    @SerializedName("REVSTDDAY")
//    val rEVSTDDAY: String,
//    @SerializedName("REVSTDDAYNM")
//    val rEVSTDDAYNM: String,
//    @SerializedName("RSVDAYSTDRCPTDAY")
//    val rSVDAYSTDRCPTDAY: String,
//    @SerializedName("RSVDAYSTDRCPTTIME")
//    val rSVDAYSTDRCPTTIME: String,
//    @SerializedName("SELMTHDCODE")
//    val sELMTHDCODE: String,
//    @SerializedName("SELMTHDCODE_NM")
//    val sELMTHDCODENM: String,
//    @SerializedName("SMCODE")
//    val sMCODE: String,
//    @SerializedName("SMCODE_NM")
//    val sMCODENM: String,
//    @SerializedName("SUBPLACENM")
//    val sUBPLACENM: String,
//    @SerializedName("SVCBEGINDT")
//    val sVCBEGINDT: String,
//    @SerializedName("SVCENDDT")
//    val sVCENDDT: String,
//    @SerializedName("SVCENDTELNO")
//    val sVCENDTELNO: String,
//    @SerializedName("SVCENDUSRSN")
//    val sVCENDUSRSN: String,
    @SerializedName("SVCID")
    val serviceId: String,
    @SerializedName("SVCNM")
    val serviceName: String,
//    @SerializedName("SVCOPNBGNDT")
//    val sVCOPNBGNDT: String,
//    @SerializedName("SVCOPNENDDT")
//    val sVCOPNENDDT: String,
//    @SerializedName("SVCSTTUS")
//    val sVCSTTUS: String,
    @SerializedName("SVCSTTUS_NM")
    val serviceStatus: String,
//    @SerializedName("TELNO")
//    val tELNO: String,
//    @SerializedName("UNICODE_NM")
//    val uNICODENM: String,
//    @SerializedName("UNITCODE")
//    val uNITCODE: String,
//    @SerializedName("USEDAYSTDRCPTDAY")
//    val uSEDAYSTDRCPTDAY: String,
//    @SerializedName("USEDAYSTDRCPTTIME")
//    val uSEDAYSTDRCPTTIME: String,
//    @SerializedName("USELIMMAXNOP")
//    val uSELIMMAXNOP: Double,
//    @SerializedName("USELIMMINNOP")
//    val uSELIMMINNOP: Double,
//    @SerializedName("USETIMEUNITCODE")
//    val uSETIMEUNITCODE: String,
//    @SerializedName("USETIMEUNITCODE_NM")
//    val uSETIMEUNITCODENM: String,
//    @SerializedName("WAITNUM")
//    val wAITNUM: Double,
    @SerializedName("X")
    val xLocation: String,
    @SerializedName("Y")
    val yLocation: String,
    @SerializedName("V_MAX")
    val serviceEnd: String,
    @SerializedName("V_MIN")
    val serviceBegin: String
)