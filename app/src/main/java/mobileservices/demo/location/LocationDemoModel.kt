package mobileservices.demo.location

import android.location.Location
import mobileservices.location.LocationResult

sealed class LocationDemoViewState {
    object LocationPermissionNotGranted : LocationDemoViewState()
    data class LocationUpdates(
        val lastKnownLocation: Location? = null,
        val locationResult: LocationResult? = null
    ) : LocationDemoViewState()
}

sealed class LocationDemoViewEffects {
    object ShowPermissionDeniedMessage : LocationDemoViewEffects()
}

sealed class LocationDemoEvent {
    data class OnRequestLocationPermission(val requestingActivity: LocationDemoActivity) : LocationDemoEvent()
    data class OnPermissionResult(val requestCode: Int) : LocationDemoEvent()
}