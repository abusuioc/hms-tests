package mobileservices.demo.audio.playback

import android.app.Application
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import mobileservices.demo.arch.BaseViewModel

class PlaybackDemoViewModel(application: Application) :
    BaseViewModel<PlaybackDemoViewState, PlaybackDemoViewEffect, PlaybackDemoEvent>(application) {

    private val playbackRepository = PlaybackRepository(application)

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

    }
}