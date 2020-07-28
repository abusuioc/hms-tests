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

        playbackRepository.playerState().observeForever {
            when (it) {
                is PlaybackRepository.PlayerState.PlayingProgress -> {
                    viewEffect =
                        PlaybackDemoViewEffect.PlayerProgressUpdate(100.0f * it.currentPosition / it.totalDuration)
                }
                is PlaybackRepository.PlayerState.Stopped -> {
                    viewEffect = PlaybackDemoViewEffect.PlayerProgressUpdate(0.0f)
                }
                else -> {
                    viewEffect = PlaybackDemoViewEffect.PlayerStateChanged(it)
                }
            }.exhaustive
        }
    }

    override fun process(viewEvent: PlaybackDemoEvent) {
        super.process(viewEvent)
        when (viewEvent) {
            is PlaybackDemoEvent.Play -> {
                when (viewState) {
                    is PlaybackDemoViewState.Playing,
                    is PlaybackDemoViewState.PlayerReady -> {
                        playbackRepository.playRemoteSong(viewEvent.song.qualityToPath[viewEvent.quality]!!)
                        viewState = PlaybackDemoViewState.Playing(viewEvent.song, viewEvent.quality)
                    }
                    is PlaybackDemoViewState.InitPlayer -> viewEffect =
                        PlaybackDemoViewEffect.ErrorPlayerNotReady
                    is PlaybackDemoViewState.PlayerInitError -> viewEffect =
                        PlaybackDemoViewEffect.ErrorPlayerNotReady
                }
            }
        }.exhaustive
    }

    fun getSongsList() = listOf(songsRepository.song1)

    override fun onCleared() {
        super.onCleared()
        when (viewState) {
            PlaybackDemoViewState.InitPlayer,
            is PlaybackDemoViewState.PlayerInitError -> {
                // no need to dispose the player
            }
            else -> playbackRepository.dispose()
        }
    }
}