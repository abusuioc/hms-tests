package mobileservices.demo.audio.playback

import android.app.Application
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

    val song_TrondheimSolistene_BrittenFrankBridgeVariationsRomance =
        mapOf(
            Quality.HiRes to "http://www.lindberg.no/hires/test/2L-125_stereo-352k-24b_04.flac",
            Quality.OriginalCd to "http://www.lindberg.no/hires/test/2L-125_stereo-44k-16b_04.flac"
        )


    enum class Quality(val description: String) {
        HiRes("Stereo 24BIT/352.8kHz"),
        OriginalCd("Original CD 16BIT/44kHz")
    }

}