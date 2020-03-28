package com.whalez.reservationlive.util

import com.whalez.reservationlive.util.Utils.Companion.CLICK_TIME_INTERVAL

class Utils {

    companion object {
        const val CLICK_TIME_INTERVAL = 500
        const val SEOUL_LOCATION_X = 127.0016985
        const val SEOUL_LOCATION_Y = 37.5642135
    }

}

private var mLastClickTime = System.currentTimeMillis()
fun isDoubleClicked(): Boolean{
    val clickedTime = System.currentTimeMillis()
    if(clickedTime - mLastClickTime < CLICK_TIME_INTERVAL) return true
    mLastClickTime = clickedTime
    return false
}