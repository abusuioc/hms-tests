package mobileservices.demo.main

import android.app.Application
import android.content.Intent
import mobileservices.demo.arch.BaseViewModel
import mobileservices.demo.arch.exhaustive
import mobileservices.demo.location.LocationDemoActivity
import mobileservices.detector.MobileServicesDetector

class MainEntryViewModel(application: Application) :
    BaseViewModel<MainEntryViewState, MainEntryViewEffects, MainEntryEvent>(application) {

    init {
        viewState = MainEntryViewState.Default(
            MobileServicesDetector.isGmsAvailable(application),
            MobileServicesDetector.isHmsAvailable(application)
        )
    }

    override fun process(viewEvent: MainEntryEvent) {
        super.process(viewEvent)
        when (viewEvent) {
            is MainEntryEvent.OnStartLocationDemo -> viewEvent.activity.startActivity(
                Intent(
                    viewEvent.activity,
                    LocationDemoActivity::class.java
                )
            )
        }.exhaustive
    }

}