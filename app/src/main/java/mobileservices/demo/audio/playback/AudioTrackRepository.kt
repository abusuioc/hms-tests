package mobileservices.demo.audio.playback

class AudioTrackRepository {

    val audioTrack1 = AudioTrack(
        interpretation = "Marianne Thorsen / TrondheimSolistene",
        artist = "Mozart",
        title = "Violin concerto in D major-Allegro",
        album = "MOZART (MQA remix 2016)",
        qualityToPath = mapOf(
            Quality.S3 to "http://www.lindberg.no/hires/test/2L-038_MQA2016-352k-24b_01.flac",
            Quality.S2 to "http://www.lindberg.no/hires/test/2L-038_MQA2016-176k-24b_01.flac",
            Quality.S1 to "http://www.lindberg.no/hires/test/2L-038_MQA2016-88k-24b_01.flac",
            Quality.MQA to "http://www.lindberg.no/hires/mqa-2018/2L-038-MQA-2016_01_stereo.mqa.flac",
            Quality.MQA_CD to "http://www.lindberg.no/hires/mqa-cd-2018/2L-038-MQA-2016_01_stereo.mqacd.mqa.flac",
            Quality.CD to "http://www.lindberg.no/hires/test/2L-038_MQA2016_stereo-44k-16b_01.flac",
            Quality.SURROUND to "http://www.lindberg.no/hires/test/2L-038_MQA2016_mch-96k-24b_01.flac",
            Quality.DSD256 to "http://www.lindberg.no/hires/test/2L-056_04_stereo_DSD256.dsf",
            Quality.DSD128 to "http://www.lindberg.no/hires/test/2L-056_04_stereo_DSD128.dsf",
            Quality.DSD64 to "http://www.lindberg.no/hires/test/2L-056_04_stereo_DSD64.dsf",
            Quality.SURROUND_DSD64 to "http://www.lindberg.no/hires/test/2L-056_04_mch_DSD64.dsf"
        )
    )


    data class AudioTrack(
        val artist: String,
        val title: String,
        val interpretation: String?,
        val album: String?,
        val qualityToPath: Map<Quality, String>
    )


    enum class Quality(val description: String) {
        CD("Original CD 16BIT/44kHz"),
        S1("Stereo 24BIT/96kHz"),
        S2("Stereo 24BIT/196kHz"),
        S3("Stereo 24BIT/352.8kHz"),
        MQA("MQA stereo original resolution"),
        MQA_CD("MQA-CD 16BIT/44kHz"),
        SURROUND("5.1 Surround 24BIT/96kHz"),
        DSD64("Stereo DSD 64"),
        DSD128("Stereo DSD 128"),
        DSD256("Stereo DSD 256"),
        SURROUND_DSD64("5.1 Surround DSD 64")
    }
}