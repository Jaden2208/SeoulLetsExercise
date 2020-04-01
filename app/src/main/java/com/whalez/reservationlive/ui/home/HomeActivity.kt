package com.whalez.reservationlive.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageInfo
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.formats.MediaView
import com.google.android.gms.ads.formats.NativeAdOptions
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.android.gms.ads.formats.UnifiedNativeAdView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.whalez.reservationlive.BuildConfig
import com.whalez.reservationlive.R
import com.whalez.reservationlive.ui.service_list.ServiceListActivity
import com.whalez.reservationlive.util.isDoubleClicked
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.bottom_app_info.view.*


class HomeActivity : AppCompatActivity() {

    companion object {
        const val ADMOB_AD_UNIT_ID = R.string.ad_unit_id_for_test
    }

    private var nativeAd: UnifiedNativeAd? = null

    override fun onResume() {
        super.onResume()
        showAd()
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        MobileAds.initialize(this) { }

        btn_app_info.setOnClickListener {
            val appInfoLayout = findViewById<LinearLayout>(R.id.app_info_layout)
            val packageInfo = packageManager.getPackageInfo(packageName, 0)

            appInfoLayout.tv_app_version.text =
                "${appVersionCode(packageInfo)}.${appVersionName(packageInfo)}"
            appInfoLayout.tv_min_sdk_version.text = "Android api ${minSdkVersion()} 이상"
            showBottomSheet(appInfoLayout)
        }
        btn_licenses.setOnClickListener {
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
            R.id.football -> "축구장"
            R.id.futsal -> "풋살장"
            R.id.baseball -> "야구장"
            R.id.basketball -> "농구장"
            R.id.tennis -> "테니스장"
            R.id.badminton -> "배드민턴장"
            R.id.volleyball -> "배구장"
            R.id.multipurpose -> "다목적경기장"
            R.id.allcodes -> "전체"
            else -> return
        }
        val intent = Intent(this, ServiceListActivity::class.java)
        intent.putExtra("codeName", codeName)
        startActivity(intent)
    }

    private fun populateUnifiedNativeAdView(
        nativeAd: UnifiedNativeAd,
        adView: UnifiedNativeAdView
    ) {
        // Set the media view. Media content will be automatically populated in the media view once
        // adView.setNativeAd() is called.
        val mediaView: MediaView = adView.findViewById(R.id.ad_media)
        adView.mediaView = mediaView

        // Set other ad assets.
        adView.headlineView = adView.findViewById(R.id.ad_headline)
        adView.bodyView = adView.findViewById(R.id.ad_body)
        adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
        adView.iconView = adView.findViewById(R.id.ad_app_icon)
        adView.priceView = adView.findViewById(R.id.ad_price)
        adView.starRatingView = adView.findViewById(R.id.ad_stars)
        adView.storeView = adView.findViewById(R.id.ad_store)
        adView.advertiserView = adView.findViewById(R.id.ad_advertiser)

        // The headline is guaranteed to be in every UnifiedNativeAd.
        (adView.headlineView as TextView).text = nativeAd.headline

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.body == null) {
            adView.bodyView.visibility = View.INVISIBLE
        } else {
            adView.bodyView.visibility = View.VISIBLE
            (adView.bodyView as TextView).text = nativeAd.body
        }
        if (nativeAd.callToAction == null) {
            adView.callToActionView.visibility = View.INVISIBLE
        } else {
            adView.callToActionView.visibility = View.VISIBLE
            (adView.callToActionView as Button).text = nativeAd.callToAction
        }
        if (nativeAd.icon == null) {
            adView.iconView.visibility = View.GONE
        } else {
            (adView.iconView as ImageView).setImageDrawable(
                nativeAd.icon.drawable
            )
            adView.iconView.visibility = View.VISIBLE
        }
        if (nativeAd.price == null) {
            adView.priceView.visibility = View.INVISIBLE
        } else {
            adView.priceView.visibility = View.VISIBLE
            (adView.priceView as TextView).text = nativeAd.price
        }
        if (nativeAd.store == null) {
            adView.storeView.visibility = View.INVISIBLE
        } else {
            adView.storeView.visibility = View.VISIBLE
            (adView.storeView as TextView).text = nativeAd.store
        }
        if (nativeAd.starRating == null) {
            adView.starRatingView.visibility = View.INVISIBLE
        } else {
            (adView.starRatingView as RatingBar).rating = nativeAd.starRating.toFloat()
            adView.starRatingView.visibility = View.VISIBLE
        }
        if (nativeAd.advertiser == null) {
            adView.advertiserView.visibility = View.INVISIBLE
        } else {
            (adView.advertiserView as TextView).text = nativeAd.advertiser
            adView.advertiserView.visibility = View.VISIBLE
        }
    }


    private fun showAd() {
        val builder = AdLoader.Builder(this, getString(ADMOB_AD_UNIT_ID))
        builder.forUnifiedNativeAd { unifiedNativeAd ->
            if (nativeAd != null) {
                nativeAd!!.destroy()
            }
            nativeAd = unifiedNativeAd
            val frameLayout: FrameLayout = findViewById(R.id.fl_adplaceholder)

            @SuppressLint("InflateParams")
            val adView = layoutInflater
                .inflate(R.layout.adview_layout, null) as UnifiedNativeAdView
            populateUnifiedNativeAdView(unifiedNativeAd, adView)
            frameLayout.removeAllViews()
            frameLayout.addView(adView)
        }
        val adOptions = NativeAdOptions.Builder().build()
        builder.withNativeAdOptions(adOptions)
        val adLoader = builder.withAdListener(object : AdListener() {
            override fun onAdFailedToLoad(errorCode: Int) {
                Log.e("onAdFailedToLoad", "Failed to load native ad: $errorCode")
            }
        }).build()
        adLoader.loadAd(AdRequest.Builder().build())
    }

    override fun onDestroy() {
        if (nativeAd != null) nativeAd!!.destroy()
        super.onDestroy()
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
//
//    private fun setLeft(text: String): String{
//        return "<div style=\"float:left;\">$text</div>"
//    }

}
