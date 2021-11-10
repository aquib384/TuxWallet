package io.tux.wallet.testnet.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import io.tux.wallet.testnet.R
import io.tux.wallet.testnet.apis.ApiInterface
import io.tux.wallet.testnet.databinding.DialogSendReceiptBinding
import io.tux.wallet.testnet.databinding.FragmentSendBinding
import io.tux.wallet.testnet.interfaces.CoinInterface
import io.tux.wallet.testnet.model.coins.CoinListModel
import io.tux.wallet.testnet.utils.Constants
import io.tux.wallet.testnet.utils.Constants.ADA_PATH
import io.tux.wallet.testnet.utils.Constants.BTC_PATH
import io.tux.wallet.testnet.utils.Constants.COIN
import io.tux.wallet.testnet.utils.Constants.ERC20_PATH
import io.tux.wallet.testnet.utils.Constants.KEY_ADA
import io.tux.wallet.testnet.utils.Constants.KEY_BTC
import io.tux.wallet.testnet.utils.Constants.KEY_ETH
import io.tux.wallet.testnet.utils.Constants.KEY_LTC
import io.tux.wallet.testnet.utils.Constants.KEY_TRX
import io.tux.wallet.testnet.utils.Constants.KEY_XLM
import io.tux.wallet.testnet.utils.Constants.KEY_XRP
import io.tux.wallet.testnet.utils.Constants.LTC_PATH
import io.tux.wallet.testnet.utils.Constants.TRX_PATH
import io.tux.wallet.testnet.utils.Constants.XLM_PATH
import io.tux.wallet.testnet.utils.Constants.XRP_PATH
import io.tux.wallet.testnet.utils.NetworkManager
import io.tux.wallet.testnet.utils.SharedPref
import io.tux.wallet.testnet.utils.Utils
import io.tux.wallet.testnet.utils.Utils.callValidationScreen
import io.tux.wallet.testnet.viewModels.CoinMarketViewModel
import io.tux.wallet.testnet.viewModels.TransactionViewModel
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import kotlin.math.roundToInt

@AndroidEntryPoint
class SendFragment : Fragment(), View.OnClickListener, CoinInterface,
    DialogInterface.OnDismissListener, DialogInterface.OnCancelListener {
    private lateinit var amt: String
    private lateinit var add: String
    lateinit var binding: FragmentSendBinding

    @Inject
    lateinit var apiInterface: ApiInterface

    @Inject
    lateinit var sharedPref: SharedPref
    lateinit var coins: List<String>
    private val viewModel: CoinMarketViewModel by activityViewModels()
    private val txnViewModel: TransactionViewModel by activityViewModels()
    private var selectedCoin: CoinListModel? = null
    private var selectedCoinPath: String = ""
    lateinit var txnDialog: BottomSheetDialog
    var coinList = ArrayList<CoinListModel>()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSendBinding.inflate(layoutInflater)
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

                coinList[i].isSelected = i == 1
                if (coinList[i].isSelected) {
                    setData(coinList[i])
                }
            }
        }
        binding.auto1.setOnClickListener(this)
        binding.ivBack.setOnClickListener(this)
        binding.ivScan.setOnClickListener(this)
        binding.btnConfirm.setOnClickListener(this)

