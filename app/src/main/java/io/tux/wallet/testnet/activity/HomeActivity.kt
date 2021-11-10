package io.tux.wallet.testnet.activity


import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.AndroidEntryPoint
import io.tux.wallet.testnet.R
import io.tux.wallet.testnet.apis.ApiInterface
import io.tux.wallet.testnet.databinding.ActivityHomeBinding
import io.tux.wallet.testnet.utils.AppLifecycleListener
import io.tux.wallet.testnet.utils.AppLifecycleListener.Companion.mCurrentActivity
import io.tux.wallet.testnet.utils.Constants.UPDATE_REQUEST_CODE
import io.tux.wallet.testnet.utils.NetworkManager
import io.tux.wallet.testnet.utils.SharedPref
import io.tux.wallet.testnet.viewModels.CoinMarketViewModel
import javax.inject.Inject


@AndroidEntryPoint
class HomeActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener,
    NavController.OnDestinationChangedListener {

    @Inject
    lateinit var sharedPref: SharedPref

    @Inject
    lateinit var apiInterface: ApiInterface
    private lateinit var binding: ActivityHomeBinding
    private lateinit var navController: NavController
    private lateinit var navView: BottomNavigationView
    private lateinit var appUpdateManager: AppUpdateManager
    private val viewModel: CoinMarketViewModel by viewModels()
    private val activityLifecycle = ProcessLifecycleOwner.get().lifecycle

    private var observeState: AppLifecycleListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mCurrentActivity = this.javaClass.name
        navView = binding.navView
        navController = findNavController(R.id.nav_host_fragment_activity_home)
        navView.setupWithNavController(navController)
        setNavGraph()
        navController.addOnDestinationChangedListener(this)
        navView.setOnItemSelectedListener(this)

        observePin()
        if (sharedPref.isLightTheme()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        if (NetworkManager.isConnected(binding.root, this)) {
            viewModel.getErcCoins()
//            viewModel.callCoinsApis()

            viewModel.coinList.sortedByDescending { coin ->
                if (coin.raw?.currencies?.isNotEmpty() == true)
                    coin.raw?.currencies?.get(0)?.data?.PRICE.toString().toDouble()
                else null
            }
            viewModel.coinList.forEach {
                Log.e("wallet", it.symbol + " ---- " + it.coin_add)
            }
        }
    }

    private fun observePin() {
        if (sharedPref.isLogin()) {
            observeState = AppLifecycleListener(this)
            observeState?.let { activityLifecycle.addObserver(it) }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        observeState?.let { activityLifecycle.removeObserver(it) }
    }

    /* override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
         super.onActivityResult(requestCode, resultCode, data)
         if (requestCode == Constants.UPDATE_REQUEST_CODE) {
             if (resultCode != RESULT_OK) {
                 updateApp()
             }
             return
         }
 *//*        for (fragment in supportFragmentManager.fragments) {
            fragment.onActivityResult(requestCode, resultCode, data)
        }*//*
    }*/

    private fun setNavGraph() {
        val graphInflater = navController.navInflater
        val navGraph = graphInflater.inflate(R.navigation.mobile_navigation)

        if (!sharedPref.isLogin()) {
            navGraph.startDestination = R.id.navigation_home
        } else {
            navGraph.startDestination = R.id.navigation_wallet
        }
        navController.graph = navGraph
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.navigation_wallet -> {
                setGuestMode(R.id.navigation_wallet)

            }

            R.id.navigation_market -> {
                navController.navigate(R.id.navigation_market)
                true
            }
            R.id.navigation_stake -> {
                setGuestMode(R.id.navigation_stake)

            }
            R.id.navigation_account -> {
                setGuestMode(R.id.navigation_account)
            }
            else -> {
                navController.navigate(R.id.navigation_home)
                true
            }
        }
    }

    private fun setGuestMode(nav_id: Int): Boolean {
        return if (sharedPref.isLogin()) {
            navController.navigate(nav_id)
            true

        } else {

            //            navController.navigate(R.id.navigation_guest)
            startActivity(Intent(this, GuestModeActivity::class.java))
            false

        }
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        if (
            destination.id == R.id.navigation_home ||
            destination.id == R.id.navigation_market ||
            destination.id == R.id.navigation_stake ||
            destination.id == R.id.navigation_wallet ||
            destination.id == R.id.navigation_account
        )
            binding.navView.visibility = VISIBLE
        else
            binding.navView.visibility = GONE
    }


    private fun updateApp() {
        appUpdateManager = AppUpdateManagerFactory.create(this)
// Returns an intent object that you use to check for an update.
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

// Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            appUpdateManager.registerListener(listener)
            val updateType = AppUpdateType.FLEXIBLE
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(updateType)
            ) {
                try {
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        updateType,
                        this,
                        UPDATE_REQUEST_CODE
                    )
                } catch (exception: IntentSender.SendIntentException) {
                    FirebaseCrashlytics.getInstance().recordException(exception)
                }
            }
        }
    }

    // Create a listener to track request state updates.
    @SuppressLint("SwitchIntDef")
    private val listener = InstallStateUpdatedListener { state ->
        when (state.installStatus()) {
            InstallStatus.DOWNLOADED -> popupSnackBarForCompleteUpdate()
            InstallStatus.INSTALLED -> unRegisterListener()
            InstallStatus.INSTALLING -> {
            }
            InstallStatus.DOWNLOADING -> {
/*                val bytesDownloaded = state.bytesDownloaded()
                val totalBytesToDownload = state.totalBytesToDownload()*/
            }
        }
    }

    private fun unRegisterListener() {
        appUpdateManager.unregisterListener(listener)
    }

// Start an update.

// When status updates are no longer needed, unregister the listener.

    private fun popupSnackBarForCompleteUpdate() {
        Snackbar.make(
            binding.root,
            "An update has just been downloaded.",
            Snackbar.LENGTH_INDEFINITE
        ).apply {
            setAction("RESTART") { appUpdateManager.completeUpdate() }
            show()
        }
    }

    override fun onResume() {
        super.onResume()
        updateApp()
    }

}



