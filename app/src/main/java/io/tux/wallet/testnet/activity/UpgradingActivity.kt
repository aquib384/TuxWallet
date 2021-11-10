package io.tux.wallet.testnet.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import io.tux.wallet.testnet.R
import io.tux.wallet.testnet.databinding.ActivityUpgradingBinding
import io.tux.wallet.testnet.model.createEntropy.EntropyResponse
import io.tux.wallet.testnet.utils.CommonFunctions
import io.tux.wallet.testnet.utils.Constants.KEYLIST
import io.tux.wallet.testnet.utils.Constants.MNEMONICS
import io.tux.wallet.testnet.utils.SharedPref
import io.tux.wallet.testnet.viewModels.OnBoardingViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class UpgradingActivity : AppCompatActivity(), View.OnClickListener {
    private var list: ArrayList<String>? = null
    lateinit var binding: ActivityUpgradingBinding
    private val onBoardingViewModel: OnBoardingViewModel by viewModels()

    @Inject
    lateinit var session: SharedPref
    private lateinit var mnemonics: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpgradingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnCancel.setOnClickListener(this)
        binding.btnUpgrade.setOnClickListener(this)
        binding.ivClose.setOnClickListener(this)
        list = intent.getStringArrayListExtra(KEYLIST)
        mnemonics = intent.getStringExtra(MNEMONICS).toString()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_upgrade -> createEntropy()
            R.id.btn_cancel, R.id.iv_close -> {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
                finishAndRemoveTask()
            }
        }
    }


    private fun createEntropy() {
        // easy easy easy easy easy easy easy easy easy easy easy eager
        binding.progressBar.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.Main).launch {
            kotlin.runCatching {
                val map = mapOf<String, Any?>(
                    "mnemonic" to mnemonics.trim(),
                    "language" to session.getMnemonicLang()
                )
                onBoardingViewModel.createEntropy(
                    map
                )
            }.onSuccess {
                val response = JSONObject(it?.string()).get("data").toString()
                val data = Gson().fromJson(response, EntropyResponse::class.java)
                CommonFunctions.setEntropyData(data, session)
                startActivity(
                    Intent(
                        this@UpgradingActivity,
                        SetPinActivity::class.java
                    ).putStringArrayListExtra(
                        KEYLIST, list
                    )
                )
            }.onFailure {
                Log.i(javaClass.name, "createEntropy: ${it.message}")
            }
            binding.progressBar.visibility = View.GONE
        }
    }

}