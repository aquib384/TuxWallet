package io.tux.wallet.testnet.utils

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import io.tux.wallet.testnet.activity.HomeActivity

class AppLifecycleListener(private val context: Context) : LifecycleObserver {
    private var isFirstTime = true

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onMoveToForeground() {
        if (isFirstTime) {
            isFirstTime = false
            return
        }
        if (mCurrentActivity == (context as HomeActivity).javaClass.name)
            ContextCompat.startActivity(context, Utils.callValidationScreen(context), null)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onMoveToBackground() { // app moved to background
    }

    companion object {
        var mCurrentActivity: String? = null
    }
}