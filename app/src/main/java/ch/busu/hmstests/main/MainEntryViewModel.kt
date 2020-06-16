package ch.busu.hmstests.main

import android.app.Application
import android.content.Intent
import ch.busu.hmstests.arch.BaseViewModel
import ch.busu.hmstests.arch.exhaustive
import ch.busu.hmstests.location.LocationDemoActivity
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