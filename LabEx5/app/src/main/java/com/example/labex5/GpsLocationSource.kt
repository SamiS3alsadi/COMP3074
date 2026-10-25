package com.example.labex5

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.core.content.ContextCompat

/**
 * Thin wrapper around the platform [LocationManager], fixed to the GPS provider.
 *
 * The lab asks for the position to come from the GPS, so this deliberately does
 * not use the fused provider, which can answer from Wi-Fi or the cell network
 * without the GPS radio ever being used.
 */
class GpsLocationSource(private val context: Context) {

    private val locationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    /** Smallest interval between updates, in milliseconds. */
    private val minTimeMs = 1_000L

    /** Smallest movement before a new update is delivered, in metres. */
    private val minDistanceM = 0f

    private var listener: LocationListener? = null

    /** True once the user has granted ACCESS_FINE_LOCATION. */
    fun hasFinePermission(): Boolean =
        ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED

    /** True when the GPS provider is switched on in system settings. */
    fun isGpsEnabled(): Boolean =
        locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

    /**
     * Starts GPS updates and reports every reading to [onFix].
     *
     * The last known fix, if the system still holds one, is delivered first so
     * the screen has something to show while the radio gets its own fix.
     * Does nothing without the permission; the caller checks that first.
     */
    fun start(onFix: (GpsFix) -> Unit) {
        if (!hasFinePermission()) return
        stop()

        val newListener = object : LocationListener {
            override fun onLocationChanged(location: Location) = onFix(location.toGpsFix())

            // Required by the interface on API levels below 30.
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) = Unit

            override fun onProviderEnabled(provider: String) = Unit

            override fun onProviderDisabled(provider: String) = Unit
        }
        listener = newListener

        // Permission is checked above, but the compiler cannot see that across
        // the call, so the SecurityException is caught rather than suppressed.
        try {
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                ?.let { onFix(it.toGpsFix()) }

            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                minTimeMs,
                minDistanceM,
                newListener
            )
        } catch (e: SecurityException) {
            listener = null
        }
    }

    /** Stops updates. Called when the screen is no longer in the foreground. */
    fun stop() {
        listener?.let { locationManager.removeUpdates(it) }
        listener = null
    }

    private fun Location.toGpsFix() = GpsFix(
        latitude = latitude,
        longitude = longitude,
        accuracyMetres = accuracy,
        timestampMillis = time
    )
}
