package com.example.labex5

/**
 * Everything the screen needs to draw itself.
 *
 * Kept as one immutable object so the UI only ever reads a consistent
 * snapshot: there is no moment where a fix is present but the permission
 * flag still says otherwise.
 */
data class LocationUiState(
    val permissionGranted: Boolean = false,
    val permissionDeniedOnce: Boolean = false,
    val gpsEnabled: Boolean = false,
    val fix: GpsFix? = null
) {
    /** True while we hold the permission and GPS is on, but no fix has arrived yet. */
    val waitingForFix: Boolean
        get() = permissionGranted && gpsEnabled && fix == null
}

/**
 * One reading from the GPS provider.
 *
 * Latitude and longitude are what the lab asks for; accuracy and the
 * timestamp are kept so the screen can show how good and how fresh the
 * reading is.
 */
data class GpsFix(
    val latitude: Double,
    val longitude: Double,
    val accuracyMetres: Float,
    val timestampMillis: Long
)
