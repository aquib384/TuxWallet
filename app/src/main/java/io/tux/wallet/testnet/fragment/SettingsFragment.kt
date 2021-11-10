package io.tux.wallet.testnet.fragment


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.AndroidEntryPoint
import io.tux.wallet.testnet.R
import io.tux.wallet.testnet.activity.CreateWalletActivity
import io.tux.wallet.testnet.activity.WelcomeActivity
import io.tux.wallet.testnet.databinding.FragmentSettingsBinding
import io.tux.wallet.testnet.utils.Constants
import io.tux.wallet.testnet.utils.Constants.BIOMETRIC
import io.tux.wallet.testnet.utils.Constants.LOGOUT
import io.tux.wallet.testnet.utils.Constants.NON_BIOMETRIC
import io.tux.wallet.testnet.utils.Constants.RECOVERY
import io.tux.wallet.testnet.utils.SharedPref
import io.tux.wallet.testnet.utils.Utils
import io.tux.wallet.testnet.viewModels.OnBoardingViewModel
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : Fragment(), View.OnClickListener {
    private var isLogout: Boolean = false
    lateinit var binding: FragmentSettingsBinding
    lateinit var biometricPrompInstance: BiometricPrompt

    @Inject
    lateinit var sharedPref: SharedPref
    private val viewModel: OnBoardingViewModel by viewModels()
    lateinit var language: String
    lateinit var lockType: String
    lateinit var notiEnabled: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        setAnimations()
        Log.e("lang", sharedPref.getLanguage().toString())

        biometricPrompInstance = instanceOfBiometricPrompt()
        language = when (sharedPref.getLanguage()) {
            0 -> resources.getString(R.string.english)
            1 -> resources.getString(R.string.chinese)
            2 -> resources.getString(R.string.japanese)
            else -> "en"
        }
        binding.tvLanguage.text = resources.getString(R.string.language)
        if (!sharedPref.getCurrencyCode().isNullOrEmpty())
            binding.tvCurrency.text =
                resources.getString(R.string.currency) + " ( ${sharedPref.getCurrencyCode()} )"
        binding.cardBiometric.setOnClickListener(this)
        binding.cardNotification.setOnClickListener(this)
        binding.cardChangePin.setOnClickListener(this)
        binding.cardCurrency.setOnClickListener(this)
        binding.cardLanguage.setOnClickListener(this)
        binding.ivBack.setOnClickListener(this)

        binding.switchNight.isChecked = !sharedPref.isLightTheme()
        binding.switchBiometrics.isChecked = sharedPref.isBiometricEnabled()
        binding.switchPinLogins.isChecked = sharedPref.isPinEnabled()
        binding.switchNotification.isChecked = sharedPref.isNotifyEnabled()

        binding.cardLogout.setOnClickListener {
            isLogout = true
            validate.launch(context?.let { it1 -> Utils.callValidationScreen(it1, LOGOUT) })
        }

        binding.cardRecoveryPhase.setOnClickListener {
            isLogout = false
            validate.launch(context?.let { it1 -> Utils.callValidationScreen(it1, LOGOUT) })
        }

        binding.switchNight.setOnCheckedChangeListener { _, isChecked ->
            when {
                isChecked -> {

                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    sharedPref.setLightTheme(false)
                }
                else -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    sharedPref.setLightTheme(true)
                }

            }
        }
        binding.switchPinLogins.setOnCheckedChangeListener { _, isChecked ->
            when {
                isChecked -> {
                    sharedPref.setPinEnabled(true)
                }
                else -> {
                    sharedPref.setPinEnabled(false)
                }

            }
            cardPinVisibility()
        }
        binding.switchBiometrics.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
//                sharedPref.setBioMetricEnables(true)
                lockType = BIOMETRIC
