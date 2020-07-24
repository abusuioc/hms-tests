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

        setContentView(binding.root)
    }

    override fun renderViewState(viewState: PlaybackDemoViewState) {
        when (viewState) {
            PlaybackDemoViewState.InitPlayer -> {
                binding.buttonPlay.isEnabled = false
                binding.textPlayerStatus.text = getString(
                    R.string.audioplayback_status,
                    getString(R.string.audioplayback_status_initializing)
                )
            }
            PlaybackDemoViewState.PlayerReady -> {
                binding.buttonPlay.isEnabled = true
                binding.textPlayerStatus.text = getString(
                    R.string.audioplayback_status,
                    getString(R.string.audioplayback_status_ready)
                )
            }
            is PlaybackDemoViewState.PlayerInitError -> {
                binding.buttonPlay.isEnabled = false
                binding.textPlayerStatus.text = getString(
                    R.string.audioplayback_status,
                    getString(R.string.audioplayback_status_error, viewState.exception.toString())
                )
            }
        }.exhaustive
    }

    override fun renderViewEffect(viewEffect: PlaybackDemoViewEffect) {
        TODO("Not yet implemented")
    }

}