package mobileservices.demo.audio.playback

import android.app.Application
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import mobileservices.demo.arch.BaseViewModel
import mobileservices.demo.arch.exhaustive

class PlaybackDemoViewModel(application: Application) :
    BaseViewModel<PlaybackDemoViewState, PlaybackDemoViewEffect, PlaybackDemoEvent>(application) {

    private val playbackRepository = PlaybackRepository(application)
    private val songsRepository = SongsRepository()

    init {
        viewState = PlaybackDemoViewState.InitPlayer

        viewModelScope.launch {
            viewState = try {
                playbackRepository.init()
                PlaybackDemoViewState.PlayerReady
            } catch (e: Exception) {
                PlaybackDemoViewState.PlayerInitError(e)
            }
        }
    }

    override fun process(viewEvent: PlaybackDemoEvent) {
        super.process(viewEvent)
        when (viewEvent) {
            is PlaybackDemoEvent.PlayStopSongAtIndex -> {
                when (viewState) {
                    is PlaybackDemoViewState.PlayingSongAtIndex -> {
                        playbackRepository.stopPlayback()
                        viewState = PlaybackDemoViewState.PlayerReady
                    }
                    is PlaybackDemoViewState.PlayerReady -> {
                        playbackRepository.playRemoteSong(songsRepository.song1.qualityToPath[SongsRepository.Quality.CD]!!)
                    }
                    else -> {

                    }
                }
            }
        }.exhaustive
    }
}