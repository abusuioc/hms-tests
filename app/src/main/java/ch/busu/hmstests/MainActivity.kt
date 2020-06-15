package ch.busu.hmstests

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ch.busu.hmstests.databinding.ActivityMainBinding
import mobileservices.detector.MobileServicesDetector

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)

        binding.mainHasGMS.isChecked = MobileServicesDetector.isGmsAvailable(this)
        binding.mainHasHMS.isChecked = MobileServicesDetector.isHmsAvailable(this)

        setContentView(binding.root)
    }
}
