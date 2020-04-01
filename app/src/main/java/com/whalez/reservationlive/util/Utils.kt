package com.whalez.reservationlive.util

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import com.whalez.reservationlive.R
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

fun basicAlertDialog(context: Context, title: String, message: String): AlertDialog? {
    return AlertDialog.Builder(
        ContextThemeWrapper(
            context,
            R.style.Theme_AppCompat_Light_Dialog
        )
    ).apply {
        setTitle(title)
        setMessage(message)
        setPositiveButton("확인"){_, _ ->  }
    }.show()
}