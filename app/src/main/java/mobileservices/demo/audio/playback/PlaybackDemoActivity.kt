package mobileservices.demo.audio.playback

import android.os.Bundle
import androidx.activity.viewModels
import mobileservices.demo.R
import mobileservices.demo.arch.BaseActivity
import mobileservices.demo.arch.exhaustive
import mobileservices.demo.databinding.ActivityPlaybackdemoBinding

class PlaybackDemoActivity :
    BaseActivity<PlaybackDemoViewState, PlaybackDemoViewEffect, PlaybackDemoEvent, PlaybackDemoViewModel>() {
    override val viewModel: PlaybackDemoViewModel by viewModels()

    private lateinit var binding: ActivityPlaybackdemoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaybackdemoBinding.inflate(layoutInflater)

        binding.buttonPlayStop.setOnClickListener {
            viewModel.process(PlaybackDemoEvent.PlayStopSongAtIndex(0))
        }

        setContentView(binding.root)
    }

    override fun renderViewState(viewState: PlaybackDemoViewState) {
        when (viewState) {
            PlaybackDemoViewState.InitPlayer -> {
                binding.buttonPlayStop.isEnabled = false
                binding.textPlayerStatus.text = getString(
                    R.string.audioplayback_status,
                    getString(R.string.audioplayback_status_initializing)
                )
            }
            PlaybackDemoViewState.PlayerReady -> {
                binding.buttonPlayStop.isEnabled = true
                binding.buttonPlayStop.setText(R.string.audioplayback_play)
                binding.textPlayerStatus.text = getString(
                    R.string.audioplayback_status,
                    getString(R.string.audioplayback_status_ready)
                )
            }
            is PlaybackDemoViewState.PlayerInitError -> {
                binding.buttonPlayStop.isEnabled = false
                binding.textPlayerStatus.text = getString(
                    R.string.audioplayback_status,
                    getString(R.string.audioplayback_status_error, viewState.exception.toString())
                )
            }
            is PlaybackDemoViewState.PlayingSongAtIndex -> {
                binding.buttonPlayStop.isEnabled = true
                binding.textPlayerStatus.text = getString(
                    R.string.audioplayback_status,
                    getString(R.string.audioplayback_status_playing, viewState.title)
                )
                binding.buttonPlayStop.setText(R.string.audioplayback_stop)
            }
        }.exhaustive
    }

    override fun renderViewEffect(viewEffect: PlaybackDemoViewEffect) {
        TODO("Not yet implemented")
    }

}