package ch.busu.hmstests

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ch.busu.hmstests.databinding.ActivityMainBinding
import com.google.android.gms.common.GoogleApiAvailability
import com.huawei.hms.api.HuaweiApiAvailability

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)

        binding.mainHasGMS.isChecked = isGmsAvailable(this)
        binding.mainHasHMS.isChecked = isHmsAvailable(this)

        setContentView(binding.root)
    }

    private fun isGmsAvailable(context: Context): Boolean {
        return GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context) ==  com.google.android.gms.common.ConnectionResult.SUCCESS
    }

    private fun isHmsAvailable(context: Context): Boolean {
        return HuaweiApiAvailability.getInstance().isHuaweiMobileServicesAvailable(context) == com.huawei.hms.api.ConnectionResult.SUCCESS
    }

}
