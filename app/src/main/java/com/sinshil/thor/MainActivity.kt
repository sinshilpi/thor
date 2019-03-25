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
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBar
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.google.android.gms.maps.model.CircleOptions


class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupDrawerLayout()
        showToolbar()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap

        googleMap.setOnMyLocationButtonClickListener(onMyLocationButtonClickListener)
        googleMap.setOnMyLocationClickListener(onMyLocationClickListener)
        enableMyLocationsIfPermitted()

        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.setMinZoomPreference(11f)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    drawerLayout.openDrawer(GravityCompat.START)
                }

                true
            }
            else ->  super.onOptionsItemSelected(item)
        }
    }

    private fun showToolbar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val actionBar: ActionBar? = supportActionBar
        actionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp)
        }
    }

    private fun setupDrawerLayout() {
        drawerLayout = findViewById(R.id.drawer_container)

        val navigationView: NavigationView = findViewById(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            drawerLayout.closeDrawers()
            true
        }
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
