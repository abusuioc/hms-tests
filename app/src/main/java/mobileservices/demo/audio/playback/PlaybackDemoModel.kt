package mobileservices.demo.audio.playback

import java.lang.Exception

sealed class PlaybackDemoViewState {
    object InitPlayer : PlaybackDemoViewState()
    object PlayerReady : PlaybackDemoViewState()
    class PlayerInitError(val exception: Exception) : PlaybackDemoViewState()
    class Playing(val audioTrack: AudioTrackRepository.AudioTrack, val quality: AudioTrackRepository.Quality) :
        PlaybackDemoViewState()
}

sealed class PlaybackDemoViewEffect {
    object ErrorPlayerNotReady : PlaybackDemoViewEffect()
    class PlayerStateChanged(val playerState: PlaybackRepository.PlayerState) : PlaybackDemoViewEffect()
    class PlayerProgressUpdate(val progressPercentage: Float) : PlaybackDemoViewEffect()
}

sealed class PlaybackDemoEvent {
    class Play(val audioTrack: AudioTrackRepository.AudioTrack, val quality: AudioTrackRepository.Quality) :
        PlaybackDemoEvent()
}