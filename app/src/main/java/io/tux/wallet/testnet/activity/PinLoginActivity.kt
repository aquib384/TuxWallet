package io.tux.wallet.testnet.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import io.tux.wallet.testnet.R
import io.tux.wallet.testnet.databinding.ActivityPinLoginBinding
import io.tux.wallet.testnet.utils.*
import io.tux.wallet.testnet.utils.AppLifecycleListener.Companion.mCurrentActivity
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.schedule

@AndroidEntryPoint
class PinLoginActivity : AppCompatActivity(), View.OnClickListener {
    private var doubleBackToExitPressedOnce: Boolean = false
    lateinit var binding: ActivityPinLoginBinding

    @Inject
    lateinit var sharedPref: SharedPref
    private var codeCount: Int = 0
    private var pin: String = ""
    private var fromContext: String = ""
    private var tag: String = PinLoginActivity::class.java.simpleName
    private lateinit var biometricPrompInstance: BiometricPrompt
    private lateinit var biometricUtils: BiometricUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPinLoginBinding.inflate(layoutInflater)
        mCurrentActivity = this.javaClass.name
        sharedPref = SharedPref(this)
        biometricUtils = BiometricUtils(this)
        fromContext = intent.getStringExtra("from").toString()

        binding.tv0.setOnClickListener(this)
        binding.tv1.setOnClickListener(this)
        binding.tv2.setOnClickListener(this)
        binding.tv3.setOnClickListener(this)
        binding.tv4.setOnClickListener(this)
        binding.tv5.setOnClickListener(this)
        binding.tv6.setOnClickListener(this)
        binding.tv7.setOnClickListener(this)
        binding.tv8.setOnClickListener(this)
        binding.tv9.setOnClickListener(this)
        binding.tvForgot.setOnClickListener(this)
        binding.ivCancel.setOnClickListener(this)
        binding.ivOk.setOnClickListener(this)
        binding.ivFingerprint.setOnClickListener(this)
        setContentView(binding.root)
        if (sharedPref.isBiometricEnabled()) {
            binding.ivFingerprint.visibility = VISIBLE
        } else {
            binding.ivFingerprint.visibility = GONE
        }
        biometricUtils.isBiometricPromptEnabled
        biometricPrompInstance = instanceOfBiometricPrompt()

        if (fromContext == Constants.LOGOUT) {
            binding.tvTitle.text = getString(R.string.logout_msg)
        }

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_1 -> setCode(resources.getString(R.string.one))
            R.id.tv_2 -> setCode(resources.getString(R.string.two))
            R.id.tv_3 -> setCode(resources.getString(R.string.three))
            R.id.tv_4 -> setCode(resources.getString(R.string.four))
            R.id.tv_5 -> setCode(resources.getString(R.string.five))
            R.id.tv_6 -> setCode(resources.getString(R.string.six))
            R.id.tv_7 -> setCode(resources.getString(R.string.seven))
            R.id.tv_8 -> setCode(resources.getString(R.string.eight))
            R.id.tv_9 -> setCode(resources.getString(R.string.nine))
            R.id.tv_0 -> setCode(resources.getString(R.string.zero))
            R.id.iv_cancel -> deleteCode()
            R.id.iv_ok -> validatePin()
            R.id.iv_fingerprint -> {

                val canAuthenticate =
                    androidx.biometric.BiometricManager.from(this).canAuthenticate()
                if (canAuthenticate == androidx.biometric.BiometricManager.BIOMETRIC_SUCCESS) {
                    biometricPrompInstance.authenticate(biometricUtils.getPromptInfo())
                } else {
                    Log.d(tag, "could not authenticate because: $canAuthenticate")
                }
            }
        }
    }


    private fun setCode(num: String) {
        pin += num
        codeCount++
        setCodeImage(codeCount, true)


        Log.e("pinLogin", "$pin--count--$codeCount")
    }

    private fun deleteCode() {
        if (pin.isNotEmpty()) {
            pin = pin.substring(0, pin.length - 1)
            setCodeImage(codeCount, false)
            codeCount--
            Log.e("pinLogin", "$pin--count--$codeCount")
        }
    }

    private fun setCodeImage(count: Int, filled: Boolean) {
        when (count) {
            1 -> setImageTint(binding.iv1, filled)
            2 -> setImageTint(binding.iv2, filled)
            3 -> setImageTint(binding.iv3, filled)
            4 -> setImageTint(binding.iv4, filled)
            5 -> setImageTint(binding.iv5, filled)
            6 -> {
                setImageTint(binding.iv6, filled)
                if (filled) {
                    validatePin()
                }
            }

            0 -> {
                setImageTint(binding.iv6, filled)
                setImageTint(binding.iv5, filled)
                setImageTint(binding.iv4, filled)
                setImageTint(binding.iv3, filled)
                setImageTint(binding.iv2, filled)
                setImageTint(binding.iv1, filled)
            }
        }

    }


    private fun setImageTint(iv: ImageView, filled: Boolean) {
        if (filled) {
            iv.setImageResource(R.drawable.circle_shape_filled)
        } else {
            iv.setImageResource(R.drawable.circle_outline)
        }
    }

    private fun validatePin() {
        if (pin == sharedPref.getPin()) {
            goNext()
        } else {
            Utils.showToast(this, "Invalid Pin. Retry!")
            reset()
        }
    }

    private fun reset() {
        Timer("Validate Pin", false).schedule(300) {
            pin = ""
            codeCount = 0
            runOnUiThread {
                setCodeImage(codeCount, false)
            }
//            Utils.vibratePhone(this@PinLoginActivity)
        }

    }

    private fun goNext() {
        Log.e("pin_from", fromContext)
        when (fromContext) {
            "splash" -> {
                val i = Intent(this, HomeActivity::class.java)
                i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(i)
            }
            Constants.LOGOUT, Constants.VALIDATE -> {
                setResult(RESULT_OK)
            }
            "recover" -> {
                sharedPref.setLogin(true)
                startActivity(Intent(this, HomeActivity::class.java))
            }
/*            Constants.VERIFICATION -> {
                setResult(VERIFY_REQUEST_CODE)
            }*/
        }
        finish()
//        finishAfterTransition()
    }

    override fun onBackPressed() {
        if (fromContext == Constants.VALIDATE) {
            if (doubleBackToExitPressedOnce) {
                finishAndRemoveTask()
                finishAffinity()
                return
            }
            this.doubleBackToExitPressedOnce = true
            Toast.makeText(this, "Please click back again to exit", Toast.LENGTH_SHORT).show()
            Handler(Looper.getMainLooper()).postDelayed({
                doubleBackToExitPressedOnce = false
            }, 2000)
        } else {
            super.onBackPressed()
        }
    }


    private fun instanceOfBiometricPrompt(): BiometricPrompt {
        val executor = ContextCompat.getMainExecutor(this)

        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Utils.showToast(this@PinLoginActivity, "$errorCode :: $errString")
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Utils.showToast(
                    this@PinLoginActivity,
                    "Authentication failed for an unknown reason"
                )
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
//                Utils.showToast(this@PinLoginActivity, "Authentication was successful")
                goNext()
            }
        }

        return BiometricPrompt(this, executor, callback)
    }


}