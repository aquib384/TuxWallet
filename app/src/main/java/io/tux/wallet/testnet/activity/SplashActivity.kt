package io.tux.wallet.testnet.activity

import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import io.tux.wallet.testnet.apis.ApiInterface
import io.tux.wallet.testnet.databinding.ActivitySplashBinding
import io.tux.wallet.testnet.model.LanguageDetectModel
import io.tux.wallet.testnet.security.WalletKeyStore
import io.tux.wallet.testnet.utils.Constants
import io.tux.wallet.testnet.utils.Constants.SPLASH
import io.tux.wallet.testnet.utils.LocaleHelper
import io.tux.wallet.testnet.utils.SharedPref
import io.tux.wallet.testnet.utils.Utils
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import wallet.core.jni.HDWallet
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.schedule

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashBinding
    lateinit var timer: Timer
    lateinit var sharedPref: SharedPref

    @Inject
    lateinit var apiInterface: ApiInterface
    private val TAG = SplashActivity::class.java.simpleName
    var lang = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = SharedPref(this)
//        val phrase = WalletKeyStore.getPhraseString(this)
        val pin = sharedPref.getString(Constants.SP_PIN_CODE_LOGIN)
        val langIndex: Int? = sharedPref.getLanguage()
        lang = when (langIndex) {

            1 -> "ja"
            2 -> "zh"
            else -> "en"
        }
        if (langIndex != null) {
            sharedPref.setLanguage(langIndex)
            LocaleHelper.setLocale(this@SplashActivity, lang)
            Utils.setAppLocale(this@SplashActivity, lang)
        }
//        Log.i(javaClass.name, "onCreate: $phrase $pin $lang $langIndex ")

        init()

    }

    @SuppressLint("HardwareIds")
    fun init() {
        if (!sharedPref.isLogin()) {
            sharedPref.setPhrase(HDWallet(128, "").mnemonic())
        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            sharedPref.setFcmToken(token)
            Log.e("fcm_token", token.toString())
        })
        if (sharedPref.isLightTheme()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        if (sharedPref.getLanguage() == null) {
            sharedPref.setLanguage(0)
            LocaleHelper.setLocale(this@SplashActivity, "EN")
            Utils.setAppLocale(this@SplashActivity, "EN")
        } else {
            LocaleHelper.setLocale(this@SplashActivity, lang)
            Utils.setAppLocale(this@SplashActivity, lang)
        }
        Utils.setDefaultCurrency(this, sharedPref)
        timer = Timer(SPLASH, false)

        goNext()
    }


    private fun goNext() {
        timer.schedule(3000) {
            val mnemonics = WalletKeyStore.getPhraseString(this@SplashActivity)
            when {
                (sharedPref.isFirstTime() && !mnemonics.isNullOrEmpty()) -> {
                    timer.cancel()
                    detectLanguage(mnemonics)
                }
                sharedPref.isLogin() -> {
                    Log.e(
                        "user_dTa",
                        sharedPref.getPhrase() + sharedPref.getDeviceId() + sharedPref.getWalletId()
                    )
                    if (SharedPref(this@SplashActivity).isPinEnabled()) {
                        Utils.gotoPin(this@SplashActivity, "splash")
                        timer.cancel()
                        finish()
                    } else {
                        startActivity(Intent(this@SplashActivity, HomeActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP;
                        })
                        timer.cancel()
                    }
                    finish()
                    finishAndRemoveTask()
                }
                else -> {
                    startActivity(Intent(this@SplashActivity, WelcomeActivity::class.java).apply {
                        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP;
                    })
                    timer.cancel()
                    finish()
                    finishAndRemoveTask()
                }
            }
            timer.cancel()
        }

    }


    private fun detectLanguage(mnemonic: String) {
        apiInterface.detectLang(mnemonic).enqueue(object : Callback<LanguageDetectModel> {
            override fun onResponse(
                call: Call<LanguageDetectModel>,
                response: Response<LanguageDetectModel>
            ) {
                Log.e("LangResponse", response.body().toString())
                if (response.code() == 200) {
                    lang = response.body()?.data?.language.toString()
                    sharedPref.setMnemonicLang(
                        when (lang) {
                            "ja" -> "japanese"
                            "zh" -> "chinese_simplified"
                            else -> "english"
                        }
                    )
                    startActivity(Intent(this@SplashActivity, UpgradingActivity::class.java).apply {
                        putStringArrayListExtra(
                            Constants.KEYLIST,
                            mnemonic.trim().split(" ").toList() as ArrayList<String>?
                        )
                        putExtra(Constants.MNEMONICS, mnemonic)
                    })
                    finish()
                    finishAndRemoveTask()
                }
            }

            override fun onFailure(call: Call<LanguageDetectModel>, t: Throwable) {
                t.printStackTrace()

            }

        })
    }


}


