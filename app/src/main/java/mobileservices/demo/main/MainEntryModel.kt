package mobileservices.demo.main

sealed class MainEntryViewState {
    data class Default(val isGmsAvailable: Boolean, val isHmsAvailable: Boolean) : MainEntryViewState()
}

sealed class MainEntryViewEffects {

}

sealed class MainEntryEvent {
    data class OnStartLocationDemo(val activity: MainEntryActivity):MainEntryEvent()
    data class OnStartLicenseCheckDemo(val activity: MainEntryActivity):MainEntryEvent()
    data class OnStartAudioPlaybackDemo(val activity: MainEntryActivity):MainEntryEvent()
}