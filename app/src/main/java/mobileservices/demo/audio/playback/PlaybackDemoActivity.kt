package mobileservices.demo.audio.playback

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.viewModels
import mobileservices.demo.R
import mobileservices.demo.arch.BaseActivity
import mobileservices.demo.arch.exhaustive
import mobileservices.demo.databinding.ActivityPlaybackdemoBinding
import mobileservices.demo.databinding.ViewPlaybacksongBinding


class PlaybackDemoActivity :
    BaseActivity<PlaybackDemoViewState, PlaybackDemoViewEffect, PlaybackDemoEvent, PlaybackDemoViewModel>() {
    override val viewModel: PlaybackDemoViewModel by viewModels()

    private lateinit var binding: ActivityPlaybackdemoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaybackdemoBinding.inflate(layoutInflater)

        binding.seekbarPlayerProgress.setOnTouchListener(OnTouchListener { _, _ -> true })
        binding.textPlayerStatus.movementMethod = ScrollingMovementMethod()

        setContentView(binding.root)
        initSongs(viewModel.getSongsList())
    }

    override fun renderViewState(viewState: PlaybackDemoViewState) {
        when (viewState) {
            is PlaybackDemoViewState.InitPlayer -> {
                binding.textPlayerStatus.text =
                    getString(R.string.audioplayback_status_initializing)
            }
            PlaybackDemoViewState.PlayerReady -> {
                binding.textPlayerStatus.text = getString(R.string.audioplayback_status_ready)
                binding.seekbarPlayerProgress.progress = 0
            }
            is PlaybackDemoViewState.PlayerInitError -> {
                binding.textPlayerStatus.text =
                    getString(R.string.audioplayback_status_error, viewState.exception.toString())
            }
            is PlaybackDemoViewState.Playing -> {
                binding.textPlayerStatus.text = getString(
                    R.string.audioplayback_status_playingIssued,
                    viewState.song.title,
                    viewState.song.artist,
                    viewState.quality.description
                )
            }
        }.exhaustive
    }

    override fun renderViewEffect(viewEffect: PlaybackDemoViewEffect) {
        when (viewEffect) {
            PlaybackDemoViewEffect.ErrorPlayerNotReady -> getString(R.string.audioplayback_status_notready)
            is PlaybackDemoViewEffect.PlayerStateChanged -> {
                when (viewEffect.playerState) {
                    is PlaybackRepository.PlayerState.Buffering -> getString(
                        R.string.audioplayback_status_buffering,
                        viewEffect.playerState.progressInPercentage
                    )
                    PlaybackRepository.PlayerState.Stopped -> getString(R.string.audioplayback_status_stopped)
                    PlaybackRepository.PlayerState.StoppedAndBuffering -> getString(R.string.audioplayback_status_stoppedAndBuffering)
                    PlaybackRepository.PlayerState.Playing -> getString(R.string.audioplayback_status_playing)
                    PlaybackRepository.PlayerState.PlayingAndBuffering -> getString(R.string.audioplayback_status_playingAndBuffering)
                    is PlaybackRepository.PlayerState.PlayingProgress -> null
                    PlaybackRepository.PlayerState.Completed -> getString(R.string.audioplayback_status_completed)
                    is PlaybackRepository.PlayerState.Error -> getString(
                        R.string.audioplayback_status_error,
                        viewEffect.playerState.errorCode.toString()
                    )
                    is PlaybackRepository.PlayerState.SongChanged -> getString(
                        R.string.audioplayback_status_songChanged,
                        viewEffect.playerState.title
                    )
                }
            }
            is PlaybackDemoViewEffect.PlayerProgressUpdate -> {
                binding.seekbarPlayerProgress.progress = viewEffect.progressPercentage.toInt()
                null
            }
        }?.let { binding.textPlayerStatus.append("\n$it") }
    }

    private fun initSongs(songs: List<SongsRepository.Song>) {
        for (song in songs) {
            val songLayoutBinding = ViewPlaybacksongBinding.inflate(layoutInflater)

            songLayoutBinding.textPlaybackSongDetails.text = getString(
                R.string.audioplayback_song_title,
                song.title,
                song.artist,
                song.interpretation,
                song.album
            )

            for (qualityToPathPair in song.qualityToPath) {
                val (songQuality, _) = qualityToPathPair
                val playSongButton = Button(this).apply {
                    text = songQuality.toString()
                    setOnClickListener {
                        viewModel.process(PlaybackDemoEvent.Play(song, songQuality))
                    }
                    isDuplicateParentStateEnabled = true
                }
                songLayoutBinding.layoutPlaybackSong.addView(
                    playSongButton,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }

            binding.layoutPlaybackSongs.addView(
                songLayoutBinding.root,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }

}