package io.tux.wallet.testnet.activity


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import io.tux.wallet.testnet.R
import io.tux.wallet.testnet.adapter.CreateWalletKeyAdapter
import io.tux.wallet.testnet.databinding.ActivityCreateWalletBinding
import io.tux.wallet.testnet.model.createEntropy.EntropyData
import io.tux.wallet.testnet.security.WalletKeyStore
import io.tux.wallet.testnet.utils.Constants.KEYLIST
import io.tux.wallet.testnet.utils.Constants.RECOVERY
import io.tux.wallet.testnet.utils.NetworkManager
import io.tux.wallet.testnet.utils.SharedPref
import io.tux.wallet.testnet.utils.Utils
import io.tux.wallet.testnet.viewModels.OnBoardingViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import okhttp3.internal.trimSubstring
import org.json.JSONObject
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class CreateWalletActivity : AppCompatActivity(), View.OnClickListener {
    private var from: String? = null
    lateinit var binding: ActivityCreateWalletBinding
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var keyAdapter: CreateWalletKeyAdapter
    private val onBoardingViewModel: OnBoardingViewModel by viewModels()
    private lateinit var phrase: List<String>

    @Inject
    lateinit var session: SharedPref
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateWalletBinding.inflate(layoutInflater)
        from = intent.getStringExtra("from")
        setContentView(binding.root)
        if (from == RECOVERY) {
            binding.btnConfirm.visibility = GONE
            WalletKeyStore.getPhraseString(this)?.split(" ")?.toList()?.let { setWalletPhrase(it) }
        } else
            if (NetworkManager.isConnected(binding.root, this)) {
                createEntropy()
            }
        binding.btnConfirm.setOnClickListener(this)
        binding.ivBack.setOnClickListener(this)

    }

    private fun setWalletPhrase(mnemonics: List<String>) {
//        val wallet = HDWallet("fence gentle initial employ balcony brass zero concert fatigue base blind speak", "")
//        val wallet = HDWallet("fence gentle initial employ balcony brass zero concert fatigue base blind speak", "")
        if (from!= RECOVERY) {
            if (onBoardingViewModel.keyList.value.isNullOrEmpty()) {
                phrase = mnemonics
                onBoardingViewModel.setKeyList(phrase)
            } else {
                phrase = onBoardingViewModel.keyList.value!!
            }
        }


        keyAdapter = CreateWalletKeyAdapter(this, mnemonics as ArrayList<String>)
        binding.rvPhrase.apply {
            gridLayoutManager = GridLayoutManager(this@CreateWalletActivity, 3)
            layoutManager = gridLayoutManager
            adapter = keyAdapter
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_confirm -> {
                val intent = Intent(
                    this,
                    PhraseVerificationActivity::class.java
                )
                intent.putStringArrayListExtra(KEYLIST, phrase as ArrayList<String>)
                startActivity(intent)
            }
            R.id.iv_back -> finish()
        }
    }

    private fun createEntropy() {
        // easy easy easy easy easy easy easy easy easy easy easy eager
        binding.progressBar.visibility = VISIBLE
        CoroutineScope(Main).launch {
            kotlin.runCatching {
                onBoardingViewModel.createEntropy(
                    mapOf<String, Any?>(
                        "mnemonic" to "",
                        "language" to Utils.getSelectedLanguage(session)
                    )
                )
            }.onSuccess {
                val response = JSONObject(it.string()).get("data").toString()
                val data = Gson().fromJson(response, EntropyData::class.java)
                setData(data)
            }.onFailure {
                Log.i(javaClass.name, "createEntropy: ${it.message}")
            }
            binding.progressBar.visibility = GONE
        }
    }

    private fun setData(data: EntropyData) {
        data.bch?.addresses?.p2wpkh_in_p2sh.let { it?.let { it1 -> session.setBchAddress(it1) } }
        data.bch?.wif.let { it?.let { it1 -> session.setBCHWIF(it1) } }
        data.ltc?.addresses?.p2wpkh_in_p2sh?.let { session.setLtcAddress(it) }
        data.ltc?.wif?.let { session.setLTCWIF(it) }
        data.data?.mnemonic?.split(" ")?.toList()?.let { it1 -> setWalletPhrase(it1) }
        data.data?.entropy?.let { it1 -> session.setEntropy(it1) }
        data.data?.addresses?.p2wpkh_in_p2sh?.let { it1 -> session.setBtcAddress(it1) }
        data.ethereumaddress?.let { it1 -> session.setEthAddress(it1) }
        data.ethereum_private_key?.let { it1 -> session.setEthKey(it1) }
        data.data?.wif?.let { it1 -> session.setWIF(it1) }
    }
}