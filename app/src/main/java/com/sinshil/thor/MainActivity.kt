package com.sinshil.thor

import android.Manifest
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import android.graphics.Color
import android.location.Location
import com.google.android.gms.maps.model.CircleOptions


class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap

        googleMap.setOnMyLocationButtonClickListener(onMyLocationButtonClickListener)
        googleMap.setOnMyLocationClickListener(onMyLocationClickListener)
        enableMyLocationsIfPermitted()

        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.setMinZoomPreference(11f)
    }

    private fun enableMyLocationsIfPermitted() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE)
        } else if (googleMap != null) {
            googleMap.isMyLocationEnabled = true
        }
    }

    private var onMyLocationButtonClickListener = GoogleMap.OnMyLocationButtonClickListener {
        googleMap.setMinZoomPreference(15f)
        false
    }

    private var onMyLocationClickListener = object : GoogleMap.OnMyLocationClickListener {
        override fun onMyLocationClick(location: Location) {
            googleMap.setMinZoomPreference(12f)

            val circleOptions = CircleOptions().center(LatLng(location.latitude, location.longitude))
            circleOptions.radius(200.0)
            circleOptions.fillColor(Color.RED)
            circleOptions.strokeWidth(6f)

            googleMap.addCircle(circleOptions)
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}
