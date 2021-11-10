package io.tux.wallet.testnet.appModules


import android.app.Application
import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import dagger.hilt.android.HiltAndroidApp
import io.tux.wallet.testnet.utils.NetworkManager
import io.tux.wallet.testnet.utils.Utils

@HiltAndroidApp
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        NetworkManager.registerNetworkConnections(this, null)
        System.loadLibrary("TrustWalletCore")
    }
}

// register observer
