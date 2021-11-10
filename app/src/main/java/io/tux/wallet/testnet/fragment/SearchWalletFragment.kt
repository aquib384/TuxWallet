package io.tux.wallet.testnet.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import io.tux.wallet.testnet.R
import io.tux.wallet.testnet.adapter.SearchCoinsAdapter
import io.tux.wallet.testnet.databinding.FragmentSearchWalletBinding
import io.tux.wallet.testnet.interfaces.CoinInterface
import io.tux.wallet.testnet.model.coinResponseModel.Display
import io.tux.wallet.testnet.model.coinResponseModel.Raw
import io.tux.wallet.testnet.model.coins.CoinListModel
import io.tux.wallet.testnet.utils.Constants
import io.tux.wallet.testnet.utils.SharedPref
import io.tux.wallet.testnet.utils.Utils.sortCoins
import io.tux.wallet.testnet.viewModels.CoinMarketViewModel
import org.json.JSONObject
import java.lang.reflect.Type
import javax.inject.Inject


@AndroidEntryPoint
class SearchWalletFragment : Fragment(), View.OnClickListener, CoinInterface {
    lateinit var binding: FragmentSearchWalletBinding
    lateinit var searchCoinsAdapter: SearchCoinsAdapter

    @Inject
    lateinit var sharedPref: SharedPref
    private val viewModel: CoinMarketViewModel by activityViewModels()
    var coinList = ArrayList<CoinListModel>()
    var displayList = mutableListOf<Display>()
    var rawList = mutableListOf<Raw>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSearchWalletBinding.inflate(layoutInflater)
        coinList = viewModel.coinList

        setCoinsAdapter()
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText?.isNotEmpty() == true) {
                    filter(newText)
                } else coinList.let { searchCoinsAdapter.updateList(it) }
                return true
            }
        })
        binding.ivCancel.setOnClickListener(this)
        binding.ivBack.setOnClickListener(this)

        return binding.root
    }


    fun filter(text: String?) {
        val temp: ArrayList<CoinListModel> = ArrayList()
        for (country in coinList) {
            if (text?.let { country.coin_name.startsWith(it, true) } == true ||
                text?.let { country.symbol.startsWith(it, true) } == true
            ) {
                temp.add(country)
            }
        }
        searchCoinsAdapter.updateList(temp)
    }


    private fun setCoinsAdapter() {

        viewModel.coinModel.observe(
            viewLifecycleOwner, {
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

                    searchCoinsAdapter = SearchCoinsAdapter(requireContext(), coinList, this)
                    binding.recyclerview.apply {
                        layoutManager = LinearLayoutManager(context)
                        adapter = searchCoinsAdapter
                        setHasFixedSize(true)
//                        binding.shimmerWallet.stopShimmer()
//                        binding.shimmerWallet.visibility = GONE
                        binding.recyclerview.visibility = VISIBLE
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            })


    }


    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.iv_back -> findNavController().popBackStack()
            R.id.iv_cancel -> {
                binding.searchView.setQuery("", true)
            }
        }
    }

    override fun coinCallBack(position: Int) {
        var args = Bundle()
        var filteredList = searchCoinsAdapter.getFilteredList()
        if (filteredList[position].symbol != Constants.KEY_TUXC) {
            args.putSerializable("coin", filteredList[position])
            findNavController().navigate(R.id.nav_coin_detail, args)
        }
    }


}