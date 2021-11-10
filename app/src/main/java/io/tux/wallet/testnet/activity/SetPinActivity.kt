package io.tux.wallet.testnet.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import io.tux.wallet.testnet.R
import io.tux.wallet.testnet.apis.ApiInterface
import io.tux.wallet.testnet.databinding.ActivitySetPinBinding
import io.tux.wallet.testnet.model.address.ADAAddressModel
import io.tux.wallet.testnet.model.wallet.AdaDetails
import io.tux.wallet.testnet.model.wallet.CoinAddressItem
import io.tux.wallet.testnet.utils.*
import io.tux.wallet.testnet.utils.Constants.ADAADDRES
import io.tux.wallet.testnet.utils.Constants.ADA_ADDRES_DETAILS
import io.tux.wallet.testnet.utils.Constants.ADA_MNEMONIC
import io.tux.wallet.testnet.utils.Constants.ADA_PHRASE
import io.tux.wallet.testnet.utils.Constants.ADA_WALLET_ID
import io.tux.wallet.testnet.utils.Constants.ADDRESS
import io.tux.wallet.testnet.utils.Constants.COIN
import io.tux.wallet.testnet.utils.Constants.CONTRACT_ID
import io.tux.wallet.testnet.utils.Constants.DEVICE_ID
import io.tux.wallet.testnet.utils.Constants.ID
import io.tux.wallet.testnet.utils.Constants.IS_ADA
import io.tux.wallet.testnet.utils.Constants.KEYLIST
import io.tux.wallet.testnet.utils.Constants.KEY_ADA
import io.tux.wallet.testnet.utils.Constants.KEY_XLM
import io.tux.wallet.testnet.utils.Constants.MNEMONIC
import io.tux.wallet.testnet.utils.Constants.NON_BIOMETRIC
import io.tux.wallet.testnet.utils.Constants.PASSPHRASE
import io.tux.wallet.testnet.utils.Constants.PRIVATE_KEY
import io.tux.wallet.testnet.utils.Constants.SECRET_KEY
import io.tux.wallet.testnet.utils.Constants.USER_DETAILS_ID
import io.tux.wallet.testnet.viewModels.OnBoardingViewModel
import wallet.core.jni.CoinType
import wallet.core.jni.HDWallet
import javax.inject.Inject

@AndroidEntryPoint
class SetPinActivity : AppCompatActivity() {
    lateinit var binding: ActivitySetPinBinding
    private lateinit var biometricPrompInstance: BiometricPrompt
    var mnemoinc = ""

    @Inject
    lateinit var sharedPref: SharedPref
    private var keyList = ArrayList<String>()
    private var mapList = ArrayList<HashMap<String, Any>>()
    private lateinit var deviceId: String
    private val onBoardingViewModel: OnBoardingViewModel by viewModels()
    private lateinit var myWallet: HDWallet
    private var isOldUser: Boolean = false

    @Inject
    lateinit var apiInterface: ApiInterface
    private var biometricEnabled = NON_BIOMETRIC


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetPinBinding.inflate(layoutInflater)
        setContentView(binding.root)
        keyList = intent.getStringArrayListExtra(KEYLIST) as ArrayList<String>
        Log.e("keyList", keyList.toString())
        myWallet = if (sharedPref.getEntropy().isNullOrEmpty()) {
            HDWallet(128, "")
        } else {
            HDWallet(sharedPref.getEntropy()?.toByteArray(), "")
        }
        if (NetworkManager.isConnected(binding.root, this)) {
            var passPhrase = ""
            keyList.forEach {
                passPhrase += "$it "
            }
            Log.e("paspahrse", passPhrase)

            onBoardingViewModel.getADAAddress(
                apiInterface,
                hashMapOf(PASSPHRASE to passPhrase.trim())
            )
            onBoardingViewModel.getStellerAddress(
                apiInterface,
                hashMapOf(MNEMONIC to passPhrase.trim())
            )
        }
        initViews()
//        validateUser()
        setObservers()
    }

