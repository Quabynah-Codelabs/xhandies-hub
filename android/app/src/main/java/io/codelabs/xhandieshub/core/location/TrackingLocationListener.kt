package io.codelabs.xhandieshub.core.location

import android.location.Location


// lat: 5.7562857
// lng: -0.1779957
interface TrackingLocationListener {
    fun onLocationUpdate(location: Location?)
}