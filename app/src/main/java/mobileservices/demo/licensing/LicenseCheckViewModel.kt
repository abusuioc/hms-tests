package mobileservices.demo.licensing

import android.app.Application
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import mobileservices.demo.arch.BaseViewModel
import mobileservices.demo.arch.exhaustive

class LicenseCheckViewModel(application: Application) :
    BaseViewModel<LicenseCheckViewState, LicenseCheckViewEffects, LicenseCheckEvent>(application) {

    private val licenseCheckRepository = LicenseCheckRepository()

    init {
        viewState = LicenseCheckViewState.Result(licenseCheckSuccess = false)
    }

    override fun process(viewEvent: LicenseCheckEvent) {
        super.process(viewEvent)
        when (viewEvent) {
            is LicenseCheckEvent.StartLicenseCheck -> {
                viewState = LicenseCheckViewState.Checking
                viewModelScope.launch {
                    val isLicenseCheckSuccessful =
                        licenseCheckRepository.doCheck(viewEvent.activity)
                    viewState = LicenseCheckViewState.Result(isLicenseCheckSuccessful)
                }
            }
        }.exhaustive
    }

}