/*
    private fun validateUser() {
        isOldUser =
            sharedPref.isFirstTime() && !WalletKeyStore.getPhraseString(this).isNullOrEmpty()
        binding.layOld.visibility = if (isOldUser) {
            VISIBLE
        } else GONE
    }*/

    @SuppressLint("HardwareIds")
    private fun initViews() {
        deviceId = Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ANDROID_ID
        )
        Log.e("deviceid", deviceId)
        sharedPref.setDeviceId(deviceId)
        binding.switchBiometrics.isChecked = false


        biometricPrompInstance = instanceOfBiometricPrompt()
        binding.etPassword.doOnTextChanged { text, _, _, _ ->
            val isPassValidate: Boolean =
                Validations.pinValidate(text.toString(), binding.layPassword)
            if (isPassValidate) {
                binding.layPassword.error = null
                binding.layPassword.isErrorEnabled = false
            }

        }
        binding.etCpassword.doOnTextChanged { text, _, _, _ ->
            val isPassValidate: Boolean =
                Validations.pinValidate(text.toString(), binding.layCpassword)
            if (isPassValidate) {
                binding.layCpassword.error = null
                binding.layCpassword.isErrorEnabled = false
            }
            if (text.toString().equals(binding.etPassword.text)) {
                binding.layCpassword.isErrorEnabled = false
                binding.layPassword.isErrorEnabled = false
            }
        }
        binding.ivBack.setOnClickListener { finish() }
        binding.btnSubmit.setOnClickListener {
            if (validate(
//                    binding.etOld.text.toString(),
                    binding.etPassword.text.toString(),
                    binding.etCpassword.text.toString()
                )
            ) {
                binding.layCpassword.isErrorEnabled = false
                binding.layPassword.isErrorEnabled = false
                binding.layOld.isErrorEnabled = false
                sharedPref.setPinEnabled(true)
                sharedPref.setFirstTime(false)
                sharedPref.setPin(binding.etPassword.text.toString())
                sharedPref.setLogin(true)
                startActivity(Intent(this@SetPinActivity, HomeActivity::class.java))
                finishAffinity()
                finishAndRemoveTask()
//                sharedPref.setWalletId(resData.getString("id"))
                /* if (NetworkManager.isConnected(binding.root, this)) {
                     callRegisterApi()
                 }*/
            }
        }
    }

    private fun validate(pass: String, cpass: String): Boolean {
        var isValidate = true
        if (pass.isEmpty()) {
            isValidate = false
            binding.layPassword.error = resources.getString(R.string.pin_error)
        }
        if (cpass.isEmpty()) {
            isValidate = false
            binding.layCpassword.error = resources.getString(R.string.cpin_error)
        } else {
            if (pass != cpass) {
                isValidate = false
                binding.layCpassword.error = resources.getString(R.string.pins_should_match)
            }
        }
//        if (isOldUser) {
//            if (oldPass.isEmpty()) {
//                isValidate = false
//                binding.layOld.error = resources.getString(R.string.pin_error)
//            } else if (oldPass.toSHA256() != sharedPref.getString(Constants.SP_PIN_CODE_LOGIN)) {
//                isValidate = false
//                binding.layOld.error = "Incorrect pin"
//            }
//        }
        return isValidate
    }


    private fun instanceOfBiometricPrompt(): BiometricPrompt {
        val executor = ContextCompat.getMainExecutor(this)

        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Utils.showToast(this@SetPinActivity, "$errorCode :: $errString")
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Utils.showToast(this@SetPinActivity, "Authentication failed for an unknown reason")
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                Utils.showToast(this@SetPinActivity, "Authentication was successful")
                SharedPref(this@SetPinActivity).setBioMetricEnables(true)
            }
        }
        return BiometricPrompt(this, executor, callback)
    }

