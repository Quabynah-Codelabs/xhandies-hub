package io.codelabs.xhandieshub.core.location

import android.location.Location

interface TrackingLocationListener {
    fun onLocationUpdate(location: Location?)
}