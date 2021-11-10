package io.tux.wallet.testnet.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import io.tux.wallet.testnet.R
import io.tux.wallet.testnet.databinding.FragmentExchangeBinding
import io.tux.wallet.testnet.interfaces.CoinInterface
import io.tux.wallet.testnet.utils.Utils
import io.tux.wallet.testnet.viewModels.CoinMarketViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.tux.wallet.testnet.model.coins.CoinListModel

@AndroidEntryPoint
class ExchangeFragment : Fragment(), View.OnClickListener, CoinInterface {
lateinit var binding:FragmentExchangeBinding
//    @Inject
//    lateinit var cryptoCoins : CryptoCoins
    private var flag =0
    private val viewModel: CoinMarketViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentExchangeBinding.inflate(layoutInflater)
        binding.ivBack.setOnClickListener(this)
        binding.cardCrypto1.setOnClickListener(this)
        binding.tvCrypto1.setOnClickListener(this)
        binding.cryptoIcon1.setOnClickListener(this)
        binding.cardCrypto2.setOnClickListener(this)
        binding.tvCrypto2.setOnClickListener(this)
        binding.cryptoIcon2.setOnClickListener(this)
        binding.etAmount1.isEnabled
        binding.etAmount2.isInEditMode
   setData(viewModel.coinList[0],0) 
   setData(viewModel.coinList[1],1) 
       
       
        return binding.root
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.iv_back -> findNavController().popBackStack()
            R.id.crypto_icon1 -> {
                flag = 0
               Utils.showCoinsDialog(requireContext(), viewModel.coinList , this,false)
            }
            R.id.tv_crypto1 -> {
                flag = 0
                Utils.showCoinsDialog(requireContext(), viewModel.coinList , this,false)
            }
            R.id.card_crypto1 -> {
                flag = 0
               Utils.showCoinsDialog(requireContext(), viewModel.coinList , this,false)
            }
            R.id.card_crypto2 -> {
                flag = 1
               Utils.showCoinsDialog(requireContext(), viewModel.coinList , this,false)
            }
            R.id.tv_crypto2 -> {
                flag = 1
               Utils.showCoinsDialog(requireContext(), viewModel.coinList , this,false)
            }
            R.id.crypto_icon2 -> {
                flag = 1
               Utils.showCoinsDialog(requireContext(), viewModel.coinList , this,false)
            }
        }
    }

    override fun coinCallBack(position: Int) {
        super.coinCallBack(position)
        setData(viewModel.coinList[position],flag)
        Utils.dismissCoinsDialog()


    }

    private fun setData(element: CoinListModel, flag : Int)
    {
        val ic =resources.getString(R.string.ic_prefix)+element.symbol.lowercase()+resources.getString(R.string.ic_suffix)
                when(flag) {
                    0 -> {

                        binding.cryptoIcon1.setImageResource( Utils.getDrawableRes(requireContext(),ic ))
                        binding.tvCrypto1.text = element.symbol
                        binding.etAmount1.setText( element.display.currencies[0].data.PRICE.toString())

                    }
                    else->
                    {
                        binding.cryptoIcon2.setImageResource( Utils.getDrawableRes(requireContext(),ic ))
                        binding.tvCrypto2.text = element.symbol
                        binding.etAmount2.setText( element.display.currencies[0].data.PRICE.toString())
                    }


                }
            }


}