package mobileservices.demo.arch

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer

abstract class BaseActivity<STATE, EFFECT, EVENT, ViewModel : BaseViewModel<STATE, EFFECT, EVENT>> :
    AppCompatActivity() {

    abstract val viewModel: ViewModel

    private val viewStateObserver = Observer<STATE> {
        Log.i(TAG, "observed viewState : $it")
        renderViewState(it)
    }

    private val viewEffectObserver = Observer<EFFECT> {
        Log.i(TAG, "observed viewEffect : $it")
        renderViewEffect(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.viewStates().observe(this, viewStateObserver)
        viewModel.viewEffects().observe(this, viewEffectObserver)
    }

    abstract fun renderViewState(viewState: STATE)

    abstract fun renderViewEffect(viewEffect: EFFECT)
}