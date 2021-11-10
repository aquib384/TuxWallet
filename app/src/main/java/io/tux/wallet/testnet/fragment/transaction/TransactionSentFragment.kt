package io.tux.wallet.testnet.fragment.transaction

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import io.tux.wallet.testnet.adapter.SendTransactionAdapter
import io.tux.wallet.testnet.apis.ApiInterface
import io.tux.wallet.testnet.databinding.FragmentTransactionDepositBinding
import io.tux.wallet.testnet.model.coins.CoinListModel
import io.tux.wallet.testnet.model.transaction.ada.adaTrxModel.Data
import io.tux.wallet.testnet.model.transaction.bch.BCHTransactionHistoryModel
import io.tux.wallet.testnet.model.transaction.btc.BTCTransactionHistoryModel
import io.tux.wallet.testnet.model.transaction.trx.TRXTransactionHistoryModel
import io.tux.wallet.testnet.model.transaction.xrp.XRPTransactionHistoryModel
import io.tux.wallet.testnet.utils.Constants
import io.tux.wallet.testnet.utils.Utils
import io.tux.wallet.testnet.viewModels.CoinMarketViewModel
import io.tux.wallet.testnet.viewModels.TransactionViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class TransactionSentFragment : Fragment(), View.OnClickListener {
    private lateinit var coin: CoinListModel
    lateinit var binding: FragmentTransactionDepositBinding
    private var trxList = ArrayList<Any>()
    private var transactionAdapter: SendTransactionAdapter? = null

    @Inject
    lateinit var apiInterface: ApiInterface

    var coinList = ArrayList<CoinListModel>()
    private val viewModel: TransactionViewModel by activityViewModels()
    private val coinModel: CoinMarketViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTransactionDepositBinding.inflate(layoutInflater)
        coinList = coinModel.coinList

        setObservers()
        return binding.root
    }


    override fun onClick(v: View?) {

    }


    private fun txnHistry(coin: CoinListModel) {
//        binding.progressBar.visibility = VISIBLE
        this.coin = coin
        transactionAdapter = SendTransactionAdapter(requireContext(), trxList, coin)
        binding.recyclerview.apply {
            val linearLayoutManager = LinearLayoutManager(context)
            linearLayoutManager.orientation = RecyclerView.VERTICAL
            layoutManager = linearLayoutManager
            adapter = transactionAdapter
        }
        try {
            when (coin.symbol) {
                Constants.KEY_ETH -> {
                    viewModel.getETHHistory(
                        hashMapOf(
                            Constants.ADDRESS to coin.coin_add,
                            Constants.SORT to ""
                        )
                    )
                }
                Constants.KEY_TRX -> {
                    viewModel.getTrxHistory(coin)
                }

                Constants.KEY_XLM -> {
                    viewModel.getXlmHistory(coin)
                }
                Constants.KEY_XRP -> {
                    viewModel.getXrpHistory(coin)
                }


                Constants.KEY_BTC -> {
                    viewModel.getBTCHistory(coin)
                }

                Constants.KEY_BCH -> {
                    viewModel.getBCHHistory(coin)
                }


                Constants.KEY_LTC -> {
                    viewModel.getLTCHistory(coin)
                }

                Constants.KEY_ADA -> {
                    viewModel.getADAHistory1(coin)
                }
                else -> {
                    if (coin.isToken) {
                        viewModel.getERC20History(
                            hashMapOf(
                                Constants.ADDRESS to coin.coin_add,
                                Constants.COIN to coin.symbol
                            )
                        )
                    } else {

                        trxList = ArrayList()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setTxnAdapter(list: MutableList<Any>, coin: CoinListModel) {
//        list.sortBy{it.timeStamp }
        Log.e("translist", list.size.toString() + "\n" + list.toString())
        if (list.isEmpty()) {
            binding.tvNoData.visibility = View.VISIBLE
            binding.recyclerview.visibility = GONE
        } else {
            binding.tvNoData.visibility = View.GONE
            binding.recyclerview.visibility = VISIBLE
            transactionAdapter?.notifyDataSetChanged()
        }

        binding.progressBar.visibility = GONE
//        binding.shimmerRank.stopShimmer()
//        binding.shimmerRank.visibility =GONE

//        transactionAdapter?.notifyItemRangeInserted(0, trxList.size - 1)
    }

    private fun setObservers() {
        coinModel.clearData.observe(viewLifecycleOwner) {
            trxList.clear()
            transactionAdapter?.itemCount?.let { it1 ->
                transactionAdapter?.notifyItemRangeRemoved(
                    0,
                    it1
                )
            }
        }
        viewModel.ercTxnModel.observe(viewLifecycleOwner, { it ->
            Log.e("erc20", it.data.toString())
            val list = it.data?.toMutableList()
            trxList.clear()
            if (list.isNullOrEmpty()) {
                setTxnAdapter(trxList, coin)
            } else {
                list.sortByDescending { it.timeStamp }
                CoroutineScope(Main).launch {
                    list.forEach { ethData ->
                        if (ethData.from.equals(coin.coin_add, true)) {
                            trxList.add(ethData)
                        }
                    }
                    withContext(Main) {
                        setTxnAdapter(trxList, coin)
                    }
                }
            }
        })

        coinModel.selectedCoin.observe(viewLifecycleOwner) {
            txnHistry(it)
        }

        viewModel.adaTxmodel.observe(viewLifecycleOwner, { adaTxnModel ->
            trxList.clear()
            val list =
                adaTxnModel.data as ArrayList<Data>?
            if (list.isNullOrEmpty()) {
                setTxnAdapter(trxList, coin)
            } else {
                list.sortByDescending { Utils.timeStampfromCreatedAt(it.inserted_at.time) }
                CoroutineScope(Main).launch {
                    for (i in 0 until list.size) {
                        if (list[i].inputs[0].address != null) {
                            trxList.add(list[i])
                        }
                    }
                    withContext(Main) {
                        setTxnAdapter(trxList, coin)
                    }
                }
            }
        })

        viewModel.ltcSendModel.observe(viewLifecycleOwner, { ltcHistoryModel ->
            Log.e("send:LTCModel", ltcHistoryModel.toString())
            trxList.clear()
            val list =
                ltcHistoryModel.data.txs as ArrayList<BTCTransactionHistoryModel.Data.Tx>?
            if (list.isNullOrEmpty()) {
                setTxnAdapter(trxList, coin)
            } else {
                list.sortByDescending { Utils.timeStampfromCreatedAt(it.confirmed) }
                CoroutineScope(Main).launch {
                    list.forEach { tx ->
                        if (!tx.inputs.let {
                                Utils.checkBtcInputAddress(
                                    coin.coin_add,
                                    //                                "1DLFxQvexxZbhmPmGDQ11HHieHm1uxF5zc",
                                    it
                                )
                            }!! && tx.outputs?.let {
                                Utils.checkBtcOutputAddress(
                                    coin.coin_add,
                                    it
                                )
                            } == true
                        )
                            trxList.add(tx)
//                         }
                    }
                    withContext(Main) {
                        setTxnAdapter(trxList, coin)
                    }
                }
            }
        })
        viewModel.btcSendModel.observe(viewLifecycleOwner, { btcHistoryModel ->
            val list =
                btcHistoryModel.data.txs as ArrayList<BTCTransactionHistoryModel.Data.Tx>?
            trxList.clear()
            if (list.isNullOrEmpty()) {
                setTxnAdapter(trxList, coin)
            } else {
//                list.sortByDescending { Utils.timeStampfromCreatedAt(it.confirmed) }
                CoroutineScope(Main).launch {
                    list.forEach { tx ->
                        if (tx.inputs.isNotEmpty() && tx.inputs[0].addresses?.isNotEmpty() == true)
                            if (Utils.checkBtcInputAddress(
                                    coin.coin_add,
                                    tx.inputs
                                )
                            ) {
                                trxList.add(tx)
                                Log.i("TAG", "setObservers: ${tx.inputs[0]}")
                            }
                    }
                    withContext(Main) {
                        setTxnAdapter(trxList, coin)
                    }
                }
            }
        })

        viewModel.bchSendModel.observe(viewLifecycleOwner, { btcHistoryModel ->
            val list =
                btcHistoryModel.data[0].payload[0].hash as ArrayList<BCHTransactionHistoryModel.Data.Payload>?
            trxList.clear()



        })
        viewModel.xrpSendModel.observe(viewLifecycleOwner, { xrpHistoryModel ->
            trxList.clear()
            if (xrpHistoryModel.data[0].transactions.isNotEmpty()) {
                val list =
                    xrpHistoryModel.data[0].transactions as ArrayList<XRPTransactionHistoryModel.Data.Transaction>
                list.sortByDescending { Utils.timeStampfromCreatedAt(it.date) }
                CoroutineScope(Main).launch {
                    list.forEach {
                        if (it.account.equals(coin.coin_add, true))
                            trxList.add(it)
                    }
                    withContext(Main) {
                        setTxnAdapter(trxList, coin)
                    }
                }
            } else {
                setTxnAdapter(trxList, coin)
            }
        })

        viewModel.xlmSendModel.observe(viewLifecycleOwner, { xlmHistoryModel ->
            val list = xlmHistoryModel.data._embedded.records.toMutableList()
            trxList.clear()
            if (list.isNullOrEmpty()) {
                setTxnAdapter(trxList, coin)
            } else {
                list.sortByDescending { it.created_at?.let { it1 -> Utils.timeStampfromCreatedAt(it1) } }
                CoroutineScope(Main).launch {
                    list.forEach {
                        if (it.source_account.equals(coin.coin_add, true))
                            trxList.add(it)
                    }
                    withContext(Main) {
                        setTxnAdapter(trxList, coin)
                    }
                }
            }
        })

        viewModel.trxSendModel.observe(viewLifecycleOwner, { trxTransactionHistoryModel ->
            val list =
                trxTransactionHistoryModel.data[0].data as ArrayList<TRXTransactionHistoryModel.Data1.Data2>
            trxList.clear()
            if (list.isNullOrEmpty()) {
                setTxnAdapter(trxList, coin)
            } else {
                list.sortByDescending { it.timestamp }
                CoroutineScope(Main).launch {
                    list.forEach {
                        if (!it.toAddress.equals(coin.coin_add, true))
                            trxList.add(it)
                    }
                    withContext(Main) {
                        setTxnAdapter(trxList, coin)
                    }
                }
            }
        })
        viewModel.ethTxnModel.observe(viewLifecycleOwner, { it ->
            val list = it.data as ArrayList?
            trxList.clear()
            if (list.isNullOrEmpty()) {
                setTxnAdapter(trxList, coin)
            } else {
                list.sortByDescending { it.timeStamp }
                CoroutineScope(Main).launch {
                    list.forEach { ethData ->
                        if (ethData.from.equals(coin.coin_add, true)) {
                            trxList.add(ethData)
                        }
                    }
                    withContext(Main) {
                        setTxnAdapter(trxList, coin)
                    }
                }
            }
        })

        viewModel.ercTxnModel.observe(viewLifecycleOwner, { it ->
            kotlin.runCatching {
                val list = it.data?.toMutableList()
                trxList.clear()
                if (list.isNullOrEmpty()) {
                    setTxnAdapter(trxList, coin)
                } else {
                    list.sortByDescending { it.timeStamp }
                    CoroutineScope(Main).launch {
                        list.forEach { ethData ->
                            if (ethData.from.equals(coin.coin_add, true)) {
                                trxList.add(ethData)
                            }
                        }
                        withContext(Main) {
                            setTxnAdapter(trxList, coin)
                        }
                    }
                }
            }
        })

//        } else {
//            trxList = ArrayList()
//            setTxnAdapter(trxList, coin)
//        }


    }


}