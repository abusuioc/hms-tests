package mobileservices.demo.audio.playback

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.huawei.hms.api.bean.HwAudioPlayItem
import com.huawei.hms.audiokit.player.callback.HwAudioConfigCallBack
import com.huawei.hms.audiokit.player.manager.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class PlaybackRepository(
    val application: Application
) {

    lateinit var audioConfigManager: HwAudioConfigManager
    lateinit var audioPlayerManager: HwAudioPlayerManager
    lateinit var audioQueueManager: HwAudioQueueManager
    lateinit var audioManager: HwAudioManager
    lateinit var playerStatusListenter: HwAudioStatusListener

    private val _playerState: MutableLiveData<PlayerState> =
        MutableLiveData(PlayerState.Stopped)

    fun playerState(): LiveData<PlayerState> = _playerState

    suspend fun init() {
        audioManager = createHwAudioManager()
        audioConfigManager = audioManager.configManager
        audioPlayerManager = audioManager.playerManager
        audioQueueManager = audioManager.queueManager

        playerStatusListenter = object : HwAudioStatusListener {
            override fun onBufferProgress(percentage: Int) {
                _playerState.value = PlayerState.Buffering(percentage)
            }

            override fun onPlayStateChange(isPlaying: Boolean, isBuffering: Boolean) {
                _playerState.value =
                    if (isPlaying) {
                        if (isBuffering) {
                            PlayerState.PlayingAndBuffering
                        } else {
                            PlayerState.Playing
                        }
                    } else {
                        if (isBuffering) {
                            PlayerState.StoppedAndBuffering
                        } else {
                            PlayerState.Stopped
                        }
                    }
            }

            override fun onQueueChanged(p0: MutableList<HwAudioPlayItem>?) {
            }

            override fun onPlayProgress(currPos: Long, duration: Long) {
                _playerState.value = PlayerState.PlayingProgress(currPos, duration)
            }

            override fun onPlayError(errorCode: Int, p1: Boolean) {
                _playerState.value = PlayerState.Error(errorCode)
            }

            override fun onSongChange(playItem: HwAudioPlayItem?) {
                playItem?.let { _playerState.value = PlayerState.SongChanged(it.audioTitle) }
            }

            override fun onPlayCompleted(isStopped: Boolean) {
                _playerState.value = if (isStopped) PlayerState.Stopped else PlayerState.Completed
            }
        }

        audioManager.addPlayerStatusListener(playerStatusListenter)
    }

    private suspend fun createHwAudioManager(): HwAudioManager {
        val hwAudioPlayerConfig = HwAudioPlayerConfig(application).apply {
            isDebugMode = true
        }

        return suspendCoroutine { continuation ->
            HwAudioManagerFactory.createHwAudioManager(
                hwAudioPlayerConfig,
                object : HwAudioConfigCallBack {
                    override fun onSuccess(hwAudioManager: HwAudioManager) {
                        try {
                            // Obtain the playback control instance.
                            hwAudioManager.playerManager
                            // Obtain the configuration control instance.
                            hwAudioManager.configManager
                            // Obtain the queue control instance.
                            hwAudioManager.queueManager
                            continuation.resume(hwAudioManager)
                        } catch (e: Exception) {
                            continuation.resumeWithException(e)
                        }
                    }

                    override fun onError(errorCode: Int) {
                        continuation.resumeWithException(UnsupportedOperationException(errorCode.toString()))
                    }
                })
        }
    }

    fun playRemoteSong(url: String) {
        val item = HwAudioPlayItem().apply {
            audioTitle = url
            audioId = url.hashCode().toString()
            setOnline(1)
            onlinePath = url
        }
        val playItemList = listOf(item)
        audioPlayerManager.playList(playItemList, 0, 0)
    }

    fun stopPlayback() {
        audioPlayerManager.stop()
    }

    fun dispose() {
        if (audioPlayerManager.isBuffering || audioPlayerManager.isPlaying) {
            stopPlayback()
        }
        audioManager.removePlayerStatusListener(playerStatusListenter)
    }

    sealed class PlayerState {
        class Buffering(val progressInPercentage: Int) : PlayerState()
        object Stopped : PlayerState()
        object StoppedAndBuffering : PlayerState()
        object Playing : PlayerState()
        object PlayingAndBuffering : PlayerState()
        class PlayingProgress(val currentPosition: Long, val totalDuration: Long) : PlayerState()
        object Completed : PlayerState()
        class Error(val errorCode: Int) : PlayerState()
        class SongChanged(val title: String) : PlayerState()
    }
}