//        binding.tvInstruction.text = HtmlCompat.fromHtml(
//            resources.getString(R.string.withdraw_instructions),
//            FROM_HTML_MODE_COMPACT
//        )
    }

    override fun onResume() {
        super.onResume()
        if (!txnViewModel.sendToAdd.value.isNullOrBlank()) {
            binding.etWallet.setText(txnViewModel.sendToAdd.value.toString())
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> findNavController().popBackStack()
            R.id.iv_scan -> gotToScanQR()
            R.id.btn_confirm -> sendCoin()
            R.id.auto_1 -> Utils.showCoinsDialog(requireContext(), coinList, this, false)
        }
    }


    private fun setData(element: CoinListModel) {
        selectedCoin = element
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
        binding.tvAvailableBal.text =
            resources.getString(R.string.balance) + " : " + element.balance?.toBigDecimal()
        when (element.symbol) {
            KEY_ETH -> {
                selectedCoinPath = ERC20_PATH
            }

            KEY_TRX -> {
                selectedCoinPath = TRX_PATH

            }
            KEY_XLM -> {
                selectedCoinPath = XLM_PATH
            }
            KEY_XRP -> {
                selectedCoinPath = XRP_PATH
            }
            KEY_BTC -> {
                selectedCoinPath = BTC_PATH
            }
            KEY_LTC -> {
                selectedCoinPath = LTC_PATH
            }
            KEY_ADA -> {
                selectedCoinPath = ADA_PATH
            }
            else -> {
                if (element.isToken)
                    selectedCoinPath = ERC20_PATH
            }
        }
    }

    val result =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK)
                selectedCoin?.let {
                    send(
                        toAddr = add,
                        amount = amt,
                        coin = it
                    )
                }
        }

    private fun isValidate(amt: String, add: String): Boolean {
        var isValidate = true
        when {
            amt.isEmpty() -> {
                binding.layAmount1.error = resources.getString(R.string.amount_error)
                isValidate = false
                binding.layAmount1.isErrorEnabled = true
                binding.etAmount1.requestFocus()
                Utils.showToast(requireContext(), resources.getString(R.string.amount_error))
            }
            add.isEmpty() -> {
                isValidate = false
                binding.layWallet.error = resources.getString(R.string.address_error)
                binding.layWallet.isErrorEnabled = true
                binding.etWallet.requestFocus()
                Utils.showToast(requireContext(), resources.getString(R.string.address_error))
            }
        }
        return isValidate
    }

    private fun sendCoin() {
        if (NetworkManager.isConnected(binding.root, requireContext())) {
            amt = binding.etAmount1.text.toString()
            add = binding.etWallet.text.toString().trim()
            if (isValidate(amt, add)) {
                val amount = amt.toDouble()
                if (amount < selectedCoin?.balance?.toDouble()!!) {
                    result.launch(callValidationScreen(requireContext()))
                } else {
                    binding.layAmount1.error = resources.getString(R.string.amount_exceed_error)
                    binding.layAmount1.isErrorEnabled = true
                    binding.etAmount1.requestFocus()
                    Utils.showToast(
                        requireContext(),
                        resources.getString(R.string.amount_exceed_error)
                    )
                }
            }
        }
    }


    override fun coinCallBack(position: Int) {
        super.coinCallBack(position)
        for (i in 0 until viewModel.coinList.size) {
            if (i == position) {
                viewModel.coinList[i].isSelected = true
                setData(viewModel.coinList[i])

            } else {
                viewModel.coinList[i].isSelected = false
            }
        }
        Utils.dismissCoinsDialog()
    }

    private fun gotToScanQR() {
        val args = Bundle()
        if (selectedCoin != null)
            args.putSerializable(COIN, selectedCoin)
        findNavController().navigate(R.id.action_nav_send_to_scan_qr, args)
    }


    fun send(
        toAddr: String,
        amount: String,
        coin: CoinListModel
    ) {
        binding.progressBar.visibility = VISIBLE
        var map: HashMap<String?, Any?>
        when (coin.symbol) {
            KEY_TRX -> {
                map = hashMapOf(
                    Constants.FROMADDRESS to coin.coin_add,
                    Constants.TOADDRESS to toAddr,
                    Constants.PRIVATE_KEY to coin.coin_key,
                    Constants.AMOUNT to amount.toDouble().times(1000000).toBigDecimal()
                        .toBigIntegerExact()
                )
            }

            KEY_XLM -> {
                map = hashMapOf(
                    Constants.FROM_ADDRESS to coin.coin_add,
                    Constants.TO_ADDRESS to toAddr,
                    Constants.SECRET_ADDRESS to coin.secret_key,
                    Constants.AMOUNT to amount
                )
            }
            KEY_XRP -> {
                map = hashMapOf(
                    Constants.SRC_ADDRESS to coin.coin_add,
//                    Constants.SRC_ADDRESS to "rMGknHjUhqqZAanGPkLGru6BrEDX2yGnKB",
                    Constants.DEST_ADDRESS to toAddr,
                    Constants.TOTAG to Utils.getXrpToTag()?.trim(),
                    Constants.SECRET to mapOf(
                        "privateKey" to coin.coin_key,
                        "publicKey" to coin.secret_key
                    ),
//                    Constants.SECRET to mapOf(
//                        "privateKey" to "cda12d0c979eaa8a373974bc9ff931594ff3433fc921048e017de30c1c74b16f",
//                        "publicKey" to "02ddfbbe17041b3ef9151e896557c84389c25e532d705f2fbe059a39e8a6ff1de9"
//                    ),
                    Constants.AMOUNT to amount.toString()
                )
            }

            //0.00008528
            KEY_BTC -> {
                map = hashMapOf(
                    Constants.FROMADDRES to coin.coin_add,
                    Constants.TOADDRESS to toAddr,
                    Constants.FROM_PRIVATE_KEY to coin.coin_key,
                    Constants.BALANCE to amount.toBigDecimal().toPlainString()
                )
            }
            KEY_LTC -> {
                map = hashMapOf(
                    Constants.FROMADDRES to coin.coin_add,
                    Constants.TOADDRESS to toAddr,
                    Constants.TOADDRESS to toAddr,
                    Constants.FROM_PRIVATE_KEY to coin.coin_key,
                    Constants.BALANCE to amount.toBigDecimal()
                        .toPlainString()/*.toDouble().times(100000000).toBigDecimal().toBigIntegerExact()*/
                )
            }
            KEY_ADA -> {
                map = hashMapOf(
                    Constants.WALLET_ID to coin.adaDetail.adaWalletId,
                    Constants.RECEIVER_ADDRESS to toAddr,
                    Constants.PASSPHRASE to coin.adaDetail.adaPhrase,
                    Constants.AMOUNT to amount.toDouble().times(1000000).roundToInt()
                )
            }

            else -> {
                var c_add = ""
                var c_key = ""
                if (coin.isToken) {
                    val eItem = Utils.getEthData(coinList)
                    c_add = eItem.coin_add
                    c_key = eItem.coin_key
                } else {
                    c_add = coin.coin_add
                    c_key = coin.coin_key
                }
                map = hashMapOf(
                    Constants.FROM_ADDR to c_add,
                    Constants.TO_ADDR to toAddr,
                    Constants.PRIVATE_KEY to c_key,
                    Constants.AMOUNT to amount.toBigDecimal(),
                    COIN to coin.symbol
                )
            }
        }
        apiInterface.sendCoins(
            selectedCoinPath, map
        ).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                binding.progressBar.visibility = GONE
                if (response.isSuccessful) {
                    Log.e(
                        "responseCode",
                        response.code()
                            .toString() + "\nresponseBody" + response.body() + "error" + response.errorBody()
                            ?.string()
                    )

                    try {
                        var responseObject =
                            JSONObject(response.body()?.string()).getJSONObject("data")
                        if (responseObject.has("code")) {
                            when (responseObject.getInt("code")) {
                                200 -> {
                                    var hash = ""
//                                    hash = when (coin.symbol) {
//                                        KEY_BTC -> responseObject.getJSONObject("hash")
//                                            .getJSONObject("tx").getString("hash")
//                                        KEY_LTC -> responseObject.getJSONObject("hash")
//                                            .getJSONObject("tx").getString("hash")
//                                        KEY_ADA -> responseObject.getJSONObject("data")
//                                            .getString("id")
//                                        else -> responseObject.getJSONObject("data").getJ
//
//                                    }

//                                    addTxnCounts(coin.symbol)
                                    selectedCoin?.let {
                                        showTxnCompleteDialog(
                                            toAddr,
                                            coin.coin_add,
                                            Utils.getDate(),
                                            amount,
                                            it
                                        )
                                    }
                                }
                                400 -> {
                                    if (responseObject.has("msg")) {
                                        Utils.showToast(
                                            requireContext(),
                                            responseObject.getString("msg")
                                        )
                                    } else {
                                        Utils.showToast(
                                            requireContext(),
                                            responseObject.getString("message")
                                        )
                                    }
                                }


                            }
                        } else if (responseObject.has("responseCode")) {
                            when (responseObject.getInt("responseCode")) {
                                200 -> {
//                                     hash = responseObject.getString("data")
//                                    Log.e("gson", responseObject.toString() + "" + hash)
                                    binding.progressBar.visibility = GONE
//                                    addTxnCounts(coin.symbol)
                                    selectedCoin?.let {
                                        showTxnCompleteDialog(
                                            toAddr,
                                            coin.coin_add,
                                            Utils.getDate(),
                                            amount,
                                            it
                                        )
                                    }
                                }
                                500 -> {
                                    Utils.showToast(
                                        requireContext(),
                                        responseObject.getString("responseMessage")
                                    )
                                }
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()

                    }


                } else {
                    if (response.errorBody() != null)
                        kotlin.runCatching {
                            Toast.makeText(
                                context,
                                JSONObject(response.errorBody()?.string()).get("message")
                                    .toString(),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
            }
        })

    }


    @SuppressLint("SetTextI18n")
    fun showTxnCompleteDialog(
        toAddr: String, fromAddr: String, time: String, amount: String, coin: CoinListModel
    ) {
        txnDialog = BottomSheetDialog(requireContext(), R.style.CustomBottomSheetDialogTheme)

        val cBinding: DialogSendReceiptBinding =
            DialogSendReceiptBinding.inflate(LayoutInflater.from(requireContext()))
        txnDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        txnDialog.setContentView(cBinding.root)

        val a = amount.toBigDecimal()
//            when (coin.symbol) {
////            KEY_ETH -> (amount / 10.0.pow(18.0)).toString().toBigDecimal()
//            KEY_ETH -> amount.toString().toBigDecimal()
////            KEY_BTC -> (amount / 10.0.pow(8.0)).toString().toBigDecimal()
//            KEY_BTC -> amount.toString().toBigDecimal()
//            KEY_LTC -> amount.toString().toBigDecimal()
////            KEY_LTC -> (amount / 10.0.pow(8.0)).toString().toBigDecimal()
//            KEY_TRX -> (amount / 10.0.pow(6.0)).toString().toBigDecimal()
//            else -> amount.toDouble()
//        }

        cBinding.tvAmt.text = a.toString() + " " + coin.symbol
        cBinding.linFee.visibility = GONE
        cBinding.linNonce.visibility = GONE
        cBinding.tvTo.text = toAddr
        cBinding.tvFrom.text = fromAddr
        cBinding.tvPageTitle.text = resources.getString(R.string.send) + " " + coin.symbol
        cBinding.tvDate.text = time
        cBinding.ivClose.setOnClickListener {
            txnDialog.dismiss()
            goToHistory()
        }
        txnDialog.show()
        txnDialog.setOnDismissListener(this)
        txnDialog.setOnCancelListener(this)


    }

    private fun goToHistory() {
        val args = Bundle()
        if (selectedCoin != null)
            args.putSerializable(COIN, selectedCoin)
        findNavController().navigate(R.id.nav_send_hisrty, args)
    }

/*

    fun addTxnCounts(symbol: String) {
        apiInterface.updateTxCount(
            hashMapOf(
                TXSTATS to true, STAKESTATS to false, COINSYMBOL to symbol,
                USER_DETAILS_ID to sharedPref.getWalletId().toString()
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
        }

        )
    }
*/


    override fun onDismiss(p0: DialogInterface?) {
        findNavController().popBackStack(R.id.navigation_wallet, false)
    }

    override fun onCancel(p0: DialogInterface?) {
        findNavController().popBackStack(R.id.navigation_wallet, false)
    }
}
