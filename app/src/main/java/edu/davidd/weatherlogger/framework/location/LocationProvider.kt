package edu.davidd.weatherlogger.framework.location

import android.annotation.SuppressLint
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import timber.log.Timber

interface LocationProvider {
    fun request(
        onLocationAvailable: (LocationData) -> Unit,
        onLostAvailability: () -> Unit
    )

    fun reset()
}

class LocationProviderImpl(private val client: FusedLocationProviderClient) : LocationProvider {

    private var callback: LocationCallback? = null

    @SuppressLint("MissingPermission")
    override fun request(onLocationAvailable: (LocationData) -> Unit, onLostAvailability: () -> Unit) {
        client.requestLocationUpdates(
            LocationHandlerForSettings.createLocationRequest(),
            LocationCallbackImpl(onLocationAvailable, onLostAvailability).also { callback = it },
            Looper.getMainLooper()
        )
    }

    override fun reset() {
        callback?.let { client.removeLocationUpdates(it) }
    }
}

private class LocationCallbackImpl(
    private val onLocationAvailable: (LocationData) -> Unit,
    private val onLostAvailability: () -> Unit
) : LocationCallback() {

    override fun onLocationResult(locationResult: LocationResult?) {
        locationResult?.locations?.forEachIndexed { index, location ->
            Timber.d("onLocationResult - $index $location")
            onLocationAvailable(LocationData(location.latitude, location.longitude))
        } ?: Timber.w("onLocationResult - result is null")
    }

    override fun onLocationAvailability(p0: LocationAvailability) {
        Timber.d("onLocationAvailability - $p0")
        if (!p0.isLocationAvailable) onLostAvailability()
    }
}