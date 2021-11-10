package io.tux.wallet.testnet.fragment

import android.content.Intent
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
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import io.tux.wallet.testnet.R
import io.tux.wallet.testnet.activity.GuestModeActivity
import io.tux.wallet.testnet.adapter.HomeCoinsAdapter
import io.tux.wallet.testnet.adapter.RankingAdapter
import io.tux.wallet.testnet.apis.ApiInterface
import io.tux.wallet.testnet.databinding.FragmentHomeBinding
import io.tux.wallet.testnet.interfaces.CoinInterface
import io.tux.wallet.testnet.model.coinResponseModel.Display
import io.tux.wallet.testnet.model.coinResponseModel.Raw
import io.tux.wallet.testnet.model.coins.CoinListModel
import io.tux.wallet.testnet.model.coins.RankModel
import io.tux.wallet.testnet.utils.Constants
import io.tux.wallet.testnet.utils.Constants.COIN
import io.tux.wallet.testnet.utils.NetworkManager
import io.tux.wallet.testnet.utils.SharedPref
import io.tux.wallet.testnet.utils.Utils
import io.tux.wallet.testnet.utils.Utils.sortCoins
import io.tux.wallet.testnet.viewModels.CoinMarketViewModel
import org.json.JSONObject
import java.lang.reflect.Type
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(), View.OnClickListener, CoinInterface {
    lateinit var binding: FragmentHomeBinding
    private var rankList = mutableListOf<RankModel>()
    lateinit var coinsAdapter: HomeCoinsAdapter
    private lateinit var rankingAdapter: RankingAdapter

    @Inject
    lateinit var sharedPref: SharedPref

    @Inject
    lateinit var apiInterface: ApiInterface


    private val viewModel: CoinMarketViewModel by activityViewModels()
    var coinList = ArrayList<CoinListModel>()
    var displayList = mutableListOf<Display>()
    var rawList = mutableListOf<Raw>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)
