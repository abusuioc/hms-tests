package mobileservices.demo.licensing

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import mobileservices.demo.R
import mobileservices.demo.arch.BaseActivity
import mobileservices.demo.arch.exhaustive
import mobileservices.demo.databinding.ActivityLicensecheckBinding
import mobileservices.demo.databinding.ActivityMainBinding

class LicenseCheckActivity :
    BaseActivity<LicenseCheckViewState, LicenseCheckViewEffects, LicenseCheckEvent, LicenseCheckViewModel>() {

    override val viewModel: LicenseCheckViewModel by viewModels()

    private lateinit var binding: ActivityLicensecheckBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLicensecheckBinding.inflate(layoutInflater)

        binding.buttonDoCheck.setOnClickListener {
            viewModel.process(
                LicenseCheckEvent.StartLicenseCheck(this)
            )
        }

        setContentView(binding.root)
    }

    override fun renderViewState(viewState: LicenseCheckViewState) {
        when (viewState) {
            LicenseCheckViewState.Checking -> {
                binding.buttonDoCheck.isEnabled = false
                binding.buttonDoCheck.text = getString(R.string.licensecheck_checking)
                binding.textCheckResult.visibility = View.GONE
            }
            is LicenseCheckViewState.Result -> {
                binding.buttonDoCheck.isEnabled = true
                binding.buttonDoCheck.text = getString(R.string.licensecheck_doCheck)
                binding.textCheckResult.visibility = View.VISIBLE
                binding.textCheckResult.text = getString(R.string.licensecheck_result, viewState.licenseCheckSuccess.toString())
            }
        }.exhaustive
    }

    override fun renderViewEffect(viewEffect: LicenseCheckViewEffects) {
        TODO("Not yet implemented")
    }
}
