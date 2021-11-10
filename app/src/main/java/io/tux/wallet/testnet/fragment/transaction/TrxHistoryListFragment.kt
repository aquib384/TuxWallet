package io.tux.wallet.testnet.fragment.transaction

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import io.tux.wallet.testnet.R
import io.tux.wallet.testnet.adapter.SendReceiveTxnAdapter
import io.tux.wallet.testnet.adapter.TxnCallBack
import io.tux.wallet.testnet.databinding.FragmentTrxListBinding
import io.tux.wallet.testnet.model.coins.CoinListModel
import io.tux.wallet.testnet.model.transaction.ada.adaTrxModel.Data
import io.tux.wallet.testnet.model.transaction.bch.BCHTransactionHistoryModel
import io.tux.wallet.testnet.model.transaction.btc.BTCTransactionHistoryModel
import io.tux.wallet.testnet.model.transaction.eth.ERC20HistoryModel
import io.tux.wallet.testnet.model.transaction.eth.ETHTransactionHistoryModel
import io.tux.wallet.testnet.model.transaction.trx.TRXTransactionHistoryModel
import io.tux.wallet.testnet.model.transaction.xlm.XLMTransactionHistoryModel
import io.tux.wallet.testnet.model.transaction.xrp.XRPTransactionHistoryModel
import io.tux.wallet.testnet.utils.Constants
import io.tux.wallet.testnet.utils.Constants.COIN
import io.tux.wallet.testnet.utils.NetworkManager
import io.tux.wallet.testnet.utils.Utils
import io.tux.wallet.testnet.viewModels.TransactionViewModel
import kotlin.math.pow

@AndroidEntryPoint
class TrxHistoryListFragment : Fragment(), TxnCallBack {
    private var modelData: Any? = null
    lateinit var binding: FragmentTrxListBinding
    private val txnViewModel: TransactionViewModel by viewModels()
    lateinit var coin: CoinListModel
    var position = 0
    private var txnHistoryList = mutableListOf<Any>()
    private var rcvHistoryList = mutableListOf<Any>()
    private var sentHistoryList = mutableListOf<Any>()
    private var pendingHistoryList = mutableListOf<Any>()
    private lateinit var txnAdapter: SendReceiveTxnAdapter
    var type = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTrxListBinding.inflate(layoutInflater)
        coin = arguments?.getSerializable(COIN) as CoinListModel
        position = arguments?.getInt("position")!!
        Log.e("trxHistryFrag", coin.symbol + "---" + position)
        if (NetworkManager.isConnected(binding.root, requireContext())) {
            binding.progressBar.visibility = VISIBLE
            txnHistry()
            setObservers()
        }

        type = when (position) {
            0 -> resources.getString(R.string.send)
            1 -> resources.getString(R.string.receive)
            else -> resources.getString(R.string.pending)
        }

