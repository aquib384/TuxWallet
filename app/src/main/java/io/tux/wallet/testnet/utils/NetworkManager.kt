package io.tux.wallet.testnet.utils

import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.view.View
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import io.tux.wallet.testnet.R
import io.tux.wallet.testnet.utils.Constants.IS_CONNECTED


@Suppress("DEPRECATION")
object NetworkManager {
    // Network Check
    fun registerNetworkConnections(
        context: Context,
        networkInterface: NetworkInterface?
    ) {
        try {
            var isFirstTime = true
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                connectivityManager.registerDefaultNetworkCallback(object :
                    ConnectivityManager.NetworkCallback() {
                    override fun onAvailable(network: Network) {
                        IS_CONNECTED = true
                        if (!isFirstTime) {
//                            showMessage(null, view, "No Internet Connection")
                        }
                        isFirstTime = false
                        networkInterface?.isConnect(true)
                    }

                    override fun onLost(network: Network) {
                        IS_CONNECTED = false // Global Static Variable
                        if (!isFirstTime) {
//                            showMessage(
//                                context.getColor(R.color.offline),
//                                view,
//                                context.getString(R.string.you_are_offline)
//                            )
                            isFirstTime = false
                            networkInterface?.isConnect(false)
                        }
                    }
                })
            } else {
                val activeNetwork = connectivityManager.activeNetworkInfo
                IS_CONNECTED = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting
            }
        } catch (e: Exception) {
            IS_CONNECTED = false
        }
    }

    private fun showMessage(color: Int?, view: View?, msg: String?) {
        try {
            val snackBar = Snackbar.make(view!!, msg!!, Snackbar.LENGTH_LONG)
            val snackBarView = snackBar.view
            color?.let { snackBar.setBackgroundTint(it) }
            val tv = snackBarView.findViewById<TextView>(R.id.snackbar_text)
            tv.textSize = 14f
            tv.setTextColor(Color.WHITE)
            snackBar.show()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    interface NetworkInterface {
        fun isConnect(isConnected: Boolean)
    }

    fun isConnected(view: View?, context: Context) =
        if (!IS_CONNECTED) {
            showMessage(context.resources.getColor(R.color.red), view, "No Internet Connection")
            false
        } else true
}