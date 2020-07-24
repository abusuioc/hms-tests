package mobileservices.demo.audio.playback

import android.app.Application
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

    suspend fun init() {
        val audioManager = createHwAudioManager()
        audioConfigManager = audioManager.configManager
        audioPlayerManager = audioManager.playerManager
        audioQueueManager = audioManager.queueManager
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
}