//                val biometricManager = BiometricManager.from(requireContext())
//                if (biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS){
////                    // TODO: show in-app settings, make authentication calls.
//                }
//                val canAuthenticate = BiometricManager.from(requireContext()).canAuthenticate()
//                if (canAuthenticate == BiometricManager.BIOMETRIC_SUCCESS) {
//                    biometricPrompInstance.authenticate(getPromptInfo())
//                } else {
//                    Log.d(TAG, "could not authenticate because: $canAuthenticate")
//                }
            } else {
                lockType = NON_BIOMETRIC
//                sharedPref.setBioMetricEnables(false)
            }

            updateBiometrics(lockType)
        }
        binding.switchNotification.setOnCheckedChangeListener { _, isChecked ->
            notiEnabled = if (isChecked) {
                "YES"
            } else {
                "NO"
            }

            viewModel.updateFcmStatus(
                hashMapOf(
                    Constants.DEVICE_ID to sharedPref.getDeviceId(),
                    Constants.FCMSTATUS to notiEnabled,
                    Constants.ID to 0,
                    Constants.MSG to "update",
                    Constants.TOKENID to sharedPref.getFCMToken(),
                    Constants.USER_DETAILS_ID to sharedPref.getWalletId()
                )
            )
        }

        viewModel.saveFcmModel.observe(viewLifecycleOwner) {
            kotlin.runCatching {
                if (it.data?.fcmStatus == "ON") {
                    sharedPref.setNotifyEnabled(true)
                } else {
                    sharedPref.setNotifyEnabled(false)
                }
            }.onFailure { FirebaseCrashlytics.getInstance().recordException(it) }
        }
        return binding.root
    }

    val validate = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            if (isLogout) {
                sharedPref.logoutWallet()
                startActivity(Intent(activity, WelcomeActivity::class.java))
                activity?.finishAffinity()
                activity?.finishAndRemoveTask()
            } else {
                startActivity(Intent(activity, CreateWalletActivity::class.java).apply {
                    putExtra("from", RECOVERY)
                })
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.card_language -> findNavController().navigate(R.id.nav_lang)
            R.id.card_change_pin -> findNavController().navigate(R.id.navigation_change_pin)
            R.id.card_currency -> findNavController().navigate(R.id.navigation_currency)
            R.id.iv_back -> findNavController().popBackStack()

        }
    }


    private fun instanceOfBiometricPrompt(): BiometricPrompt {
        val executor = ContextCompat.getMainExecutor(requireContext())

        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Utils.showToast(requireContext(), "$errorCode :: $errString")
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Utils.showToast(requireContext(), "Authentication failed for an unknown reason")
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                Utils.showToast(requireContext(), "Authentication was successful")
            }
        }

        return BiometricPrompt(this, executor, callback)
    }

    private fun setAnimations() {
        val animationSlide =
            AnimationUtils.loadAnimation(requireContext(), R.anim.item_animation_fall_down)
        binding.cardChangePin.startAnimation(animationSlide)
        binding.cardBiometric.startAnimation(animationSlide)
        binding.cardPinLogin.startAnimation(animationSlide)
        binding.cardCurrency.startAnimation(animationSlide)
        binding.cardLanguage.startAnimation(animationSlide)
        binding.cardNotification.startAnimation(animationSlide)
        binding.cardNight.startAnimation(animationSlide)
        binding.cardLogout.startAnimation(animationSlide)
        binding.cardRecoveryPhase.startAnimation(animationSlide)

    }

    private fun cardPinVisibility() {
        if (sharedPref.isPinEnabled()) {
            binding.cardChangePin.visibility = VISIBLE

        } else {
            binding.cardChangePin.visibility = GONE
        }
    }


    fun updateBiometrics(locType: String) {
//        loadingBar.show()
        sharedPref.getWalletId()?.toLong()?.let { viewModel.updateBiometrics(locType, it) }
        viewModel.recoverModel.observe(viewLifecycleOwner, { it ->

            if (it == null) {
            } else {
                when (it.data.lockType) {
                    BIOMETRIC -> sharedPref.setBioMetricEnables(true)
                    else -> sharedPref.setBioMetricEnables(false)
                }
//                loadingBar.dismiss()
                Utils.showToast(requireContext(), it.message)
            }

        })
    }
}