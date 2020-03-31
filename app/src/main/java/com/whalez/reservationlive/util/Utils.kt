package com.whalez.reservationlive.util

import com.whalez.reservationlive.util.Utils.Companion.CLICK_TIME_INTERVAL

class Utils {

    companion object {
        const val CLICK_TIME_INTERVAL = 500
        const val NO_LOCATION_X = 0.0
        const val NO_LOCATION_Y = 0.0
    }

}

var mLastClickTime: Long = 0
fun isDoubleClicked(): Boolean {
    val clickedTime = System.currentTimeMillis()
    if (clickedTime - mLastClickTime < CLICK_TIME_INTERVAL) return true
    mLastClickTime = clickedTime
    return false
}