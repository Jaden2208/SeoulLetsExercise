package com.whalez.reservationlive.ui.single_service_details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.whalez.reservationlive.util.Utils.Companion.SEOUL_LOCATION_X
import com.whalez.reservationlive.util.Utils.Companion.SEOUL_LOCATION_Y
import com.whalez.reservationlive.R
import kotlinx.android.synthetic.main.activity_view_map.*
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

class ViewMapActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_map)

        val permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
        ActivityCompat.requestPermissions(this, permissions,0)

        btn_back.setOnClickListener { finish() }

        val xLocation = intent.getDoubleExtra("xLocation", SEOUL_LOCATION_X)
        val yLocation = intent.getDoubleExtra("yLocation", SEOUL_LOCATION_Y)
        val serviceName = intent.getStringExtra("serviceName")

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
}
