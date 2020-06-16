package ch.busu.hmstests.location

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import ch.busu.hmstests.R
import ch.busu.hmstests.arch.BaseActivity
import ch.busu.hmstests.arch.exhaustive
import ch.busu.hmstests.databinding.ActivityLocationdemoBinding
import com.google.android.material.snackbar.Snackbar

class LocationDemoActivity :
    BaseActivity<LocationDemoViewState, LocationDemoViewEffects, LocationDemoEvent, LocationDemoViewModel>() {
    override val viewModel: LocationDemoViewModel by viewModels()

    private lateinit var binding: ActivityLocationdemoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLocationdemoBinding.inflate(layoutInflater)

        binding.buttonRequestPermission.setOnClickListener {
            viewModel.process(
                LocationDemoEvent.OnRequestLocationPermission(this)
            )
        }

        setContentView(binding.root)
    }


    override fun renderViewState(viewState: LocationDemoViewState) {
        when (viewState) {
            LocationDemoViewState.LocationPermissionNotGranted -> {
                binding.layoutDefault.visibility = View.VISIBLE
                binding.layoutMain.visibility = View.GONE
            }
            is LocationDemoViewState.LocationUpdates -> {
                binding.layoutDefault.visibility = View.GONE
                binding.layoutMain.visibility = View.VISIBLE
                binding.textLastKnownLocation.text =
                    getString(
                        R.string.locationdemo_lastKnownLocation,
                        viewState.lastKnownLocation ?: getString(R.string.locationdemo_noLocations)
                    )
                binding.textLocations.text =
                    getString(
                        R.string.locationdemo_updatedLocations,
                        viewState.locationResult?.locations?.let { if (it.isNotEmpty()) it.toString() else null }
                            ?: getString(R.string.locationdemo_noLocations)
                    )
            }
        }.exhaustive
    }

    override fun renderViewEffect(viewEffect: LocationDemoViewEffects) {
        when (viewEffect) {
            is LocationDemoViewEffects.ShowPermissionDeniedMessage -> {
                Snackbar.make(
                    binding.layoutParent,
                    R.string.locationdemo_permissionError,
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }.exhaustive
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        viewModel.process(LocationDemoEvent.OnPermissionResult(requestCode))
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

}