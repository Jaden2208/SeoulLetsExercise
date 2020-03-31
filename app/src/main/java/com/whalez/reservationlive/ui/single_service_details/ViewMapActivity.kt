package com.whalez.reservationlive.ui.single_service_details

import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import com.whalez.reservationlive.R
import com.whalez.reservationlive.util.Utils.Companion.NO_LOCATION_X
import com.whalez.reservationlive.util.Utils.Companion.NO_LOCATION_Y
import kotlinx.android.synthetic.main.activity_view_map.*
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapReverseGeoCoder
import net.daum.mf.map.api.MapView
import java.lang.Exception

class ViewMapActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_map)

        val permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
        ActivityCompat.requestPermissions(this, permissions, 0)

        btn_back.setOnClickListener { finish() }

        val address = intent.getStringExtra("address")
        var xLocation = intent.getDoubleExtra("xLocation", NO_LOCATION_X)
        var yLocation= intent.getDoubleExtra("yLocation", NO_LOCATION_Y)
        val serviceName = intent.getStringExtra("serviceName")

        if ((xLocation == NO_LOCATION_X || yLocation == NO_LOCATION_Y) && !address.isNullOrEmpty()) {
            var place: List<Address>? = null
            try {
                place = Geocoder(this).getFromLocationName(address, 1)
            } catch (e: Exception) {
                Log.e("kkk", e.message.toString())
            }
            if (place != null && place.isNotEmpty()) {
                xLocation = place[0].latitude
                yLocation = place[0].longitude
            }
        }

        Log.d("kkkX", xLocation.toString())
        Log.d("kkkY", yLocation.toString())

        if (xLocation != NO_LOCATION_X && yLocation != NO_LOCATION_Y) {
            setMapView(serviceName!!, xLocation, yLocation)
        } else {
            setError()
        }


    }

    private fun setMapView(serviceName: String, xLocation: Double, yLocation: Double) {
        val mapView = MapView(this)
        map_view.addView(mapView)
        val marker = MapPOIItem()
        val servicePoint = MapPoint.mapPointWithGeoCoord(xLocation, yLocation)

        marker.apply {
            itemName = serviceName
            tag = 0
            mapPoint = servicePoint
            markerType = MapPOIItem.MarkerType.BluePin
            selectedMarkerType = MapPOIItem.MarkerType.RedPin
        }
        mapView.apply {
            addPOIItem(marker)
            setMapCenterPoint(servicePoint, false)
        }
    }

    private fun setError() {
        tv_error.visibility = View.VISIBLE
        map_view.visibility = View.GONE
    }
}