        return binding.root
    }


    companion object {
        fun newInstance(coin: CoinListModel, pos: Int): TrxHistoryListFragment {
            val args = Bundle()
            args.putSerializable(COIN, coin)
            args.putInt("position", pos)
            val fragment = TrxHistoryListFragment()
            fragment.arguments = args
            return fragment
        }

    }

    private fun txnHistry() {
        when (coin.symbol) {
            Constants.KEY_ETH -> {
                txnViewModel.getETHHistory(
                    hashMapOf(
                        Constants.ADDRESS to coin.coin_add,
                        Constants.SORT to ""
                    )
                )

            }
            Constants.KEY_XLM -> {
                txnViewModel.getXlmHistory(coin)

            }
            Constants.KEY_TRX -> {
                txnViewModel.getTrxHistory(coin)
            }

            Constants.KEY_XRP -> {
                txnViewModel.getXrpHistory(coin)
            }

            Constants.KEY_BTC -> {
                txnViewModel.getBTCHistory(coin)
            }
            Constants.KEY_LTC -> {
                txnViewModel.getLTCHistory(coin)
            }

            Constants.KEY_ADA -> {
                txnViewModel.getADAHistory1(coin)
            }

            Constants.KEY_BCH -> {
                txnViewModel.getBCHHistory(coin)
            }


            else -> {
//                binding.progressBar.visibility = GONE
////                binding.shimmerRank.stopShimmer()
////                binding.shimmerRank.visibility =GONE
//                binding.tvNoData.visibility = VISIBLE
                if (coin.isToken) {
                    txnViewModel.getERC20History(
                        hashMapOf(
                            Constants.ADDRESS to coin.coin_add,
                            Constants.COIN to coin.symbol
                        )
                    )
                }
            }
        }
    }


    private fun setTxnAdapter(list: MutableList<Any>) {
        binding.progressBar.visibility = GONE
        Log.e("lisrsrsrsrs", list.toString())
        if (list.isEmpty()) {
            binding.tvNoData.visibility = VISIBLE
            binding.recyclerview.visibility = GONE
        } else {
            binding.tvNoData.visibility = GONE
            binding.recyclerview.visibility = VISIBLE
            txnAdapter = SendReceiveTxnAdapter(requireContext(), list, coin, this).apply {
                setHasStableIds(true)
            }
            binding.recyclerview.apply {
                val linearLayoutManager = LinearLayoutManager(context)
                linearLayoutManager.orientation = RecyclerView.VERTICAL
                layoutManager = linearLayoutManager
                adapter = txnAdapter

            }
            txnAdapter.notifyDataSetChanged()
        }

//        binding.shimmerRank.stopShimmer()
//        binding.shimmerRank.visibility =GONE
//        binding.recyclerview.visibility= VISIBLE
    }


    override fun txnCallBack(data: Any) {
        modelData = data
        when (data) {
            is ETHTransactionHistoryModel.Data -> txnViewModel.getSendETHReceipt(data.hash)
            is ERC20HistoryModel.Data -> txnViewModel.getSendERC20Receipt(data.hash)
            is TRXTransactionHistoryModel.Data1.Data2 -> txnViewModel.getSendTRXReceipt(data.hash)
            is XLMTransactionHistoryModel.Data.Embedded.Record -> txnViewModel.getSendXLMReceipt(
                data.hash
            )
            is XRPTransactionHistoryModel.Data.Transaction -> txnViewModel.getSendXRPReceipt(data.hash)
            is BTCTransactionHistoryModel.Data.Tx -> txnViewModel.getSendBTCReceipt(data.hash)
//            is LTCTransactionHistoryModel.Data.Tx -> txnViewModel.getSendLTCReceipt(data.hash)
            is Data -> txnViewModel.getSendADAReceipt(
                coin.adaDetail.adaWalletId,
                data.id
            )
        }
    }


    private fun setObservers() {
        txnHistoryList.clear()

        rcvHistoryList.clear()
        txnViewModel.ethTxnModel.observe(viewLifecycleOwner, { it ->
            sentHistoryList.clear()
            if (it.code == 200 && !it.data.isNullOrEmpty()) {
                txnHistoryList = it.data.toMutableList()
                txnHistoryList.forEach {
                    if (it is ETHTransactionHistoryModel.Data) {
                        Log.e("eth_daata", it.toString())
                        if (coin.coin_add.equals(it.from, true))
                            sentHistoryList.add(it)
                        else
                            rcvHistoryList.add(it)
                    }
                }
            }
            if (position == 0)
                setTxnAdapter(sentHistoryList)
            else
                setTxnAdapter(rcvHistoryList)
        })
//        }
//            else
//            {
//                setTxnAdapter(sentHistoryList)
//              binding.tvNoData.visibility =VISIBLE
//                binding.progressBar.visibility =GONE
//
//            }

        txnViewModel.ercTxnModel.observe(viewLifecycleOwner, { it ->
            if (it.data != null) {
                txnHistoryList = it.data.toMutableList()
                txnHistoryList.forEach {
                    if (it is ERC20HistoryModel.Data) {
                        Log.e("eth_daata", it.toString())
                        if (coin.coin_add.equals(it.to, true))
                            rcvHistoryList.add(it)
                        else
                            sentHistoryList.add(it)
                    }

                }
            } else {
                sentHistoryList = mutableListOf<Any>()
                rcvHistoryList = mutableListOf<Any>()
            }
            if (position == 0)
                setTxnAdapter(sentHistoryList)
            else
                setTxnAdapter(rcvHistoryList)
            Log.e(
                "sendlist_eth",
                "" + sentHistoryList.size + "--" + sentHistoryList.toString()
            )
            Log.e(
                "receivelist_eth",
                "" + rcvHistoryList.size + "--" + rcvHistoryList.toString()
            )

        })

//        if (txnViewModel.xlmSendModel != null) {
        txnViewModel.xlmSendModel.observe(viewLifecycleOwner, { it ->
            Log.e("xlm_resp", it.data.toString())
            txnHistoryList = it.data._embedded.records.toMutableList()
            if (txnHistoryList.isNotEmpty()) {
                txnHistoryList.forEach {
                    if (it is XLMTransactionHistoryModel.Data.Embedded.Record) {
                        Log.e("xlm_daata", it.toString())
//                        if (coin.coin_add.equals(it.source_account.toString(), true))
                        Log.e("xlm_addresss", coin.coin_add + "---" + it.source_account)
                        if (it.source_account.equals(coin.coin_add, true))
                            sentHistoryList.add(it)
                        else
                            rcvHistoryList.add(it)
                    }

                }
            } else {
                sentHistoryList = mutableListOf<Any>()
                rcvHistoryList = mutableListOf<Any>()
                pendingHistoryList = mutableListOf<Any>()
            }

            if (position == 0)
                setTxnAdapter(sentHistoryList)
            else
                setTxnAdapter(rcvHistoryList)
            Log.e(
                "sendlist_eth",
                "" + sentHistoryList.size + "--" + sentHistoryList.toString()
            )
            Log.e(
                "receivelist_eth",
                "" + rcvHistoryList.size + "--" + rcvHistoryList.toString()
            )
        })


        txnViewModel.trxSendModel.observe(viewLifecycleOwner,
            { trxTransactionHistoryModel ->
                Log.e("sendTrxModel", trxTransactionHistoryModel.toString())

                txnHistoryList = trxTransactionHistoryModel.data[0].data.toMutableList()
                Log.e(
                    "lissst",
                    txnHistoryList.toString() + "\nsize :" + txnHistoryList.size
                )

                if (txnHistoryList.isNotEmpty()) {
                    txnHistoryList.forEach {
                        if (it is TRXTransactionHistoryModel.Data1.Data2) {
                            if (coin.coin_add.equals(it.ownerAddress, true))
                                sentHistoryList.add(it)
                            else
                                rcvHistoryList.add(it)
                        }
                    }
                } else {
                    sentHistoryList = mutableListOf<Any>()
                    rcvHistoryList = mutableListOf<Any>()
                    pendingHistoryList = mutableListOf<Any>()
                }
                if (position == 0)
                    setTxnAdapter(sentHistoryList)
                else
                    setTxnAdapter(rcvHistoryList)
                Log.e(
                    "sendlist_trx",
                    "" + sentHistoryList.size + "--" + sentHistoryList.toString()
                )
                Log.e(
                    "receivelist_trx",
                    "" + rcvHistoryList.size + "--" + rcvHistoryList.toString()
                )
            })

        txnViewModel.btcSendModel.observe(viewLifecycleOwner, { it ->
            Log.e("btc_resp", it.data.toString())
            if (it.data.txs != null) {
                txnHistoryList = it.data.txs.toMutableList()
                if (txnHistoryList.isNotEmpty()) {
                    txnHistoryList.forEach {
                        if (it is BTCTransactionHistoryModel.Data.Tx) {
                            Log.e("btc_daata", it.toString())
                            if (Utils.checkBtcInputAddress(
                                    coin.coin_add,
//                                      "1DLFxQvexxZbhmPmGDQ11HHieHm1uxF5zc",
                                    it.inputs
                                )/* && it.outputs?.let { it1 ->
                                    Utils.checkBtcOutputAddress(
                                        coin.coin_add,
                                        //                                        "1DLFxQvexxZbhmPmGDQ11HHieHm1uxF5zc",
                                        it1
                                    )
                                }*/
                            )
                                sentHistoryList.add(it)
                            else
                                rcvHistoryList.add(it)
                        }

                    }
                } else {
                    sentHistoryList = mutableListOf<Any>()
                    rcvHistoryList = mutableListOf<Any>()
                    pendingHistoryList = mutableListOf<Any>()
                }
            } else {
                sentHistoryList = mutableListOf<Any>()
                rcvHistoryList = mutableListOf<Any>()
            }
            if (position == 0)
                setTxnAdapter(sentHistoryList)
            else
                setTxnAdapter(rcvHistoryList)
            Log.e(
                "sendlist_btc",
                "" + sentHistoryList.size + "--" + sentHistoryList.toString()
            )
            Log.e(
                "receivelist_btc",
                "" + rcvHistoryList.size + "--" + rcvHistoryList.toString()
            )

        })

        txnViewModel.bchSendModel.observe(viewLifecycleOwner, { it ->
            Log.e("ada_resp", it.toString())
            if (it.data!=null){
                txnHistoryList= it.data[0].payload.toMutableList()
                txnHistoryList.forEach {
                    if (it is BCHTransactionHistoryModel.Data.Payload) {
                        Log.e("eth_daata", it.toString())
                        var address=it.txins[0].addresses[0]
                        var add: Array<String> = address.split(":").toTypedArray()
                        if ("qp9232aytsstvt347ls8qqnl803xgnx4a5zxxjsgel".equals(add[1], true))
                            sentHistoryList.add(it)
                        else
                            rcvHistoryList.add(it)
                    }
                }

            }
            if (position==0)setTxnAdapter(sentHistoryList)
            else setTxnAdapter(rcvHistoryList)

        })

        txnViewModel.ltcSendModel.observe(viewLifecycleOwner, { it ->
            Log.e("ltc_resp", it.data.toString())
            if (it.data.txs != null) {

                txnHistoryList = it.data.txs.toMutableList()
                if (txnHistoryList.isNotEmpty()) {
                    txnHistoryList.forEach {
                        if (it is BTCTransactionHistoryModel.Data.Tx) {
                            Log.e("ltc_daata", it.toString())
                            if (Utils.checkBtcInputAddress(
                                    coin.coin_add,
                                    it.inputs
                                ) && it.outputs?.let { it1 ->
                                    Utils.checkBtcOutputAddress(
                                        coin.coin_add,
                                        it1
                                    )
                                } == true
                            )
                                sentHistoryList.add(it)
                            else
                                rcvHistoryList.add(it)
                        }

                    }
                } else {
                    sentHistoryList = mutableListOf<Any>()
                    rcvHistoryList = mutableListOf<Any>()
                    pendingHistoryList = mutableListOf<Any>()
                }
            } else {
                sentHistoryList = mutableListOf<Any>()
                rcvHistoryList = mutableListOf<Any>()
            }
            if (position == 0)
                setTxnAdapter(sentHistoryList)
            else
                setTxnAdapter(rcvHistoryList)
            Log.e(
                "sendlist_ltc",
                "" + sentHistoryList.size + "--" + sentHistoryList.toString()
            )
            Log.e(
                "receivelist_ltc",
                "" + rcvHistoryList.size + "--" + rcvHistoryList.toString()
            )
        })

        txnViewModel.adaTxmodel.observe(viewLifecycleOwner, { adaTxnModel ->
            Log.e("ada_resp", adaTxnModel.toString())
            if (adaTxnModel.data?.isNotEmpty() == true) {
                txnHistoryList = adaTxnModel.data.toMutableList()!!
                if (txnHistoryList.isNotEmpty()) {
                    txnHistoryList.forEach { txModel ->
                        if (txModel is Data) {
                            if (txModel.inputs[0].address == null)
                                rcvHistoryList.add(txModel)
                            else
                                sentHistoryList.add(txModel)
                        }
                    }
                } else {
                    sentHistoryList = mutableListOf<Any>()
                    rcvHistoryList = mutableListOf<Any>()
                    pendingHistoryList = mutableListOf<Any>()
                }
                if (position == 0)
                    setTxnAdapter(sentHistoryList)
                else
                    setTxnAdapter(rcvHistoryList)
            }
        })

        txnViewModel.xrpSendModel.observe(viewLifecycleOwner, { it ->
            Log.e("xrp_resp", it.data.toString())
            txnHistoryList = it.data[0].transactions.toMutableList()
            if (txnHistoryList.isNotEmpty()) {
                txnHistoryList.forEach {
                    if (it is XRPTransactionHistoryModel.Data.Transaction) {
                        Log.e("xrp_daata", it.toString())
                        if (coin.coin_add.equals(it.account, true))
                            sentHistoryList.add(it)
                        else
                            rcvHistoryList.add(it)
                    }
                }
            } else {
                sentHistoryList = mutableListOf<Any>()
                rcvHistoryList = mutableListOf<Any>()
                pendingHistoryList = mutableListOf<Any>()
            }
            if (position == 0)
                setTxnAdapter(sentHistoryList)
            else
                setTxnAdapter(rcvHistoryList)
            Log.e(
                "sendlist_xrp",
                "" + sentHistoryList.size + "--" + sentHistoryList.toString()
            )
            Log.e(
                "receivelist_xrp",
                "" + rcvHistoryList.size + "--" + rcvHistoryList.toString()
            )
        })

        txnViewModel.ltcReceiptModel.observe(viewLifecycleOwner, {
            Log.e("hasshreceipt", it.toString())
            Utils.showTxnCompleteDialog(
                requireContext(),
                it.data.addresses[0],
                it.data.addresses[1],
                it.data.fees.toDouble().div((10.0).pow(8)).toBigDecimal().toString(),
                Utils.createDatefromTimeStamp(
                    Utils.timeStampfromCreatedAt(it.data.confirmed).toString()
                ),
                it.data.total.div((10.0).pow(8)).toString(),
                "",
                coin, type
            )
        })

        txnViewModel.btcReceiptModel.observe(viewLifecycleOwner, {
            if (it.data != null) {
                Log.e("hasshreceipt", it.toString())
                var from = ""
                var to = ""
                if (type.equals("send")) {
                    to = it.data.outputs[0].addresses[0]
                    from = coin.coin_add
                } else {
                    from = it.data.outputs[0].addresses[0]
                    to = coin.coin_add
                }
                Utils.showTxnCompleteDialog(
                    requireContext(),
                    to,
                    from,
                    it.data.fees.toDouble().div((10.0).pow(8)).toBigDecimal().toString(),
                    Utils.createDatefromTimeStamp(
                        Utils.timeStampfromCreatedAt(it.data.confirmed).toString()
                    ),
                    it.data.outputs[0].value.div((10.0).pow(8)).toString(),
                    "",
                    coin, type
                )
            }
        })

        txnViewModel.trxReceiptModel.observe(viewLifecycleOwner, {
            var a = (it.data.contractData.amount.toDouble() / 10.0.pow(6)).toDouble()
            Log.e("hasshreceipt", it.toString())
            Utils.showTxnCompleteDialog(
                requireContext(),
                it.data.toAddress,
                it.data.ownerAddress,
                "",
                Utils.createDatefromTimeStamp(it.data.timestamp.toString()) as String,
                a.toString(),
                "",
                coin, type
            )
        })

        txnViewModel.xrpReceiptModel.observe(viewLifecycleOwner, {
            Log.e("hasshreceipt", it.toString())
            Utils.showTxnCompleteDialog(
                requireContext(),
                it.data.raw.tx.Destination,
                it.data.raw.tx.Account,
                it.data.raw.tx.Fee,
                Utils.createDatefromTimeStamp(
                    Utils.timeStampfromCreatedAt(it.data.raw.date).toString()
                ),
                it.data.raw.tx.Amount,
                "",
                coin, type
            )
        })

        txnViewModel.xlmReceiptModel.observe(viewLifecycleOwner, {
            Utils.showTxnCompleteDialog(
                requireContext(),
                "",
                it.data.source_account,
                it.data.fee_charged,
                Utils.createDatefromTimeStamp(
                    Utils.timeStampfromCreatedAt(it.data.created_at).toString()
                ),
                "",
                "",
                coin, type
            )
        })

        txnViewModel.ethReceiptModel.observe(viewLifecycleOwner, {
            Log.e("hasshreceipt", it.toString())
            if (modelData is ETHTransactionHistoryModel.Data) {
                val a =
                    ((modelData as ETHTransactionHistoryModel.Data).value.toDouble() / 10.0.pow(18.0)).toBigDecimal()
                Utils.showTxnCompleteDialog(
                    requireContext(),
                    it.data.to,
                    it.data.from,
                    it.data.gasUsed,
                    Utils.createDatefromTimeStamp((modelData as ETHTransactionHistoryModel.Data).timeStamp),
                    a.toString(),
                    (modelData as ETHTransactionHistoryModel.Data).nonce,
                    coin,
                    type
                )
            }
        })
        txnViewModel.erc20ReceiptModel.observe(viewLifecycleOwner, {
            Log.e("hasshreceipt", it.toString())

            val a =
                ((modelData as ERC20HistoryModel.Data).value.toDouble() / 10.0.pow(18.0)).toBigDecimal()
            Utils.showTxnCompleteDialog(
                requireContext(),
                it.data.to,
                it.data.from,
                it.data.gasUsed,
                Utils.createDatefromTimeStamp((modelData as ERC20HistoryModel.Data).timeStamp),
                a.toString(),
                (modelData as ERC20HistoryModel.Data).nonce,
                coin,
                type
            )
        })

        txnViewModel.adaReceiptModel.observe(viewLifecycleOwner, {
            val a =
                ((modelData as Data).outputs[0].amount.quantity.toDouble() / 10.0.pow(6)).toBigDecimal()
            val frmAddr = if (it.inputs.isNotEmpty())
                if (it.inputs[0].address == null) {
                    it.inputs[0].id
                } else {
                    it.inputs[0].address
                } else null

            Utils.showTxnCompleteDialog(
                requireContext(),
                it.outputs[0].address,
                frmAddr,
                it.fee.quantity.toDouble().div((10.0).pow(6)).toString(),
                Utils.createDatefromTimeStamp(
                    Utils.timeStampfromCreatedAt((modelData as Data).inserted_at.time)
                        .toString()
                ),
                a.toString(),
                "",
                coin,
                type
            )
        })
    }
}