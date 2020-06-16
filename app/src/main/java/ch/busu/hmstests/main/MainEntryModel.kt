package ch.busu.hmstests.main

sealed class MainEntryViewState {
    data class Default(val isGmsAvailable: Boolean, val isHmsAvailable: Boolean) : MainEntryViewState()
}

sealed class MainEntryViewEffects {

}

sealed class MainEntryEvent {
    data class OnStartLocationDemo(val activity: MainEntryActivity):MainEntryEvent()
}