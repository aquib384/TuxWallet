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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.tux.wallet.testnet.R
import io.tux.wallet.testnet.adapter.SendTransactionAdapter
import io.tux.wallet.testnet.databinding.FragmenTransactionHistryBinding
import io.tux.wallet.testnet.model.coins.CoinListModel
import io.tux.wallet.testnet.model.transaction.ada.adaTrxModel.Data
import io.tux.wallet.testnet.model.transaction.bch.BCHTransactionHistoryModel
import io.tux.wallet.testnet.model.transaction.btc.BTCTransactionHistoryModel
import io.tux.wallet.testnet.model.transaction.trx.TRXTransactionHistoryModel
import io.tux.wallet.testnet.model.transaction.xrp.XRPTransactionHistoryModel
import io.tux.wallet.testnet.utils.Constants
import io.tux.wallet.testnet.utils.NetworkManager
import io.tux.wallet.testnet.utils.Utils
import io.tux.wallet.testnet.viewModels.TransactionViewModel


class SendTransactionHistryFragment : Fragment(), View.OnClickListener {
    lateinit var binding: FragmenTransactionHistryBinding
    lateinit var transactionAdapter: SendTransactionAdapter
    private var txnHistoryList = mutableListOf<Any>()
    private lateinit var coin: CoinListModel
    private val viewModel: TransactionViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmenTransactionHistryBinding.inflate(layoutInflater)
        binding.ivBack.setOnClickListener(this)
        binding.progressBar.visibility = VISIBLE
        coin = arguments?.get(Constants.COIN) as CoinListModel
        if (NetworkManager.isConnected(binding.root, requireContext())) {
            txnHistry()
        }
        return binding.root
    }


    private fun txnHistry() {


        when (coin.symbol) {
            Constants.KEY_ETH -> {
                viewModel.getETHHistory(
                    hashMapOf(
                        Constants.ADDRESS to coin.coin_add,
                        Constants.SORT to ""
                    )
                )
                if (viewModel.ethTxnModel != null) {
                    txnHistoryList.clear()
                    viewModel.ethTxnModel.observe(viewLifecycleOwner, { it ->
                        Log.e("resp", it.data.toString())
                        val list = it.data?.toMutableList()
                        list?.sortByDescending { it.timeStamp }
                        list?.forEach { ethData ->

//                       if (ethData.from.equals(coin.coin_add,true))

                            txnHistoryList.add(ethData)


                        }
                        setTxnAdapter(txnHistoryList)


                    })
                } else {
                    txnHistoryList = ArrayList<Any>()
                    setTxnAdapter(txnHistoryList)
                }
            }
            Constants.KEY_TRX -> {

                viewModel.getTrxHistory(coin)
                if (viewModel.trxSendModel != null) {
                    txnHistoryList.clear()
                    viewModel.trxSendModel.observe(viewLifecycleOwner,
                        { trxTransactionHistoryModel ->
                            Log.e("sendTrxModel", trxTransactionHistoryModel.toString())
                            val list =
                                trxTransactionHistoryModel.data[0].data as ArrayList<TRXTransactionHistoryModel.Data1.Data2>
                            Log.e("lissst", list.toString() + "\nsize :" + list.size)
                            list.sortByDescending { it.timestamp }
                            list.forEach {

//                            if(it.ownerAddress.equals(coin.coin_add,true))
                                txnHistoryList.add(it)
                            }

                            setTxnAdapter(txnHistoryList)

                        })
                } else {
                    txnHistoryList = ArrayList<Any>()
                    setTxnAdapter(txnHistoryList)
                }
            }


            Constants.KEY_XLM -> {

                viewModel.getXlmHistory(coin)
                if (viewModel.xlmSendModel != null) {
                    txnHistoryList.clear()
                    viewModel.xlmSendModel.observe(viewLifecycleOwner, { xlmHistoryModel ->
                        Log.e("sendxlmModel", xlmHistoryModel.toString())
                        val list = xlmHistoryModel.data._embedded.records.toMutableList()
                        Log.e("lissst", list.toString() + "\nsize :" + list.size)
                        list.sortByDescending {
                            it.created_at?.let { it1 ->
                                Utils.timeStampfromCreatedAt(
                                    it1
                                )
                            }
                        }
                        list.forEach {

//                            if(it.source_account.equals(coin.coin_add,true))
                            txnHistoryList.add(it)
                        }

                        setTxnAdapter(txnHistoryList)

                    })

                } else {
                    txnHistoryList = ArrayList<Any>()
                    setTxnAdapter(txnHistoryList)
                }
            }
            Constants.KEY_XRP -> {

                viewModel.getXrpHistory(coin)
                if (viewModel.xrpSendModel != null) {
                    txnHistoryList.clear()
                    viewModel.xrpSendModel.observe(viewLifecycleOwner, { xrpHistoryModel ->
                        Log.e("sendXRPModel", xrpHistoryModel.toString())

                        val list =
                            xrpHistoryModel.data[0].transactions as ArrayList<XRPTransactionHistoryModel.Data.Transaction>
                        list.sortByDescending { Utils.timeStampfromCreatedAt(it.date) }
                        Log.e("lissst", list.toString() + "\nsize :" + list.size)
                        list.forEach {
//                            if (it.account.toString().equals(coin.coin_add,true))
                            txnHistoryList.add(it)
                        }

                        setTxnAdapter(txnHistoryList)

                    })
                } else {
                    txnHistoryList = ArrayList<Any>()
                    setTxnAdapter(txnHistoryList)
                }
            }


            Constants.KEY_BTC -> {

                viewModel.getBTCHistory(coin)

                if (viewModel.btcSendModel != null) {
                    viewModel.btcSendModel.observe(viewLifecycleOwner, { btcHistoryModel ->
                        Log.e("sendBCHModel", btcHistoryModel.toString())
                        txnHistoryList.clear()
                        val list =
                            btcHistoryModel.data.txs as ArrayList<BTCTransactionHistoryModel.Data.Tx>
                        Log.e("lissst", list.toString() + "\nsize :" + list.size)
                        list.sortByDescending { Utils.timeStampfromCreatedAt(it.confirmed) }
                        list.forEach { tx ->
//                            if (tx.addresses[0].equals(coin.coin_add,true)) {
                            txnHistoryList.add(tx)
//                                    }
                        }

                        setTxnAdapter(txnHistoryList)

                    })
                } else {
                    txnHistoryList = ArrayList<Any>()
                    setTxnAdapter(txnHistoryList)
                }
            }

            Constants.KEY_BCH -> {

                viewModel.getBCHHistory(coin)

                if (viewModel.bchSendModel != null) {
                    viewModel.bchSendModel.observe(viewLifecycleOwner, { bchHistoryModel ->
                        Log.e("sendBTCModel", bchHistoryModel.toString())
                        txnHistoryList.clear()
                        val list =
                            bchHistoryModel.data[0].payload as ArrayList<BCHTransactionHistoryModel.Data.Payload>
                        Log.e("lissst", list.toString() + "\nsize :" + list.size)
                        list.sortByDescending { it.timestamp }
                        list.forEach { it ->
                           if (it.txins[0].addresses[0].equals(coin.coin_add,true)) {

                            txnHistoryList.add(it)
                                    }
                        }

                        setTxnAdapter(txnHistoryList)

                    })
                } else {
                    txnHistoryList = ArrayList<Any>()
                    setTxnAdapter(txnHistoryList)
                }
            }



            Constants.KEY_LTC -> {

                viewModel.getLTCHistory(coin)
                if (viewModel.ltcSendModel != null) {
                    viewModel.ltcSendModel.observe(viewLifecycleOwner, { ltcHistoryModel ->
                        Log.e("send:LTCModel", ltcHistoryModel.toString())
                        txnHistoryList.clear()
                        val list =
                            ltcHistoryModel.data.txs as ArrayList<BTCTransactionHistoryModel.Data.Tx>
                        Log.e("lissst", list.toString() + "\nsize :" + list.size)
                        list.sortByDescending { Utils.timeStampfromCreatedAt(it.confirmed) }
                        list.forEach { tx ->
//                            if (tx.addresses[0].equals(coin.coin_add,true))
//                            {
                            txnHistoryList.add(tx)
//                            }

                        }

                        setTxnAdapter(txnHistoryList)

                    })
                } else {
                    txnHistoryList = ArrayList<Any>()
                    setTxnAdapter(txnHistoryList)
                }
            }

            Constants.KEY_ADA -> {
                viewModel.getADAHistory1(coin)

                viewModel.adaTxmodel.observe(viewLifecycleOwner, { adaTxnModel ->
                    Log.e("ada_resp", adaTxnModel.toString())
                    txnHistoryList.clear()
                    val list = adaTxnModel.data as ArrayList<Data>?
                    list?.sortByDescending { Utils.timeStampfromCreatedAt(it.inserted_at.time) }
                    list?.forEach {
                        txnHistoryList.add(it)
                    }
                    setTxnAdapter(txnHistoryList)

                })
            }

            else -> {
                if (coin.isToken) {
                    viewModel.getERC20History(
                        hashMapOf(
                            Constants.ADDRESS to coin.coin_add,
                            Constants.COIN to coin.symbol
                        )
                    )

                    viewModel.ercTxnModel.observe(viewLifecycleOwner, { it ->
                        Log.e("erc20", it.data.toString())
                        txnHistoryList.clear()
                        val list = it.data?.toMutableList()
                        list?.sortByDescending { it.timeStamp }
                        list?.forEach { ethData ->
                            if (ethData.from.equals(coin.coin_add, true))
                                txnHistoryList.add(ethData)
                        }
                        setTxnAdapter(txnHistoryList)


                    })
                }
            }
        }
    }


    private fun setTxnAdapter(list: MutableList<Any>) {
//        list.sortBy{it.timeStamp }
        Log.e("translist", txnHistoryList.toString())
        transactionAdapter = SendTransactionAdapter(requireContext(), list, coin)
        binding.recyclerview.apply {
            var linearLayoutManager = LinearLayoutManager(context)
            linearLayoutManager.orientation = RecyclerView.VERTICAL
            layoutManager = linearLayoutManager
            adapter = transactionAdapter
        }
        binding.progressBar.visibility = GONE
//        binding.shimmerRank.stopShimmer()
//        binding.shimmerRank.visibility =GONE
        binding.recyclerview.visibility = VISIBLE
    }

    override fun onClick(v: View?) {
        findNavController().popBackStack(R.id.navigation_wallet, false)
    }

}