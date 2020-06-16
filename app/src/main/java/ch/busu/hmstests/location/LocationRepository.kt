package ch.busu.hmstests.location

import android.annotation.SuppressLint
import android.app.Application
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ch.busu.hmstests.arch.TAG
import mobileservices.location.*

class LocationRepository(
    application: Application
) {
    private val _locationUpdates: MutableLiveData<LocationResult> = MutableLiveData()
    fun onLocationUpdated(): LiveData<LocationResult> = _locationUpdates

    private val _lastKnownLocation: MutableLiveData<Location> = MutableLiveData()
    fun onLastKnownLocationChanged(): LiveData<Location> = _lastKnownLocation

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application.applicationContext)

    private val locationUpdatesCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            Log.i(TAG, "locationResult: $locationResult")
            locationResult?.let { _locationUpdates.value = it }
        }
    }

    private fun createLocationRequest() =
        LocationRequest.create()?.apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

    fun startLocationUpdates() {
        Log.i(TAG, "startLocationUpdates")
        fusedLocationClient.requestLocationUpdates(
            createLocationRequest(),
            locationUpdatesCallback,
            Looper.getMainLooper()
        )
    }

    fun stopLocationUpdates() {
        Log.i(TAG, "stopLocationUpdates")
        fusedLocationClient.removeLocationUpdates(locationUpdatesCallback)
    }

    @SuppressLint("MissingPermission")
    fun subscribeToLastLocation() {
        Log.i(TAG, "subscribeToLastLocation")
        fusedLocationClient.lastLocation?.addOnSuccessListener {
            Log.i(TAG, "lastKnownLocation: $it")
            it?.let {
                _lastKnownLocation.value = it
            }
        }
    }
}
