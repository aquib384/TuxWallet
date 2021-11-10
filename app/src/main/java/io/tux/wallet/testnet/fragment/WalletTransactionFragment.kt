package io.tux.wallet.testnet.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import io.tux.wallet.testnet.R
import io.tux.wallet.testnet.adapter.viewpagers.TransactionViewPagerAdapter
import io.tux.wallet.testnet.databinding.FragmentWalletTransactionBinding
import io.tux.wallet.testnet.interfaces.CoinInterface
import io.tux.wallet.testnet.model.coins.CoinListModel
import io.tux.wallet.testnet.utils.Utils
import io.tux.wallet.testnet.viewModels.CoinMarketViewModel


class WalletTransactionFragment : Fragment(), View.OnClickListener, CoinInterface {
    lateinit var binding: FragmentWalletTransactionBinding
    var coinList = ArrayList<CoinListModel>()
    private val coinModel: CoinMarketViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWalletTransactionBinding.inflate(layoutInflater)
        binding.ivBack.setOnClickListener(this)
        setupViewPager()
        coinList = coinModel.coinList
        for (i in 0 until coinList.size) {
            coinList[i].isSelected = i == 0
            if (coinList[i].isSelected) {
                setData(coinList[i])
            }
        }
        if (coinList.isNotEmpty())
            coinModel.selectedCoin.value = coinList[0]
        binding.auto1.setOnClickListener(this)



        return binding.root
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> findNavController().popBackStack()
            R.id.auto_1 -> Utils.showCoinsDialog(requireContext(), coinList, this, false)
        }
    }

    override fun coinCallBack(position: Int) {
        coinModel.selectedCoin.value = coinList[position]
        setData(coinList[position])
        Utils.dismissCoinsDialog()
        coinModel.clearData.value = true
    }


    private fun setData(element: CoinListModel) {
        if (!element.isToken) {
            val ic =
                resources.getString(R.string.ic_prefix) + element.symbol.lowercase() + resources.getString(
                    R.string.ic_suffix
                )
            binding.auto1.setCompoundDrawablesRelativeWithIntrinsicBounds(
                Utils.getDrawableRes(
                    requireContext(),
                    ic
                ), 0, R.drawable.ic_arrow_drop_down, 0
            )

        } else {

            Utils.setImgDrawableGlide(requireContext(), element, binding.auto1)
        }
        binding.auto1.setText(element.symbol)
    }

    private fun setupViewPager() {
        val adapter = TransactionViewPagerAdapter(activity)
        binding.viewpager.adapter = adapter
        binding.viewpager.offscreenPageLimit = 2

        TabLayoutMediator(binding.tabLayout, binding.viewpager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.send)
                }
                1 -> {
                    tab.text = getString(R.string.receive)
                }

            }
        }.attach()
    }

}