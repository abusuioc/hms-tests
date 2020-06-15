package ch.busu.hmstests

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import ch.busu.hmstests.databinding.ActivityCurrentlocationBinding
import com.google.android.material.snackbar.Snackbar
import mobileservices.location.*

class CurrentLocationActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val locationUpdatesCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            updateLocationsUI(locationResult)
        }
    }

    private val LOCATION_PERMISSION_REQUEST_CODE = 101

    lateinit var binding: ActivityCurrentlocationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        binding = ActivityCurrentlocationBinding.inflate(layoutInflater)

        setContentView(binding.root)

        if (isPermissionGranted(android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            locationPermissionGrantedUpdateUI()
        } else {
            requestLocationPermissions()
        }
    }

    override fun onStart() {
        super.onStart()
        subscribeToLastLocation()
    }

    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    private fun isPermissionGranted(permission: String): Boolean =
        ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED

    private fun requestLocationPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (isPermissionGranted(android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                locationPermissionGrantedUpdateUI()
            } else {
                Snackbar.make(
                    binding.currentLocationCoordinatorLayoutMain,
                    R.string.currentLocation_permissionError,
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun locationPermissionGrantedUpdateUI() {
        binding.currentLocationLinearLayoutDefault.visibility = View.GONE
        binding.currentLocationLinearLayoutMain.visibility = View.VISIBLE
        binding.currentLocationTextViewLastKnownLocation.text =
            getString(
                R.string.currentLocation_lastKnownLocation,
                getString(R.string.currentLocation_noLocations)
            )
    }

    @SuppressLint("MissingPermission")
    private fun subscribeToLastLocation() {
        if (isPermissionGranted(android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            fusedLocationClient.lastLocation?.addOnSuccessListener(this) {
                binding.currentLocationTextViewLastKnownLocation.text =
                    getString(R.string.currentLocation_lastKnownLocation, it.toString())
            }
        }
    }

    private fun createLocationRequest() =
        LocationRequest.create()?.apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

    private fun startLocationUpdates() {
        if (isPermissionGranted(android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            fusedLocationClient.requestLocationUpdates(
                createLocationRequest(),
                locationUpdatesCallback,
                Looper.getMainLooper()
            )
        }
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationUpdatesCallback)
    }

    private fun updateLocationsUI(locationResult: LocationResult?) {
        val text = if (locationResult == null || locationResult.locations.isEmpty()) {
            getString(R.string.currentLocation_noLocations)
        } else {
            locationResult.locations.joinToString(separator = " ; ")
        }
        binding.currentLocationTextViewLocations.text = getString(
            R.string.currentLocation_updatedLocations,
            text
        )
    }
}
