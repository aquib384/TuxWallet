package io.tux.wallet.testnet.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.tux.wallet.testnet.R
import io.tux.wallet.testnet.adapter.MarketAdapter
import io.tux.wallet.testnet.databinding.FragmentMarketListBinding
import io.tux.wallet.testnet.model.coinResponseModel.CoinData
import io.tux.wallet.testnet.model.coins.MarketPairModel
import io.tux.wallet.testnet.utils.Constants
import io.tux.wallet.testnet.utils.Constants.CURRENCY
import io.tux.wallet.testnet.utils.Constants.PAIR
import io.tux.wallet.testnet.utils.NetworkManager
import io.tux.wallet.testnet.utils.RecyclerTouchListener
import io.tux.wallet.testnet.viewModels.CoinMarketViewModel

@AndroidEntryPoint
class
MarketListFragment : Fragment(), View.OnClickListener {
    private var marketAdapter: MarketAdapter? = null
    lateinit var binding: FragmentMarketListBinding

    //    private val viewModel: PairExchangeViewModel by viewModels()
    private val viewModel: CoinMarketViewModel by activityViewModels()
    lateinit var currency: String
    private var marketList = ArrayList<MarketPairModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMarketListBinding.inflate(layoutInflater)
        currency = arguments?.getString(CURRENCY)!!

        if (NetworkManager.isConnected(binding.root, requireContext())) {
            when (currency) {
                context?.resources?.getString(R.string.favourite) -> setMarketList()
                else -> callPairsApi()
            }
        }
        setAdapter()

        binding.tvNameVol.setOnClickListener(this)
        binding.tvPrice.setOnClickListener(this)
        binding.tvChange.setOnClickListener(this)

//        binding.swipe.setOnRefreshListener {
//            if(NetworkManager.isConnected(binding.root,requireContext())) {
////                coinName.let { viewModel.coinMarketExchangePairs(it, 3) }
//                viewModel.coinTopPairsRequest(coinName,10)
////                viewModel.topPairModel.observe(viewLifecycleOwner,
////                    { pair ->
////                        Log.e("TopcoinsList",pair.toString())
////                        pair.data.Data.forEach {
////                            viewModel.callCoinsApis(it)
////
////                            Log.e("display",viewModel.displayList.toString())
////                        }
////
////                    }
////                )
//            }
//            if (binding.swipe.isRefreshing) {
//                binding.swipe.isRefreshing = false
//
//            }
//        }
        binding.recyclerview.addOnItemTouchListener(
            RecyclerTouchListener(
                activity,
                binding.recyclerview,
                object : RecyclerTouchListener.OnItemClickListener {
                    override fun onItemClick(view: View?, position: Int) {
                        val args = Bundle()
                        args.putSerializable(PAIR, marketList[position])
                        findNavController().navigate(R.id.nav_market_detail, args)
                    }

                    override fun onLongItemClick(view: View?, position: Int) {}
                }
            ))
        return binding.root
    }

    private fun setAdapter() {
        marketAdapter = MarketAdapter(requireContext(), marketList)
        binding.recyclerview.apply {
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(context)
            adapter = marketAdapter
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.tv_name_vol -> marketList.sortBy {
                it.raw.currencies[0].data.VOLUME24HOUR.toString().toDouble()
            }
            R.id.tv_price -> marketList.sortBy {
                it.raw.currencies[0].data.PRICE.toString().toDouble()
            }
            R.id.tv_change -> marketList.sortBy {
                it.raw.currencies[0].data.CHANGEPCT24HOUR.toString().toDouble()
            }
        }
    }

    private fun callPairsApi() {
        binding.progressBar.visibility = VISIBLE
//        binding.shimmerRank.visibility=VISIBLE
//        binding.recyclerview.visibility = GONE
//        binding.shimmerRank.startShimmer()
        marketList.clear()

        when (currency) {
            Constants.GBP -> viewModel.gbpListModel.observe(viewLifecycleOwner) {
                setData(it.data)
            }
            Constants.CNY -> viewModel.cnyListModel.observe(viewLifecycleOwner) {
                setData(it.data)
            }
            Constants.JPY -> viewModel.jpyListModel.observe(viewLifecycleOwner) {
                setData(it.data)
            }
            Constants.EUR -> viewModel.eurListModel.observe(viewLifecycleOwner) {
                setData(it.data)
            }
            else -> viewModel.rankModel.observe(viewLifecycleOwner) {
                setData(it.data)
            }
        }
    }

    private fun setData(data: CoinData) {
        marketList.clear()
        for (i in data.display.indices) {
            marketList.add(MarketPairModel(data.display[i], data.raw[i]))
        }
        binding.progressBar.visibility = GONE
        marketAdapter?.notifyItemRangeInserted(0, marketList.size - 1)
    }

    private fun setMarketList() {
        marketList.clear()
//        marketAdapter.notifyDataSetChanged()

//        binding.shimmerRank.stopShimmer()
//        binding.shimmerRank.visibility =GONE
        binding.tvNoData.visibility = VISIBLE
        binding.progressBar.visibility = GONE
    }

    companion object {
        fun newInstance(coinName: String): MarketListFragment {
            val args = Bundle()
//            args.putString(COIN, coinName)
            args.putString(CURRENCY, coinName)
            val fragment = MarketListFragment()
            fragment.arguments = args
            return fragment
        }

    }
}