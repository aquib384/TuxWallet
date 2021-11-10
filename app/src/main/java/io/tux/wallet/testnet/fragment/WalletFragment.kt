package io.tux.wallet.testnet.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import io.tux.wallet.testnet.R
import io.tux.wallet.testnet.adapter.WalletCoinsAdapter
import io.tux.wallet.testnet.databinding.FragmentWalletBinding
import io.tux.wallet.testnet.interfaces.CoinInterface
import io.tux.wallet.testnet.model.coinResponseModel.Display
import io.tux.wallet.testnet.model.coinResponseModel.Raw
import io.tux.wallet.testnet.model.coins.CoinListModel
import io.tux.wallet.testnet.utils.CommonFunctions.convertIntoBalance
import io.tux.wallet.testnet.utils.Constants.COIN
import io.tux.wallet.testnet.utils.Constants.DISPLAY
import io.tux.wallet.testnet.utils.Constants.KEY_TUXC
import io.tux.wallet.testnet.utils.Constants.RAW
import io.tux.wallet.testnet.utils.NetworkManager
import io.tux.wallet.testnet.utils.SharedPref
import io.tux.wallet.testnet.utils.Utils.sortCoins
import io.tux.wallet.testnet.viewModels.CoinMarketViewModel
import org.json.JSONObject
import java.lang.reflect.Type
import java.text.DecimalFormat
import javax.inject.Inject


@AndroidEntryPoint
class WalletFragment : Fragment(), View.OnClickListener, CoinInterface {
    lateinit var binding: FragmentWalletBinding
    private var walletAdapter: WalletCoinsAdapter? = null

