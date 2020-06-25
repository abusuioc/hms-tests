package mobileservices.demo.licensing

sealed class LicenseCheckViewState {
    data class Result(val licenseCheckSuccess: Boolean) : LicenseCheckViewState()
    object Checking : LicenseCheckViewState()
}

sealed class LicenseCheckViewEffects {

}

sealed class LicenseCheckEvent {
    data class StartLicenseCheck(val activity: LicenseCheckActivity) : LicenseCheckEvent()
}