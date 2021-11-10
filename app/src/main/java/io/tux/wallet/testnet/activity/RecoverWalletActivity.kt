package io.tux.wallet.testnet.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import io.tux.wallet.testnet.R
import io.tux.wallet.testnet.adapter.RecoverKeyAdapter
import io.tux.wallet.testnet.apis.ApiInterface
import io.tux.wallet.testnet.databinding.ActivityRecoverWalletBinding
import io.tux.wallet.testnet.model.createEntropy.EntropyResponse
import io.tux.wallet.testnet.utils.*
import io.tux.wallet.testnet.utils.Constants.KEYLIST
import io.tux.wallet.testnet.viewModels.OnBoardingViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@AndroidEntryPoint
class RecoverWalletActivity : AppCompatActivity(), View.OnClickListener {
    private var tag: String = RecoverWalletActivity::class.java.simpleName
    lateinit var binding: ActivityRecoverWalletBinding
    lateinit var gridLayoutManager: GridLayoutManager
    lateinit var recoverKeyAdapter: RecoverKeyAdapter
    var keyList = ArrayList<String>()
    private val onBoardingViewModel: OnBoardingViewModel by viewModels()

    @Inject
    lateinit var apiInterface: ApiInterface

    @Inject
    lateinit var sharedPref: SharedPref
    private var mnemoinc: String = ""
//    "mnemonics":{"11":"young","1":"weekend","12":"enlist","2":"gift","3":"curious","4":"dry","5":"kit","6":"surround","7":"west","8":"defy","9":"muscle","10":"private"}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecoverWalletBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = SharedPref(this)
        recoverKeyAdapter = RecoverKeyAdapter(this@RecoverWalletActivity)
        binding.rvPhrase.apply {
            gridLayoutManager = GridLayoutManager(this@RecoverWalletActivity, 3)
            layoutManager = gridLayoutManager
            adapter = recoverKeyAdapter
        }
        binding.btnConfirm.setOnClickListener(this)
        binding.ivBack.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_confirm -> {
                validateKey()
            }
            R.id.iv_back -> finish()
        }
    }


    private fun validateKey() {
        keyList.clear()
        for (i in 0..11) {
            val view: View? = binding.rvPhrase.findViewHolderForAdapterPosition(i)?.itemView;
            val et: EditText? = view?.findViewById(R.id.et_key)
            val phrase = et?.text.toString().trim()
            if (phrase.isNotEmpty()) {
                keyList.add(phrase)
            }
        }
        Log.e(KEYLIST, keyList.toString() + keyList.size)
        if (keyList.size == 12) {
            for (i in 0 until keyList.size) {
                var element = keyList[i]
                mnemoinc = "$mnemoinc $element"
            }
            Log.e("mnemonic", mnemoinc)
            if (NetworkManager.isConnected(binding.root, this)) {
                createEntropy()
            }

        } else {
            Utils.showToast(this, "Enter all 12 words to continue")
        }
    }

/*
    @SuppressLint("HardwareIds")
    private fun callRecoverApi() {
        binding.progressBar.visibility = VISIBLE
        val map = HashMap<String?, Any?>()
        val deviceId = Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ANDROID_ID
        )
        map[Constants.MNEMONICS] = Utils.getHashMap(keyList)
        map[Constants.DEVICE_ID] = deviceId

        Log.e("map", map.toString())
        apiInterface.recoverWallet(map).enqueue(object : Callback<ResponseBody> {
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
                    try {
                        val responseObject = JSONObject(response.body()?.string())
                        when (responseObject.getInt("code")) {
                            200 -> {
                                val resData = responseObject.getJSONObject("data")
                                Log.e("keyList", keyList.toString())
                                mnemoinc = keyList.subList(0, 12).joinToString(" ")
                                Log.e("mnemonic::", mnemoinc + mnemoinc.length)
                                WalletKeyStore.putPhrase(
                                    mnemoinc.toByteArray(),
                                    this@RecoverWalletActivity
                                )
                                sharedPref.setPinEnabled(true)
                                sharedPref.setPin(resData.getString("walletPin"))
                                sharedPref.setWalletId(resData.getString("id"))
                                sharedPref.setFirstTime(false)
                                when (resData.getString("lockType")) {
                                    Constants.BIOMETRIC -> sharedPref.setBioMetricEnables(true)
                                    else -> sharedPref.setBioMetricEnables(false)
                                }
                                var json = resData.getString("coinAddressDetails")
                                sharedPref.setAddresses(json)
                                createEntropy(response.code())
                            }
                        }
                        binding.progressBar.visibility = GONE
                    } catch (e: Exception) {
                        e.printStackTrace()
//                        loadingBar.dismiss()
                    }
                } else {
                    binding.progressBar.visibility = GONE
                    if (response.errorBody() != null)
                        if (response.code() == 400) {
                            createEntropy(response.code())
                        } else
                            Toast.makeText(
                                this@RecoverWalletActivity,
                                JSONObject(response.errorBody()?.string()).get("message")
                                    .toString(),
                                Toast.LENGTH_LONG
                            ).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                t.printStackTrace()
                binding.progressBar.visibility = GONE
                Toast.makeText(this@RecoverWalletActivity, t.message, Toast.LENGTH_LONG).show()
//                loadingBar.dismiss()
            }
        })
    }*/

    private fun createEntropy() {
        CoroutineScope(Dispatchers.Main).launch {
            kotlin.runCatching {
                onBoardingViewModel.createEntropy(
                    mapOf<String, Any?>(
                        "mnemonic" to mnemoinc.trim(),
                        "language" to Utils.getSelectedLanguage(sharedPref)
                    )
                )
            }.onSuccess { entrophyResponse ->
                val result: JSONObject =
                    JSONObject(entrophyResponse.string()).get("data") as JSONObject
                if (result.getInt("status_code") == 400) {
                    Utils.showToast(this@RecoverWalletActivity, result.getString("message"))
                } else {
                    val data = Gson().fromJson(result.toString(), EntropyResponse::class.java)
                    CommonFunctions.setEntropyData(data, sharedPref)
                    /*onBoardingViewModel.getFcmStatus(sharedPref.getWalletId().toString())
                    onBoardingViewModel.fcmStatusModel.observe(this@RecoverWalletActivity) {
                        Log.e("fcmStatus", it.toString())
                        if (it.data == "ON") {
                            sharedPref.setNotifyEnabled(true)
                        } else {
                            sharedPref.setNotifyEnabled(false)
                        }
                    }*/
                    val i =
                        Intent(this@RecoverWalletActivity, SetPinActivity::class.java)
                    i.putStringArrayListExtra(KEYLIST, keyList)
                    startActivity(i)
                    finish()
                    finishAndRemoveTask()
                }
            }.onFailure {
                Utils.showToast(this@RecoverWalletActivity, it.message.toString())
            }
        }
    }


}