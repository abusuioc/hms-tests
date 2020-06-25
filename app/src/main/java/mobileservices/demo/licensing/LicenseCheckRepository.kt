package mobileservices.demo.licensing

import com.huawei.android.sdk.drm.Drm
import com.huawei.android.sdk.drm.DrmCheckCallback
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LicenseCheckRepository {
    suspend fun doCheck(activity: LicenseCheckActivity): Boolean =
        suspendCoroutine { continuation ->
            Drm.check(
                activity,
                activity.packageName,
                DRM_ID,
                DRM_PUBLIC_KEY,
                object : DrmCheckCallback {
                    override fun onCheckSuccess() {
                        continuation.resume(true)
                    }

                    override fun onCheckFailed() {
                        continuation.resume(false)
                    }
                }
            )
        }

    companion object {
        const val DRM_ID = "5190041000024232879"
        const val DRM_PUBLIC_KEY =
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsZs6vykesVr5z+ksA2kD9vvezzvgXZd9NtU0wg43HLv7ADjG74ygnaOz8f9DaivrDaLsWNxwSe3LglWOL+gJEGZbeql20nPZGSTiwab6+EJNMuSTjQ4xaguS+j6lSG/8F1S2XkOXNLN3XfydxpZaFPrEU4XjcyBJUx6+xmwy8qFT436EdvFhHPeJbh1+qsIovnvLCS+cFdSDQPQZgY7R2IKwEdn35kjNHKTvhdmG5cEZwRqWrwHJCBgqWtgrC6KisAggYeofTsqLgPeflkXYacM5OPKDUvLZ6Ho2XGjTcYi29u7XHa0HB+35J2hXOmmjQIdvtXjhYsF/ul9emhyfDwIDAQAB"
    }
}