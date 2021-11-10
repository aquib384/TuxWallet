package io.tux.wallet.testnet.fragment

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.tux.wallet.testnet.R
import io.tux.wallet.testnet.apis.ApiInterface
import io.tux.wallet.testnet.databinding.FragmentBuyStakesBinding
import io.tux.wallet.testnet.interfaces.CoinInterface
import io.tux.wallet.testnet.model.UpdateTxCountModel
import io.tux.wallet.testnet.model.coins.CoinListModel
import io.tux.wallet.testnet.utils.Constants
import io.tux.wallet.testnet.utils.Constants.ACTIVE
import io.tux.wallet.testnet.utils.Constants.COIN
import io.tux.wallet.testnet.utils.Constants.ID
import io.tux.wallet.testnet.utils.Constants.KEY_ADA
import io.tux.wallet.testnet.utils.Constants.KEY_XTZ
import io.tux.wallet.testnet.utils.Constants.MNEMONIC
import io.tux.wallet.testnet.utils.Constants.TXN_ID
import io.tux.wallet.testnet.utils.Constants.WALLET_DETAILS_ID
import io.tux.wallet.testnet.utils.NetworkManager
import io.tux.wallet.testnet.utils.SharedPref
import io.tux.wallet.testnet.utils.Utils
import io.tux.wallet.testnet.viewModels.CoinMarketViewModel
import io.tux.wallet.testnet.viewModels.StakesViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class BuyStakesFragment : Fragment(), View.OnClickListener, CoinInterface {

    lateinit var binding: FragmentBuyStakesBinding

    @Inject
    lateinit var sharedPref: SharedPref

    @Inject
    lateinit var apiInterface: ApiInterface

    lateinit var coins: List<String>
    private val viewModel: CoinMarketViewModel by activityViewModels()
    private val stkViewModel: StakesViewModel by activityViewModels()
    private var coinsList = mutableListOf<CoinListModel>()
    private lateinit var selectedCoin: CoinListModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBuyStakesBinding.inflate(layoutInflater)
        binding.btnConfirm.setOnClickListener(this)
        binding.ivBack.setOnClickListener(this)
        initViews()
        setObserver()
        return binding.root
    }

    val result =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK)
                if (NetworkManager.isConnected(binding.root, requireContext())) {
                    when (selectedCoin.symbol) {
                        KEY_ADA -> {
                            stkViewModel.stakeAda(
                                mapOf(
                                    MNEMONIC to selectedCoin.adaDetail.adaPhrase
                                )
                            )
                        }
                        KEY_XTZ -> {
                            stkViewModel.stakeXTZ(
                                mapOf(MNEMONIC to sharedPref.getPhrase())
                            )
                        }
                        else -> {
                            Utils.showComingSoonDialog(requireContext())
                            binding.progressBar.visibility = GONE
                        }
                    }
                }
        }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> findNavController().popBackStack()
            R.id.auto_1 -> Utils.showCoinsDialog(requireContext(), coinsList, this, false)
            R.id.btn_confirm -> {
                if (NetworkManager.isConnected(binding.root, requireContext())) {
                    val mnemonic =
                        "common pulse planet enter luxury cannon traffic patient captain bulb make another copy salute must"
                    context?.let { result.launch(Utils.callValidationScreen(it)) }
                }
            }
        }


    }

    private fun initViews() {

        viewModel.coinList.forEach {
            if (it.display.stakeStatus == ACTIVE)
                coinsList.add(it)
        }
        for (i in 0 until coinsList.size) {
            coinsList[i].isSelected = i == 0
            if (coinsList[i].isSelected) {
                setData(coinsList[i])
            }
        }
        binding.auto1.setOnClickListener(this)

        binding.ivBack.setOnClickListener(this)
        binding.btnConfirm.setOnClickListener(this)


    }

    override fun coinCallBack(position: Int) {
        super.coinCallBack(position)
        coinsList[position].isSelected = true
        setData(coinsList[position])

        Utils.dismissCoinsDialog()


    }


    private fun setData(element: CoinListModel) {
        selectedCoin = element
        val ic =
            resources.getString(R.string.ic_prefix) + element.symbol.lowercase() + resources.getString(
                R.string.ic_suffix
            )
        binding.layCrypto1.setStartIconDrawable(Utils.getDrawableRes(requireContext(), ic))
        binding.auto1.setText(element.symbol)
        binding.auto1.setCompoundDrawablesRelativeWithIntrinsicBounds(
            0,
            0,
            R.drawable.ic_arrow_drop_down,
            0
        )
        binding.tvAvailableBal.text =
            resources.getString(R.string.balance) + " : " + element.balance?.toBigDecimal()

//        if (element.symbol == KEY_ADA) {
//            binding.cardRanking.visibility = VISIBLE
//        } else {
//            binding.cardRanking.visibility = GONE
//        }


    }

    private fun setObserver() {
        stkViewModel.errorMsg.observe(viewLifecycleOwner) {
            context?.let { it1 -> Utils.showToast(it1, it) }
        }

        stkViewModel.stakesResponse.observe(viewLifecycleOwner, { stakeResponse ->
            Log.e("stakeResponse", stakeResponse.data.toString())
            if (stakeResponse.data?.tx_id != null) {
                val msg = stakeResponse.msg
                stkViewModel.saveStake(
                    hashMapOf(
                        COIN to selectedCoin.symbol,
                        ID to 0,
                        TXN_ID to stakeResponse.data.tx_id,
                        WALLET_DETAILS_ID to sharedPref.getWalletId()
                    )

                )
                binding.root.visibility = GONE
                msg?.let { Utils.showToast(requireContext(), it) }
                findNavController().popBackStack()
            } else {
                Utils.showToast(requireContext(), resources.getString(R.string.signup_error))
            }
        })

        viewModel.coinList
    }

    fun addTxnCounts(symbol: String) {
        apiInterface.updateTxCount(
            hashMapOf(
                Constants.TXSTATS to false,
                Constants.STAKESTATS to true,
                Constants.COINSYMBOL to symbol
            )
        ).enqueue(object : Callback<UpdateTxCountModel> {
            override fun onResponse(
                call: Call<UpdateTxCountModel>,
                response: Response<UpdateTxCountModel>
            ) {
                Log.e("txnLog", response.body()?.data.toString())
            }

            override fun onFailure(call: Call<UpdateTxCountModel>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

}