/*
    private fun callRegisterApi() {
        binding.progressBar.visibility = VISIBLE
        binding.btnSubmit.isEnabled = false
        val map = HashMap<String?, Any?>()
//        map[MNEMONICS] = Utils.getHashMap(keyList.dropLast(3) as java.util.ArrayList<String>)
        map[MNEMONICS] = Utils.getHashMap(keyList as java.util.ArrayList<String>)
        map[WALLET_PIN] = binding.etPassword.text.toString()
        map[DEVICE_ID] = deviceId
        map[LOCK_TYPE] = biometricEnabled
        Log.e("map", map.toString())
        createWallet(map)

    }


    private fun createWallet(hashMap: HashMap<String?, Any?>) {
        apiInterface.createNewWallet(hashMap).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    Log.e(
                        "responseCode",
                        response.code().toString() + "\nresponseBody" + response.body()
                            .toString() + "error" + response.errorBody()?.string()
                    )
//                    Toast.makeText(context, "Successfully Transferred", Toast.LENGTH_LONG).show()

                    try {
                        var responseObject = JSONObject(response.body()?.string())
                        when (responseObject.getInt("code")) {
                            200 -> {
                                mapAddress(responseObject)
                            }
                            400 -> {
                                Utils.showToast(
                                    this@SetPinActivity,
                                    responseObject.getString("message")
                                )
                            }
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    if (response.errorBody() != null)
                        Toast.makeText(
                            this@SetPinActivity,
                            JSONObject(response.errorBody()?.string()).get("message").toString(),
                            Toast.LENGTH_LONG
                        ).show()
                }

                binding.progressBar.visibility = GONE
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(this@SetPinActivity, t.message, Toast.LENGTH_LONG).show()
//                loadingBar.dismiss()
            }
        })

        binding.btnSubmit.isEnabled = true
    }

    fun mapAddress(responseObject: JSONObject) {
        var hash = responseObject.getString("message")
        var resData = responseObject.getJSONObject("data")
        Log.e("gson", responseObject.toString() + "" + hash)
        Log.e("keyList", keyList.toString())
        mnemoinc = keyList.subList(0, 12).joinToString(" ")
        Log.e("mnemonic::", mnemoinc + mnemoinc.length)
        putPhrase(mnemoinc.toByteArray(), this)

        when (resData.getString("lockType")) {
            BIOMETRIC -> sharedPref.setBioMetricEnables(true)
            else -> sharedPref.setBioMetricEnables(false)
        }
        mapList.forEach {
            it[USER_DETAILS_ID] = sharedPref.getWalletId()?.toInt()!!
        }
        Log.e("mapLIst", mapList.toString())
        onBoardingViewModel.mapAddresses1(mapList)
        onBoardingViewModel.mapAddrModel1.observe(this, {
            val gson = Gson()
            val json = gson.toJson(mapList)
            Log.e("json_add", json)
            sharedPref.setAddresses(json)
            onBoardingViewModel.setFcmStatus(
                hashMapOf(
                    DEVICE_ID to sharedPref.getDeviceId(),
                    FCMSTATUS to "ON",
                    ID to 0,
                    MSG to "register",
                    TOKENID to sharedPref.getFCMToken(),
                    USER_DETAILS_ID to sharedPref.getWalletId()
                )
            )
        })
        onBoardingViewModel.saveFcmModel.observe(this) {
            if (it.data != null) {
                Log.e("registerFcm", it.toString())
                if (it.data.fcmStatus == "ON") {
                    sharedPref.setNotifyEnabled(true)
                } else {
                    sharedPref.setNotifyEnabled(false)
                }
                Utils.showToast(this@SetPinActivity, responseObject.getString("message"))
                Log.e("sharedPref add", sharedPref.getAddresses()!!)

            }
        }
    }*/

    private fun setObservers() {
        onBoardingViewModel.adaModel.observe(this, { adaAddressModel ->
            Log.e("ada", adaAddressModel.toString())
            val aList = adaAddressModel.data.address as List<ADAAddressModel.Data.Addres>
            val addList = ArrayList<CoinAddressItem.AdaCoinAddressDetail>()
            val addsList = ArrayList<HashMap<String, Any>>()

            aList.forEach {
                addList.add(CoinAddressItem.AdaCoinAddressDetail(it.id, 0, 0))
                addsList.add(
                    hashMapOf(
                        ADAADDRES to it.id,
                        "coinAddressDetailsId" to 0,
                        "id" to 0
                    )
                )
            }
            Log.e("adaCoinAdddress", addList.toString())
            Log.e("adaCoinAdddressMap", addsList.toString())
            mapList.add(
                hashMapOf(
                    ADA_ADDRES_DETAILS to addsList,
                    ADA_MNEMONIC to adaAddressModel.data.mnemonic.toString(),
                    ADA_PHRASE to adaAddressModel.data.passphrase,
                    ADA_WALLET_ID to adaAddressModel.data.id,
                    ADDRESS to addList[0].adaAddress,
                    COIN to KEY_ADA,
                    CONTRACT_ID to "",
                    DEVICE_ID to deviceId,
                    ID to CoinType.CARDANO.value(),
                    IS_ADA to "YES",
                    PRIVATE_KEY to "",
                    SECRET_KEY to "",
                    USER_DETAILS_ID to 0
                )
            )
        })

        onBoardingViewModel.stellarModel.observe(this, {
            Log.e("stellar", it.toString())
            val adaDetails =
                AdaDetails(ArrayList(), "", "", "")
            mapList.add(
                hashMapOf(
                    ADA_ADDRES_DETAILS to adaDetails.adaCoinAddressDetails,
                    ADA_MNEMONIC to adaDetails.adaMnemonic,
                    ADA_PHRASE to adaDetails.adaPhrase,
                    ADA_WALLET_ID to adaDetails.adaWalletId,
                    ADDRESS to it.data.publicKey,
                    COIN to KEY_XLM,
                    CONTRACT_ID to "",
                    DEVICE_ID to deviceId,
                    ID to CoinType.STELLAR.value(),
                    IS_ADA to "NO",
                    PRIVATE_KEY to Utils.getHexString(
                        myWallet.getKeyForCoin(CoinType.STELLAR).data(), false
                    ),
                    SECRET_KEY to it.data.secretKey,
                    USER_DETAILS_ID to 0
                )
            )
            val gson = Gson()
            val json = gson.toJson(mapList)
            sharedPref.setAddresses(json)
        })

        Log.e("mapsss", mapList.toString())


    }


}