//        cryptoCoins = CryptoCoins(requireContext(),apiInterface)
        binding.ivSearch.setOnClickListener(this)
        binding.ivScan.setOnClickListener(this)
        binding.linFav.setOnClickListener(this)
        binding.linWallet.setOnClickListener(this)
        binding.constNotification.setOnClickListener(this)
        binding.linWithdraw.setOnClickListener(this)
        binding.linDeposit.setOnClickListener(this)
        binding.btnDeposit.setOnClickListener(this)

        coinList = viewModel.coinList
        callApis()


        binding.swipe.setOnRefreshListener {
            callApis()
            if (binding.swipe.isRefreshing) {
                binding.swipe.isRefreshing = false

            }
        }



        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {

                    1 -> setLooser()
                    2 -> setVolume()
                    else -> setGainer()


                }
                when (tab?.text) {

                    resources.getString(R.string.losers) -> setLooser()
                    resources.getString(R.string.h_vol) -> setVolume()
                    resources.getString(R.string.gainers) -> setGainer()


                }
                return

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                return

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

                return
            }

        })

        binding.tabLayout.getTabAt(0)?.select()
        Log.e("selected_create", "" + binding.tabLayout.getTabAt(0)?.isSelected)

        return binding.root
    }

    private fun callApis() {
        if (NetworkManager.isConnected(binding.root, requireContext())) {
            binding.progressBar.visibility = VISIBLE
            binding.shimmerCoins.visibility = VISIBLE
//            binding.shimmerRank.visibility= VISIBLE
            binding.rvTop.visibility = GONE
//            binding.rvRanking.visibility = GONE
//            viewModel.getErcCoins()
//            viewModel.rankListRequest(USD)
            setObservers()
        }

    }


    private fun setObservers() {
        viewModel.coinModel.observe(
            viewLifecycleOwner, { it ->
                try {

                    val jsonObject = JSONObject(Gson().toJson(it.data))
                    Log.e("cryptoModelObject", jsonObject.toString())
                    val displayListType: Type = object : TypeToken<List<Display>>() {}.type
                    val rawListType: Type = object : TypeToken<List<Raw>>() {}.type
                    displayList =
                        Gson().fromJson(jsonObject.getString(Constants.DISPLAY), displayListType)
                    rawList = Gson().fromJson(jsonObject.getString(Constants.RAW), rawListType)

                    Log.e("displayList", displayList.toString())
                    Log.e("rawList", rawList.toString())
                    for (i in 0 until coinList.size!!) {
                        val coin = coinList[i]
                        for (j in 0 until displayList.size) {
                            if (coin?.symbol == displayList[j].coin && coin?.symbol == rawList[j].coin) {
                                coin.display = displayList[j]
                                coin.raw = rawList[j]
                            }
                        }

                    }
//                   Log.e("coinsListViewModelDISPLAY",coinList[0].display.toString())
//                   Log.e("coinsListViewModelRAW",coinList[0].raw.toString())
                    coinList.sortCoins()
                    Log.e("coinList", coinList.toString())
                    binding.shimmerCoins.stopShimmer()
                    binding.shimmerCoins.visibility = GONE
                    binding.rvTop.visibility = VISIBLE
                    coinsAdapter = HomeCoinsAdapter(requireContext(), coinList, this)
                    binding.rvTop.apply {
                        val linearLayoutManager = LinearLayoutManager(context)
                        linearLayoutManager.orientation = RecyclerView.HORIZONTAL
                        layoutManager = linearLayoutManager
                        adapter = coinsAdapter
                        binding.progressBar.visibility = GONE

                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }

            })

        viewModel.rankModel.observe(viewLifecycleOwner, {
            rankList.clear()
            for (i in it.data.display.indices) {
                rankList.add(RankModel(it.data.display[i], it.data.raw[i]))
            }

            Log.e("rankList", rankList.toString())
            setGainer()


        })


    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_search -> findNavController().navigate(R.id.wallet_search)
            R.id.lin_fav -> Utils.showComingSoonDialog(requireContext())
            R.id.const_notification -> Utils.showComingSoonDialog(requireContext())
            R.id.iv_scan -> gotToScanQR()
            R.id.lin_withdraw -> Utils.showComingSoonDialog(requireContext())
            R.id.lin_wallet -> Utils.showComingSoonDialog(requireContext())
            R.id.lin_deposit -> Utils.showComingSoonDialog(requireContext())
            R.id.btn_deposit -> Utils.showComingSoonDialog(requireContext())
        }

    }


    override fun coinCallBack(position: Int) {
        var args = Bundle()
        if(viewModel.coinList[position].symbol!= Constants.KEY_TUXC) {
            args.putSerializable(COIN, viewModel.coinList[position])
            findNavController().navigate(R.id.nav_coin_detail, args)
        }
    }


    private fun checkLogin(nav_id: Int) {
        if (sharedPref.isLogin()) {
            findNavController().navigate(nav_id)
        } else {
            startActivity(Intent(requireContext(), GuestModeActivity::class.java))
        }
    }


    fun setGainer() {

        rankList.sortByDescending { it.raw.currencies[0].data.CHANGEPCT24HOUR as Comparable<Any> }
        setRankingAdapter()

    }

    fun setLooser() {
        rankList.sortBy { it.raw.currencies[0].data.CHANGEPCT24HOUR as Comparable<Any> }
        setRankingAdapter()

    }

    fun setVolume() {
        rankList.sortByDescending { it.raw.currencies[0].data.VOLUME24HOUR as Comparable<Any> }
        setRankingAdapter()

    }


    private fun setRankingAdapter() {
//    binding.shimmerRank.stopShimmer()
//    binding.shimmerRank.visibility=GONE
        binding.rvRanking.visibility = VISIBLE
        rankingAdapter = RankingAdapter(requireContext(), rankList)
        binding.rvRanking.apply {
            val linearLayoutManager = LinearLayoutManager(context)
            linearLayoutManager.orientation = VERTICAL
            layoutManager = linearLayoutManager
            adapter = rankingAdapter
        }
    }


    private fun gotToScanQR() {
        if (sharedPref.isLogin()) {
            val args = Bundle()
            args.putString(Constants.FROM, HomeFragment::class.java.simpleName)
            findNavController().navigate(R.id.nav_coin_detail, args)
        } else {
            startActivity(Intent(requireContext(), GuestModeActivity::class.java))

        }
    }

}