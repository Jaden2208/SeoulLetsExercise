package com.whalez.reservationlive.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageInfo
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.whalez.reservationlive.BuildConfig
import com.whalez.reservationlive.R
import com.whalez.reservationlive.databinding.ActivityHomeBinding
import com.whalez.reservationlive.ui.service_list.ServiceListActivity
import com.whalez.reservationlive.util.isDoubleClicked

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAppInfo.setOnClickListener {
            binding.bottomAppInfo.apply {
                val packageInfo = packageManager.getPackageInfo(packageName, 0)
                tvAppVersion.text = "${appVersionCode(packageInfo)}.${appVersionName(packageInfo)}"
                tvMinSdkVersion.text = "Android api ${minSdkVersion()} 이상"
                showBottomSheet(appInfoLayout)
            }
        }
        binding.btnLicenses.setOnClickListener {
            if (isDoubleClicked()) return@setOnClickListener
            val intent = Intent(this, LicensesActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showBottomSheet(layout: LinearLayout) {
        val sheetBehavior = BottomSheetBehavior.from(layout)
        if (sheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        } else {
            sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    fun onClickCode(view: View) {
        if (isDoubleClicked()) return

        val codeName = when (view.id) {
            R.id.football -> "축구"
            R.id.futsal -> "풋살"
            R.id.baseball -> "야구"
            R.id.basketball -> "농구"
            R.id.tennis -> "테니스"
            R.id.badminton -> "배드민턴"
            R.id.volleyball -> "배구"
            R.id.multipurpose -> "다목적경기장"
            R.id.jokgu -> "족구"
            R.id.allcodes -> "전체"
            else -> return
        }
        val intent = Intent(this, ServiceListActivity::class.java)
        intent.putExtra("codeName", codeName)
        startActivity(intent)
    }

    private fun appVersionCode(packageInfo: PackageInfo): Long {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageInfo.longVersionCode
        } else {
            packageInfo.versionCode.toLong()
        }
    }

    private fun appVersionName(packageInfo: PackageInfo): String {
        return packageInfo.versionName
    }

    private fun minSdkVersion(): Int {
        return BuildConfig.MIN_SDK_VERSION
    }

}