    @Inject
    lateinit var sharedPref: SharedPref
    private val viewModel: CoinMarketViewModel by activityViewModels()
    private var displayList = mutableListOf<Display>()
    private var rawList = mutableListOf<Raw>()
    var coinList = ArrayList<CoinListModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentWalletBinding.inflate(layoutInflater)
        binding.linTransaction.setOnClickListener(this)
        binding.linReceive.setOnClickListener(this)
        binding.linSend.setOnClickListener(this)
        binding.ivSearch.setOnClickListener(this)
        coinList = viewModel.coinList
        apiCall()
        setObservers()
        setCoinsAdapter(coinList)
        binding.swipe.setOnRefreshListener {
            apiCall()
            binding.recyclerview.visibility = GONE
            viewModel.getErcCoins()
            if (binding.swipe.isRefreshing) {
                binding.swipe.isRefreshing = false
            }
        }
        return binding.root
    }

    private fun apiCall() {
        if (NetworkManager.isConnected(binding.root, requireContext())) {
            binding.progressBar.visibility = VISIBLE
            binding.tvBal.visibility = GONE
            binding.shimmerLayout.visibility = VISIBLE
//            binding.shimmerWallet.visibility = VISIBLE
            binding.shimmerLayout.startShimmer()
//            binding.shimmerWallet.startShimmer()
        }
    }


    private fun setCoinsAdapter(list: ArrayList<CoinListModel>) {
        walletAdapter = WalletCoinsAdapter(requireContext(), list, this@WalletFragment)
        binding.recyclerview.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = walletAdapter
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.lin_transaction -> findNavController().navigate(R.id.wallet_transaction)
            R.id.iv_search -> findNavController().navigate(R.id.wallet_search)
            R.id.lin_send -> findNavController().navigate(R.id.nav_send)
            R.id.lin_receive -> findNavController().navigate(R.id.nav_receive)
        }
    }


    override fun coinCallBack(position: Int) {
        val args = Bundle()
        if (viewModel.coinList[position].symbol != KEY_TUXC) {
            args.putSerializable(COIN, viewModel.coinList[position])
            findNavController().navigate(R.id.nav_coin_detail, args)
        }
//        findNavController().navigate(R.id.nav_send_hisrty,args)
    }

    private fun setObservers() {
        viewModel.coinModel.observe(
            viewLifecycleOwner, { coinResponse ->
                try {
                    val jsonObject = JSONObject(Gson().toJson(coinResponse.data))
                    Log.e("cryptoModelObject", jsonObject.toString())
                    val displayListType: Type = object : TypeToken<List<Display>>() {}.type
                    val rawListType: Type = object : TypeToken<List<Raw>>() {}.type
                    displayList =
                        Gson().fromJson(jsonObject.getString(DISPLAY), displayListType)
                    rawList = Gson().fromJson(jsonObject.getString(RAW), rawListType)
                    Log.e("displayList", displayList.toString())
                    Log.e("rawList", rawList.toString())
//                    walletAmount = 0.0
                    coinList.forEach {
                        val coin = it
                        for (j in 0 until displayList.size) {
                            if (coin.symbol == displayList[j].coin && coin.symbol == rawList[j].coin) {
                                coin.display = displayList[j]
                                coin.raw = rawList[j]
                            } else {

                            }
                        }
                    }
                    coinList.sortCoins()
                    binding.recyclerview.visibility = VISIBLE

//                    viewModel.coinList.sortWith(compareBy<CoinListModel> {it.balance}.reversed().thenByDescending{it.raw.currencies[0].data.PRICE.toString().toDouble()})

//                    coinList.sortWith(compareBy<CoinListModel> {it.raw.currencies[0].data.PRICE.toString().toDouble()}.thenBy{it.balance }) as ArrayList<CoinListModel>
                    walletAdapter?.notifyDataSetChanged()
//                    setWalletBalance()
//                   Log.e("coinsListViewModelDISPLAY",coinList[0].display.toString())
//                   Log.e("coinsListViewModelRAW",coinList[0].raw.toString())
//                    Log.e("coinList", viewModel.coinList.toString())
                    binding.progressBar.visibility = GONE
//                    binding.shimmerWallet.stopShimmer()
//                    binding.shimmerWallet.visibility =GONE
                    binding.cardRanking.visibility = VISIBLE
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            })
        viewModel.balanceIndex.observe(viewLifecycleOwner) { balIndex ->
//            viewModel.coinList.sortWith(compareBy<CoinListModel> { it.raw.currencies[0].data.PRICE.toString().toDouble() }.thenBy { it.balance })
            walletAdapter?.notifyItemChanged(balIndex)
        }

        viewModel.walletAmount.observe(viewLifecycleOwner) {
            binding.tvBal.text = sharedPref.getCurrencySymbol() + " " + it.convertIntoBalance()
            binding.shimmerLayout.stopShimmer()
            binding.shimmerLayout.visibility = GONE
            binding.tvBal.visibility = VISIBLE
        }
/*
        viewModel.ethBalance.observe(viewLifecycleOwner, {
            val index = Utils.getIndexOfCoin(KEY_ETH, viewModel.coinList)
            walletAdapter?.notifyItemChanged(index)
        })
        viewModel.trxBalance.observe(viewLifecycleOwner, {
            val index = Utils.getIndexOfCoin(KEY_TRX, viewModel.coinList)
            walletAdapter?.notifyItemChanged(index)
        })

        viewModel.btcBalance.observe(viewLifecycleOwner, { responseModel ->
            val index = Utils.getIndexOfCoin(KEY_BTC, viewModel.coinList)
            walletAdapter?.notifyItemChanged(index)
        })
        viewModel.bchBalance.observe(viewLifecycleOwner, {
            val index = Utils.getIndexOfCoin(KEY_BCH, viewModel.coinList)
            walletAdapter?.notifyItemChanged(index)
        })
        viewModel.xrpBalance.observe(viewLifecycleOwner, {
            val index = Utils.getIndexOfCoin(KEY_XRP, viewModel.coinList)
            walletAdapter?.notifyItemChanged(index)
        })
        viewModel.neoBalance.observe(viewLifecycleOwner, {
            val index = Utils.getIndexOfCoin(KEY_NEO, viewModel.coinList)
            walletAdapter?.notifyItemChanged(index)
        })
        viewModel.xlmBalance.observe(viewLifecycleOwner, {
            val index = Utils.getIndexOfCoin(KEY_XLM, viewModel.coinList)
            walletAdapter?.notifyItemChanged(index)
        })
        viewModel.eosBalance.observe(viewLifecycleOwner, {
            val index = Utils.getIndexOfCoin(KEY_EOS, viewModel.coinList)
            walletAdapter?.notifyItemChanged(index)
        })
        viewModel.atomBalance.observe(viewLifecycleOwner, {
            val index = Utils.getIndexOfCoin(KEY_ATOM, viewModel.coinList)
            walletAdapter?.notifyItemChanged(index)
        })
        viewModel.algoBalance.observe(viewLifecycleOwner, {
            val index = Utils.getIndexOfCoin(KEY_ALGO, viewModel.coinList)
            walletAdapter?.notifyItemChanged(index)
        })
        viewModel.ltcBalance.observe(viewLifecycleOwner, {
            val index = Utils.getIndexOfCoin(KEY_LTC, viewModel.coinList)
            walletAdapter?.notifyItemChanged(index)
        })
        viewModel.linkBalance.observe(viewLifecycleOwner, {
            val index = Utils.getIndexOfCoin(KEY_LINK, viewModel.coinList)
            walletAdapter?.notifyItemChanged(index)
        })
        viewModel.bandBalance.observe(viewLifecycleOwner, {
            val index = Utils.getIndexOfCoin(KEY_BAND, viewModel.coinList)
            walletAdapter?.notifyItemChanged(index)
        })
        viewModel.uniBalance.observe(viewLifecycleOwner, {
            val index = Utils.getIndexOfCoin(KEY_UNI, viewModel.coinList)
            walletAdapter?.notifyItemChanged(index)
        })
        viewModel.sushiBalance.observe(viewLifecycleOwner, {
            val index = Utils.getIndexOfCoin(KEY_SUSHI, viewModel.coinList)
            walletAdapter?.notifyItemChanged(index)
        })
        viewModel.aaveBalance.observe(viewLifecycleOwner, {
            val index = Utils.getIndexOfCoin(KEY_AAVE, viewModel.coinList)
            walletAdapter?.notifyItemChanged(index)
        })
        viewModel.adaBalance.observe(viewLifecycleOwner, {
            Log.e("ada_balance", it.toString())
            val index = Utils.getIndexOfCoin(KEY_ADA, viewModel.coinList)
            walletAdapter?.notifyItemChanged(index)
        })
        viewModel.fileBalance.observe(viewLifecycleOwner, {
            val index = Utils.getIndexOfCoin(KEY_FIL, viewModel.coinList)
            walletAdapter?.notifyItemChanged(index)
        })
        viewModel.xtzBalance.observe(viewLifecycleOwner, {
            val index = Utils.getIndexOfCoin(KEY_XTZ, viewModel.coinList)
            walletAdapter?.notifyItemChanged(index)
        })*/

    }


    /* private fun getCoinBalance(coinList: ArrayList<CoinListModel>) {
         CoroutineScope(Main).launch {
             coinList.forEach { coin ->
                 viewModel.getERC20Balance(coin)
                 delay(30000)
             }
         }
     }*/


}