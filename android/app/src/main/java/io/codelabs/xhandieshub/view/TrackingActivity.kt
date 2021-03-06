package io.codelabs.xhandieshub.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import io.codelabs.sdk.util.network.Outcome
import io.codelabs.sdk.util.toast
import io.codelabs.xhandieshub.R
import io.codelabs.xhandieshub.core.base.BaseActivity
import io.codelabs.xhandieshub.core.debugger
import io.codelabs.xhandieshub.core.location.GPSTracker
import io.codelabs.xhandieshub.core.location.MapApi
import io.codelabs.xhandieshub.core.location.MapService
import io.codelabs.xhandieshub.core.location.TrackingLocationListener
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import java.util.*

class TrackingActivity : BaseActivity(), OnMapReadyCallback {
    private val mapApi by inject<MapApi>()
    private val mapService by inject<MapService>()

    private lateinit var tracker: GPSTracker
    private var line: Polyline? = null


    private var mMap: GoogleMap? = null
    private var hasLocationPermission: Boolean = false
    private val permissions = mutableListOf<String>().apply {
        add(Manifest.permission.ACCESS_FINE_LOCATION)
        add(Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracking)

        hasLocationPermission = EasyPermissions.hasPermissions(
            this,
            permissions[0], permissions[1]
        )
        if (!hasLocationPermission) requestLocationPermission()
        setupMap()

        // Tracker
        tracker = GPSTracker(this@TrackingActivity, /*object : TrackingLocationListener {
            override fun onLocationUpdate(location: Location?) {
                if (location == null) return
                mMap?.clear()
                debugger("${location.latitude} && ${location.longitude}")

                // Other position
                val otherPosition = LatLng(5.6501627, -0.1962279)
//                val otherPosition = LatLng(5.7562857, -0.1779957)

                // Re-map user's location
                val currentPos = LatLng(location.latitude, location.longitude)
                mMap?.addMarker(MarkerOptions().position(currentPos).title("Your current position"))
                mMap?.addMarker(MarkerOptions().position(otherPosition).title("Tracked products"))
                mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPos, 15.0f))

                ioScope.launch {
                    with(Geocoder(this@TrackingActivity, Locale.getDefault())) {
                        try {
                            val address =
                                this.getFromLocation(
                                    tracker.latitude,
                                    tracker.longitude,
                                    2
                                )[0].getAddressLine(0)

                            debugger("Origin Address: $address")

                            uiScope.launch {
                                mapApi.getDistance(
                                    origin = "Legon Boundary, Accra, Ghana", // todo: remove this hard coded location in the future
//                               origin = "${tracker.latitude}, ${tracker.longitude}",
                                    destination = address
//                                destination = "${otherPosition.latitude}, ${otherPosition.longitude}"
                                ).observe(this@TrackingActivity, Observer { outcome ->
                                    when (outcome) {
                                        is Outcome.Success -> {
                                            if (outcome.data.routes.isNotEmpty()) {
                                                val routes = outcome.data.routes
                                                try {
                                                    line?.remove()
                                                    for (i in 0 until routes.size) {
                                                        val distance =
                                                            routes[i].legs[i].distance.text
                                                        val duration =
                                                            routes[i].legs[i].duration.text
                                                        debugger(
                                                            String.format(
                                                                "Distance: %s & Duration: %s",
                                                                distance,
                                                                duration
                                                            )
                                                        )


                                                        // Draw the polyline
                                                        val encodedString =
                                                            routes[i].overviewPolyline.points
                                                        val poly =
                                                            mapService.decodePoly(encodedString)

                                                        val options = PolylineOptions()
//                                                            .addAll(poly)
                                                            .add(
                                                                LatLng(
                                                                    tracker.latitude,
                                                                    tracker.longitude
                                                                )
                                                                , otherPosition
                                                            )
                                                            .width(20.0f)
                                                            .color(
                                                                ContextCompat.getColor(
                                                                    this@TrackingActivity,
                                                                    R.color.colorAccent
                                                                )
                                                            )
                                                            .geodesic(true)

                                                        line = mMap?.addPolyline(options)
                                                    }
                                                } catch (e: Exception) {
                                                    debugger(e.localizedMessage)
                                                }
                                            } else {
                                                debugger("no routes found")
                                            }
                                        }

                                        is Outcome.Progress -> {
                                            // Loading location
                                            debugger("Loading location")
                                        }

                                        is Outcome.ApiError, is Outcome.Failure -> {
                                            // error occurred
                                            debugger("An error occurred while loading location")
                                        }
                                    }
                                })
                            }

                        } catch (e: Exception) {
                            debugger(e.localizedMessage)
                            uiScope.launch {
                                toast("Cannot find target destination")
                            }
                        }
                    }
                }
            }
        }*/ null)

        mMap?.clear()
        debugger("${tracker.latitude} && ${tracker.longitude}")

        // Other position
        val otherPosition = LatLng(5.6554933,-0.1837714)

        // Re-map user's location
        val currentPos = LatLng(tracker.latitude, tracker.longitude)
        mMap?.addMarker(MarkerOptions().position(currentPos).title("Your current position"))
        mMap?.addMarker(MarkerOptions().position(otherPosition).title("Tracked products"))
        mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPos, 15.0f))

        ioScope.launch {
            with(Geocoder(this@TrackingActivity, Locale.getDefault())) {
                try {
                    val address =
                        this.getFromLocation(
                            tracker.latitude,
                            tracker.longitude,
                            2
                        )[0].getAddressLine(0)

                    debugger("Origin Address: $address")

                    uiScope.launch {
                        mapApi.getDistance(
                            origin = address, // todo: remove this hard coded location in the future
//                               origin = "${tracker.latitude}, ${tracker.longitude}",
                            destination = /*address*/ "Legon%20Boundary%2C%20Accra%2C%20Ghana"
//                                destination = "${otherPosition.latitude}, ${otherPosition.longitude}"
                        ).observe(this@TrackingActivity, Observer { outcome ->
                            when (outcome) {
                                is Outcome.Success -> {
                                    if (outcome.data.routes.isNotEmpty()) {
                                        val routes = outcome.data.routes
                                        try {
                                            line?.remove()
                                            for (i in 0 until routes.size) {
                                                val distance =
                                                    routes[i].legs[i].distance.text
                                                val duration =
                                                    routes[i].legs[i].duration.text
                                                debugger(
                                                    String.format(
                                                        "Distance: %s & Duration: %s",
                                                        distance,
                                                        duration
                                                    )
                                                )


                                                // Draw the polyline
                                                val encodedString =
                                                    routes[i].overviewPolyline.points
                                                val poly =
                                                    mapService.decodePoly(encodedString)

                                                val options = PolylineOptions()
                                                  .addAll(poly)
                                                   /* .add(
                                                        LatLng(
                                                            tracker.latitude,
                                                            tracker.longitude
                                                        )
                                                        , otherPosition
                                                    )*/
                                                    .width(20.0f)
                                                    .color(
                                                        ContextCompat.getColor(
                                                            this@TrackingActivity,
                                                            R.color.colorAccent
                                                        )
                                                    )
                                                    .geodesic(true)

                                                line = mMap?.addPolyline(options)
                                            }
                                        } catch (e: Exception) {
                                            debugger(e.localizedMessage)
                                        }
                                    } else {
                                        debugger("no routes found")
                                    }
                                }

                                is Outcome.Progress -> {
                                    // Loading location
                                    debugger("Loading location")
                                }

                                is Outcome.ApiError, is Outcome.Failure -> {
                                    // error occurred
                                    debugger("An error occurred while loading location")
                                }
                            }
                        })
                    }

                } catch (e: Exception) {
                    debugger(e.localizedMessage)
                    uiScope.launch {
                        toast("Cannot find target destination")
                    }
                }
            }
        }
    }

    private fun requestLocationPermission() {
        EasyPermissions.requestPermissions(
            this,
            getString(R.string.loc_permission),
            RC_LOC,
            permissions[0], permissions[1]
        )
    }

    @SuppressLint("MissingPermission")
    @AfterPermissionGranted(RC_LOC)
    private fun setupMap() {
        if (hasLocationPermission) {
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this)
        } else requestLocationPermission()
    }


    override fun onMapReady(googleMap: GoogleMap) {
        // Setup map style
        mMap = googleMap
        mMap?.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.custom_map_style))
        mMap?.isMyLocationEnabled = true
        mMap?.isBuildingsEnabled = true

        val currentPos = LatLng(tracker.latitude, tracker.longitude)
        mMap?.addMarker(MarkerOptions().position(currentPos).title("Your current position"))
        mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPos, 15.0f))

        // Re-route user with new location
        mMap?.setOnMyLocationClickListener {
            tracker.updateLocation(it)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults)
        hasLocationPermission =
            grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
        if (hasLocationPermission) setupMap()
    }

    companion object {
        private const val RC_LOC = 8
    }
}
