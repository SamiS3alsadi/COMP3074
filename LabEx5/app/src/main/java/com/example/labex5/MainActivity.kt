package com.example.labex5

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.labex5.ui.theme.LabEx5Theme

/**
 * Lab 5 - shows the current latitude and longitude, read from the GPS.
 *
 * The activity owns the permission request and the lifetime of the GPS
 * updates; the screen is a plain function of [LocationUiState].
 */
class MainActivity : ComponentActivity() {

    private lateinit var gps: GpsLocationSource

    private var uiState by mutableStateOf(LocationUiState())

    /**
     * Both permissions are asked for together. Android 12 and later let the
     * user grant approximate location only, which answers FINE as denied, so
     * the result is read per permission rather than as a single yes or no.
     */
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { granted ->
        val fine = granted[Manifest.permission.ACCESS_FINE_LOCATION] == true
        uiState = uiState.copy(
            permissionGranted = fine,
            permissionDeniedOnce = !fine
        )
        if (fine) startUpdates()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        gps = GpsLocationSource(this)

        setContent {
            LabEx5Theme {
                LocationScreen(
                    state = uiState,
                    onRequestPermission = {
                        permissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    },
                    onOpenLocationSettings = {
                        startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                    },
                    onOpenAppSettings = {
                        startActivity(
                            Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                android.net.Uri.fromParts("package", packageName, null)
                            )
                        )
                    }
                )
            }
        }
    }

    /**
     * Permission and the GPS switch are both re-read here, because the user can
     * change either one in Settings while the app sits in the background.
     */
    override fun onResume() {
        super.onResume()
        val granted = gps.hasFinePermission()
        uiState = uiState.copy(
            permissionGranted = granted,
            gpsEnabled = gps.isGpsEnabled()
        )
        if (granted) startUpdates()
    }

    /** Updates stop with the screen: a GPS left running is a flat battery. */
    override fun onPause() {
        super.onPause()
        gps.stop()
    }

    private fun startUpdates() {
        gps.start { fix ->
            uiState = uiState.copy(fix = fix, gpsEnabled = true)
        }
    }
}
