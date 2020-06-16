package mobileservices.demo.main

import android.os.Bundle
import androidx.activity.viewModels
import mobileservices.demo.arch.BaseActivity
import mobileservices.demo.arch.exhaustive
import mobileservices.demo.databinding.ActivityMainBinding

class MainEntryActivity :
    BaseActivity<MainEntryViewState, MainEntryViewEffects, MainEntryEvent, MainEntryViewModel>() {

    override val viewModel: MainEntryViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        binding.buttonLaunchLocationDemo.setOnClickListener {
            viewModel.process(
                MainEntryEvent.OnStartLocationDemo(
                    this
                )
            )
        }

        setContentView(binding.root)
    }

    override fun renderViewState(viewState: MainEntryViewState) {
        when (viewState) {
            is MainEntryViewState.Default -> {
                binding.checkHasGMS.isChecked = viewState.isGmsAvailable
                binding.checkHasHMS.isChecked = viewState.isHmsAvailable
            }
        }.exhaustive
    }

    override fun renderViewEffect(viewEffect: MainEntryViewEffects) {
        TODO("Not yet implemented")
    }
}
