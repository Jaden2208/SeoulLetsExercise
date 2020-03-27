package com.whalez.reservationlive.ui.service_list

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import androidx.lifecycle.LifecycleOwner
import com.skydoves.powermenu.MenuAnimation
import com.skydoves.powermenu.PowerMenu
import com.skydoves.powermenu.PowerMenuItem
import com.skydoves.powermenu.kotlin.createPowerMenu
import com.whalez.reservationlive.R

class MenuFactory: PowerMenu.Factory() {

    companion object { // 메뉴 옵션
        const val RESERVATION_AVAILABLE = 0
        const val FILTER_OPTION = 1
        const val APP_INFO = 2
    }

    override fun create(context: Context, lifecycle: LifecycleOwner): PowerMenu {
        return createPowerMenu(context) {
            addItem(PowerMenuItem("접수 가능 시설", false))
            addItem(PowerMenuItem("필터 옵션", false))
            addItem(PowerMenuItem("앱 정보", false))
            /* PowerMenu methods link
             * https://github.com/skydoves/PowerMenu#powermenu-methods
             */
            setAutoDismiss(true)
            setLifecycleOwner(lifecycle)
            setAnimation(MenuAnimation.SHOWUP_TOP_RIGHT)
            setMenuRadius(10f)
            setMenuShadow(10f)
            setTextColorResource(R.color.colorPrimaryDark)
            setTextSize(15)
            setTextGravity(Gravity.CENTER)
            setTextTypeface(Typeface.create("sans-serif-light", Typeface.BOLD))
            setSelectedTextColor(Color.WHITE)
            setMenuColor(Color.WHITE)
            setSelectedMenuColorResource(R.color.colorPrimary)
            setPreferenceName("BasicMenu")
        }
    }
}