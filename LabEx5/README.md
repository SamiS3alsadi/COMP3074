# Lab 5 - GPS latitude and longitude

Android app that shows the device's current latitude and longitude, read from
the GPS.

## What it does

- Asks for `ACCESS_FINE_LOCATION` at runtime and handles a refusal
- Reads the position from `LocationManager.GPS_PROVIDER`
- Shows latitude, longitude, accuracy and the time of the reading
- Detects that location is switched off and offers to open Settings
- Stops GPS updates when the app is not in the foreground

## Permissions

`ACCESS_FINE_LOCATION` is the one the GPS provider needs; `ACCESS_COARSE_LOCATION`
is requested alongside it because Android 12 and later let the user grant
approximate location only. Both are declared in `AndroidManifest.xml` and
requested at run time from `MainActivity`.

## Running it on an emulator

The emulator has no GPS radio, so a position has to be supplied:

1. Run the app and grant the location permission.
2. Open Extended Controls ("..." beside the emulator) -> Location.
3. Enter a latitude and longitude, then click **Set Location**.

The reading appears on screen within a second.

## Files

| File | Purpose |
|------|---------|
| `MainActivity.kt` | Permission request, GPS lifetime, screen state |
| `GpsLocationSource.kt` | Wraps `LocationManager`, fixed to the GPS provider |
| `LocationUiState.kt` | The state the screen draws |
| `LocationScreen.kt` | Compose UI |
