package io.tux.wallet.testnet.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.tux.wallet.testnet.R
import io.tux.wallet.testnet.apis.ApiInterface
import io.tux.wallet.testnet.databinding.FragmentWithdrawBinding
import io.tux.wallet.testnet.utils.Utils
import io.tux.wallet.testnet.viewModels.CoinMarketViewModel
import javax.inject.Inject

@AndroidEntryPoint
class WithdrawFragment : Fragment(), View.OnClickListener {
    lateinit var binding: FragmentWithdrawBinding
    lateinit var coins: List<String>
    private val viewModel: CoinMarketViewModel by activityViewModels()

    @Inject
    lateinit var apiInterface: ApiInterface
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWithdrawBinding.inflate(layoutInflater)
        binding.ivBack.setOnClickListener(this)
//        setArrayAdapter()
        return binding.root
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> findNavController().popBackStack()
        }
    }

//    private fun setArrayAdapter()
//    {
//        cryptoCoins = CryptoCoins(requireContext(),apiInterface)
//        coins = cryptoCoins.getStringCoinsList()
//        Log.e("coins",coins.toString())
//        val adapter = ArrayAdapter(requireContext(),
//            android.R.layout.simple_list_item_1, coins)
//        binding.auto1.setAdapter(adapter)
//
//        binding.auto1.threshold =1
//        binding.auto1.setSelection(0)
//        binding.auto1.setText(coins[0].toString(), false);
//        setData(coins[0].toString())
//        binding.auto1.setOnItemClickListener { parent, view, position, id ->
//
//            val selectedText =  binding.auto1.text.toString()
//            setData(selectedText)
////            Utils.showToast(requireContext(), selectedText)
//        }
//
//
//    }


    private fun setData(s: String) {
        var coinsList = viewModel.coinList
        for (element in coinsList) {
            if (element.symbol == s) {
                val ic =
                    resources.getString(R.string.ic_prefix) + element.symbol.lowercase() + resources.getString(
                        R.string.ic_suffix
                    )
                binding.layCrypto1.setStartIconDrawable(Utils.getDrawableRes(requireContext(), ic))
//        binding.auto1.setCompoundDrawablesWithIntrinsicBounds( Utils.getDrawableRes(requireContext(),coinsList[pos].logo), null, null)
//                binding.auto1.setCompoundDrawablesRelativeWithIntrinsicBounds( 0,0,R.drawable.ic_arrow_drop_down,0)
            }
        }
    }

}