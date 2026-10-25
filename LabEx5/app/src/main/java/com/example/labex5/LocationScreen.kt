package com.example.labex5

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.labex5.ui.theme.LabEx5Theme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * The whole screen. It draws one of four things, in priority order:
 * no permission, GPS switched off, waiting for the first fix, or the fix itself.
 */
@Composable
fun LocationScreen(
    state: LocationUiState,
    onRequestPermission: () -> Unit,
    onOpenLocationSettings: () -> Unit,
    onOpenAppSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "My GPS location",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(Modifier.height(24.dp))

            when {
                !state.permissionGranted -> PermissionNeeded(
                    deniedOnce = state.permissionDeniedOnce,
                    onRequestPermission = onRequestPermission,
                    onOpenAppSettings = onOpenAppSettings
                )

                !state.gpsEnabled -> GpsOff(onOpenLocationSettings = onOpenLocationSettings)

                state.fix == null -> WaitingForFix()

                else -> FixCard(fix = state.fix)
            }
        }
    }
}

@Composable
private fun PermissionNeeded(
    deniedOnce: Boolean,
    onRequestPermission: () -> Unit,
    onOpenAppSettings: () -> Unit
) {
    Text(
        text = if (deniedOnce) {
            "Precise location was denied. The GPS cannot be read without it."
        } else {
            "This app needs your precise location to read the GPS."
        },
        style = MaterialTheme.typography.bodyLarge
    )
    Spacer(Modifier.height(16.dp))
    Button(onClick = onRequestPermission) {
        Text("Grant location permission")
    }
    if (deniedOnce) {
        Spacer(Modifier.height(8.dp))
        Button(onClick = onOpenAppSettings) {
            Text("Open app settings")
        }
    }
}

@Composable
private fun GpsOff(onOpenLocationSettings: () -> Unit) {
    Text(
        text = "Location is switched off on this device.",
        style = MaterialTheme.typography.bodyLarge
    )
    Spacer(Modifier.height(16.dp))
    Button(onClick = onOpenLocationSettings) {
        Text("Open location settings")
    }
}

@Composable
private fun WaitingForFix() {
    CircularProgressIndicator()
    Spacer(Modifier.height(16.dp))
    Text(
        text = "Waiting for a GPS fix…",
        style = MaterialTheme.typography.bodyLarge
    )
    Spacer(Modifier.height(8.dp))
    Text(
        text = "This can take a moment outdoors, and needs a set location on an emulator.",
        style = MaterialTheme.typography.bodySmall
    )
}

@Composable
private fun FixCard(fix: GpsFix) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(20.dp)) {
            Coordinate(label = "Latitude", value = fix.latitude)
            Spacer(Modifier.height(12.dp))
            Coordinate(label = "Longitude", value = fix.longitude)
            Spacer(Modifier.height(16.dp))
            Text(
                text = "Accuracy: ±${"%.0f".format(fix.accuracyMetres)} m",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Updated: ${formatTime(fix.timestampMillis)}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Source: GPS provider",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun Coordinate(label: String, value: Double) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.titleMedium)
        Text(
            // Six decimal places is about 0.1 m, finer than any GPS fix.
            text = "%.6f".format(value),
            style = MaterialTheme.typography.titleMedium,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
        )
    }
}

private fun formatTime(millis: Long): String =
    SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date(millis))

@Preview(showBackground = true)
@Composable
private fun FixPreview() {
    LabEx5Theme {
        LocationScreen(
            state = LocationUiState(
                permissionGranted = true,
                gpsEnabled = true,
                fix = GpsFix(43.676500, -79.410300, 12f, System.currentTimeMillis())
            ),
            onRequestPermission = {},
            onOpenLocationSettings = {},
            onOpenAppSettings = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PermissionPreview() {
    LabEx5Theme {
        LocationScreen(
            state = LocationUiState(),
            onRequestPermission = {},
            onOpenLocationSettings = {},
            onOpenAppSettings = {}
        )
    }
}
