package io.tux.wallet.testnet.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import io.tux.wallet.testnet.R
import io.tux.wallet.testnet.adapter.viewpagers.MarketViewPagerAdapter
import io.tux.wallet.testnet.apis.ApiInterface
import io.tux.wallet.testnet.databinding.FragmentMarketBinding
import io.tux.wallet.testnet.utils.SharedPref
import javax.inject.Inject

@AndroidEntryPoint
class MarketFragment : Fragment() {
    lateinit var binding: FragmentMarketBinding

    @Inject
    lateinit var apiInterface: ApiInterface

    @Inject
    lateinit var sharedPref: SharedPref
    var tabPos = 0
    private val curr = arrayListOf<String>("USD", "JPY", "GBP", "EUR", "CNY")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMarketBinding.inflate(layoutInflater)
//        cList = Utils.getStringCoinsList(coinModel.coinList) as ArrayList<String>
        setupViewPager()
        return binding.root
    }

    private fun setupViewPager() {
        if (sharedPref.isLogin()) {
//            binding.tabLayout.addTab(
//                binding.tabLayout.newTab().setText(resources.getString(R.string.favourite))
//            );
//            cList.add(0, resources.getString(R.string.favourite))
            if (!curr.contains(resources.getString(R.string.favourite)))
                curr.add(0, resources.getString(R.string.favourite))
        }

        val adapter = MarketViewPagerAdapter(this, curr)
        binding.viewpager.adapter = adapter
        binding.viewpager.offscreenPageLimit = 4
        TabLayoutMediator(binding.tabLayout, binding.viewpager) { tab, position ->
            tab.text = curr[position]
            tabPos = position
        }.attach()

    }
}