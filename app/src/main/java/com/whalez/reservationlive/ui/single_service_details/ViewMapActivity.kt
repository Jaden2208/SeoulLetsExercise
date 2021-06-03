package com.whalez.reservationlive.ui.single_service_details

import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import com.whalez.reservationlive.databinding.ActivityViewMapBinding
import com.whalez.reservationlive.util.Utils.Companion.NO_LOCATION_X
import com.whalez.reservationlive.util.Utils.Companion.NO_LOCATION_Y
import com.whalez.reservationlive.util.basicAlertDialog
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import java.lang.Exception

class ViewMapActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewMapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
        ActivityCompat.requestPermissions(this, permissions, 0)

        binding.btnBack.setOnClickListener { finish() }

        val address = intent.getStringExtra("address")
        var xLocation = intent.getDoubleExtra("xLocation", NO_LOCATION_X)
        var yLocation = intent.getDoubleExtra("yLocation", NO_LOCATION_Y)
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
            basicAlertDialog(this,
                title = "주의",
                message = "정확한 좌표 정보가 존재하지 않아 위치가 정확하지 않을 수 있습니다."
            )
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
        binding.mapView.addView(mapView)
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
        binding.tvError.visibility = View.VISIBLE
        binding.mapView.visibility = View.GONE
    }
}
