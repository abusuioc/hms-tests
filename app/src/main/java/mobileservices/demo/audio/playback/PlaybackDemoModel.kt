package mobileservices.demo.audio.playback

import java.lang.Exception

sealed class PlaybackDemoViewState {
    object InitPlayer : PlaybackDemoViewState()
    object PlayerReady : PlaybackDemoViewState()
    class PlayerInitError(val exception: Exception) : PlaybackDemoViewState()
}

sealed class PlaybackDemoViewEffect {

}

sealed class PlaybackDemoEvent {

}