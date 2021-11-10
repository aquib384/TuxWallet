package io.tux.wallet.testnet.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import io.tux.wallet.testnet.R
import io.tux.wallet.testnet.databinding.DialogMyAddressBinding
import io.tux.wallet.testnet.databinding.FragmentReceiveBinding
import io.tux.wallet.testnet.interfaces.CoinInterface
import io.tux.wallet.testnet.model.coins.CoinListModel
import io.tux.wallet.testnet.utils.Constants.COIN
import io.tux.wallet.testnet.utils.SharedPref
import io.tux.wallet.testnet.utils.Utils
import io.tux.wallet.testnet.viewModels.CoinMarketViewModel
import javax.inject.Inject


@AndroidEntryPoint
class ReceiveFragment : Fragment(), View.OnClickListener, CoinInterface {
    lateinit var binding: FragmentReceiveBinding

    @Inject
    lateinit var sharedPref: SharedPref
    lateinit var coins: List<String>
    private val viewModel: CoinMarketViewModel by activityViewModels()
    lateinit var selectedCoin: CoinListModel
    var coinList = ArrayList<CoinListModel>()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReceiveBinding.inflate(layoutInflater)
        initViews()
        return binding.root
    }

    private fun initViews() {
        coinList = viewModel.coinList
        if (arguments?.containsKey(COIN) == true) {
            val coin = arguments?.get(COIN) as CoinListModel
            for (i in 0 until coinList.size) {
                if (coin.symbol == coinList[i].symbol) {
                    coinList[i].isSelected = true
                    setData(coinList[i])
                }
            }
        } else {
            for (i in 0 until coinList.size) {
                coinList[i].isSelected = i == 0
                if (coinList[i].isSelected) {
                    setData(coinList[i])
                }
            }
        }
        binding.auto1.setOnClickListener(this)
        binding.ivBack.setOnClickListener(this)
        binding.btnCopy.setOnClickListener(this)


    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> findNavController().popBackStack()
            R.id.iv_scan -> gotToQR()
            R.id.auto_1 -> {
                Utils.showCoinsDialog(requireContext(), coinList, this, false)
            }
            R.id.btn_copy -> Utils.copyTextToClipboard(requireContext(), selectedCoin.coin_add)
        }
    }


    private fun setData(element: CoinListModel) {
        try {
            selectedCoin = element
            if (!element.isToken) {
                val ic = resources.getString(R.string.ic_prefix) + element.symbol.lowercase() + resources.getString(
                    R.string.ic_suffix
                )
                binding.auto1.setCompoundDrawablesRelativeWithIntrinsicBounds( Utils.getDrawableRes(requireContext(), ic),0, R.drawable.ic_arrow_drop_down, 0)

            }
            else{

                Utils.setImgDrawableGlide(requireContext(),element,binding.auto1)

            }
            binding.auto1.setText(element.symbol)


            if (element.isToken) {
                var eItem = Utils.getEthData(coinList)
                binding.tvAddress.text = eItem.coin_add.trim()
                binding.ivCode.setImageBitmap(Utils.getQr(requireActivity(), eItem))
            } else {
                binding.tvAddress.text = element.coin_add.trim()
                binding.ivCode.setImageBitmap(Utils.getQr(requireActivity(), element))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    override fun coinCallBack(position: Int) {
        super.coinCallBack(position)
        setData(coinList[position])
        Utils.dismissCoinsDialog()


    }

    private fun gotToQR() {
        var args = Bundle()
        args.putSerializable(COIN, selectedCoin)
        findNavController().navigate(R.id.my_qr, args)
    }


    @SuppressLint("SetTextI18n")
    fun showQRDialog(coin: CoinListModel) {

        val dialog = BottomSheetDialog(requireContext(), R.style.CustomBottomSheetDialogTheme)
        val cBinding: DialogMyAddressBinding =
            DialogMyAddressBinding.inflate(LayoutInflater.from(requireContext()))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(cBinding.root)
        cBinding.ivClose.setOnClickListener {
            dialog.dismiss()
        }
        cBinding.tvPageTitle.text = resources.getString(R.string.receive) + " " + coin.symbol.trim()
        cBinding.tvAddress.text = coin.coin_add.trim()
        cBinding.ivCode.setImageBitmap(Utils.getQr(requireActivity(), coin))
        cBinding.tvCopy.setOnClickListener(this)
        dialog.show()

    }


}