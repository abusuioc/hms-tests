package ch.busu.hmstests.location

import android.app.Application
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import ch.busu.hmstests.arch.BaseViewModel
import ch.busu.hmstests.arch.TAG
import ch.busu.hmstests.arch.exhaustive

class LocationDemoViewModel(application: Application) :
    BaseViewModel<LocationDemoViewState, LocationDemoViewEffects, LocationDemoEvent>(application) {

    private val locationRepository = LocationRepository(getApplication())

    init {
        viewState = if (isPermissionGranted(android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            LocationDemoViewState.LocationUpdates()
        } else {
            LocationDemoViewState.LocationPermissionNotGranted
        }

        when (viewState) {
            is LocationDemoViewState.LocationUpdates -> {
                locationRepository.subscribeToLastLocation()
            }
        }

        locationRepository.onLastKnownLocationChanged()
            .observeForever {
                val currentViewState = viewState
                viewState = if (currentViewState is LocationDemoViewState.LocationUpdates) {
                    currentViewState.copy(lastKnownLocation = it)
                } else {
                    LocationDemoViewState.LocationUpdates(lastKnownLocation = it)
                }
            }
        locationRepository.onLocationUpdated().observeForever {
            val currentViewState = viewState
            viewState = if (currentViewState is LocationDemoViewState.LocationUpdates) {
                currentViewState.copy(locationResult = it)
            } else {
                LocationDemoViewState.LocationUpdates(locationResult = it)
            }
        }
    }

    override fun process(viewEvent: LocationDemoEvent) {
        super.process(viewEvent)
        when (viewEvent) {
            is LocationDemoEvent.OnRequestLocationPermission -> requestLocationPermissions(viewEvent.requestingActivity)
            is LocationDemoEvent.OnPermissionResult -> onPermissionResult(viewEvent.requestCode)
        }.exhaustive
    }

    private fun isPermissionGranted(permission: String): Boolean =
        ContextCompat.checkSelfPermission(
            getApplication(),
            permission
        ) == PackageManager.PERMISSION_GRANTED

    private fun requestLocationPermissions(requestingActivity: LocationDemoActivity) {
        ActivityCompat.requestPermissions(
            requestingActivity,
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    private fun onPermissionResult(requestCode: Int) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (isPermissionGranted(android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                viewState = LocationDemoViewState.LocationUpdates()
                locationRepository.subscribeToLastLocation()
            } else {
                viewState = LocationDemoViewState.LocationPermissionNotGranted
                viewEffect = LocationDemoViewEffects.ShowPermissionDeniedMessage
            }
        }
    }

    fun onPause() {
        Log.i(TAG, "onPause")
        locationRepository.stopLocationUpdates()
    }

    fun onResume() {
        Log.i(TAG, "onResume")
        when (viewState) {
            is LocationDemoViewState.LocationUpdates -> {
                locationRepository.startLocationUpdates()
            }
        }
    }

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 101
    }
}