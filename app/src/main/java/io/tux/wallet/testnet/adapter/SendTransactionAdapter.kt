package io.tux.wallet.testnet.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.tux.wallet.testnet.R
import io.tux.wallet.testnet.databinding.ItemSendTransactionBinding
import io.tux.wallet.testnet.model.coins.CoinListModel
import io.tux.wallet.testnet.model.transaction.ada.adaTrxModel.Data
import io.tux.wallet.testnet.model.transaction.bch.BCHTransactionHistoryModel
import io.tux.wallet.testnet.model.transaction.btc.BTCTransactionHistoryModel
import io.tux.wallet.testnet.model.transaction.eth.ERC20HistoryModel
import io.tux.wallet.testnet.model.transaction.eth.ETHTransactionHistoryModel
import io.tux.wallet.testnet.model.transaction.trx.TRXTransactionHistoryModel
import io.tux.wallet.testnet.model.transaction.xlm.XLMTransactionHistoryModel
import io.tux.wallet.testnet.model.transaction.xrp.XRPTransactionHistoryModel
import io.tux.wallet.testnet.utils.Utils
import kotlin.math.pow

class SendTransactionAdapter(
    var context: Context,
    var list: MutableList<Any>,
    var coin: CoinListModel
) :
    RecyclerView.Adapter<SendTransactionAdapter.ViewHolder>() {
    inner class ViewHolder(var binding: ItemSendTransactionBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SendTransactionAdapter.ViewHolder {
        return ViewHolder(
            ItemSendTransactionBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SendTransactionAdapter.ViewHolder, position: Int) {

        var data = list[position]
        var blockHash = ""
        var to = ""
        var from: String? = ""
        var time = ""
        var amount = ""
        holder.binding.tvCoin.text = coin.symbol
        if (!coin.isToken) {
            val ic =
                context.getString(R.string.ic_prefix) + coin.symbol.lowercase() + context.getString(
                    R.string.ic_suffix
                )
            holder.binding.tvCoin.setCompoundDrawablesRelativeWithIntrinsicBounds(
                Utils.getDrawableRes(
                    context,
                    ic
                ), 0, 0, 0
            )
        } else {
            Utils.setImgDrawableGlide(context, coin, holder.binding.tvCoin)

        }


        when (data) {
            is ETHTransactionHistoryModel.Data -> {
                val a = data.value.toDouble() / 10.0.pow(18.0)
                blockHash = data.blockHash.toString()
                to = data.to.toString()
                from = data.from.toString()
                time = Utils.createDatefromTimeStamp(data.timeStamp).toString()
                amount = a.toBigDecimal().toString()
            }
            is ERC20HistoryModel.Data -> {
                val a = data.value.toDouble() / 10.0.pow(18.0)
                blockHash = data.blockHash.toString()
                to = data.to.toString()
                from = data.from.toString()
                time = Utils.createDatefromTimeStamp(data.timeStamp).toString()
                amount = a.toBigDecimal().toString()
            }

            is TRXTransactionHistoryModel.Data1.Data2 -> {
                var a = data.amount.toDouble() / 10.0.pow(6)
                blockHash = data.hash.toString()
                to = data.toAddress.toString()
                from = data.ownerAddress.toString()
                time = Utils.createDatefromTimeStamp(data.timestamp.toString()).toString()
                amount = a.toString()
            }
            is XRPTransactionHistoryModel.Data.Transaction -> {
                var a = data.fee
                blockHash = data.hash.toString()
                from = data.account
                to = ""
                time = Utils.createDatefromTimeStamp(
                    Utils.timeStampfromCreatedAt(data.date).toString()
                )
                amount = a.toBigDecimal().toString()
            }
            is BTCTransactionHistoryModel.Data.Tx -> {
//                if (Utils.checkBtcInputAddress(
////                                        coin.coin_add,
//                        "1DLFxQvexxZbhmPmGDQ11HHieHm1uxF5zc",
//                        data.inputs
//                    ) && Utils.checkBtcOutputAddress(
////                                        coin.coin_add,
//                        "1DLFxQvexxZbhmPmGDQ11HHieHm1uxF5zc",
//                        data.outputs)
//                )
//                {
//                    from = data.outputs[0].addresses[0]
//                    to =  coin.coin_add
//                }
//                else
//                {
//                    to = data.outputs[0].addresses[0]
//                    from = coin.coin_add
//                }
                to = data.outputs?.get(1)?.addresses?.get(0).toString()
                from = data.outputs?.get(0)?.addresses?.get(0).toString()
                val a = data.outputs?.get(0)?.value?.toDouble()?.div((10.0).pow(8))?.toBigDecimal()
                blockHash = data.hash

                time = Utils.createDatefromTimeStamp(
                    Utils.timeStampfromCreatedAt(data.confirmed).toString()
                )
                amount = a.toString()
            }
//            is BTCTransactionHistoryModel.Data.Tx -> {
//                var a = data.total.toDouble().div((10.0).pow(8)).toBigDecimal()
//                blockHash = data.hash.toString()
//                from = data.addresses[0]
//                to = data.addresses[1]
//                time = Utils.createDatefromTimeStamp(
//                    Utils.timeStampfromCreatedAt(data.confirmed).toString()
//                )
//                amount = a.toString()
//            }
            is XLMTransactionHistoryModel.Data.Embedded.Record -> {
//                var a = data.total.toDouble().div((10.0).pow(8)).toBigDecimal()
                blockHash = data.hash.toString()
                from = data.source_account
                to = ""
                time = data.created_at?.let {
                    Utils.createDatefromTimeStamp(
                        Utils.timeStampfromCreatedAt(it).toString()
                    )
                }.toString()
                amount = ""
            }

            is BCHTransactionHistoryModel.Data.Payload -> {
//                var a = data.total.toDouble().div((10.0).pow(8)).toBigDecimal()
                blockHash = data.hash

            }

            is Data -> {
                val a = data.outputs[0].amount.quantity.toString().toDouble().div((10.0).pow(6))
                    .toBigDecimal()
                blockHash = data.id
                from = if (data.inputs[0].address == null)
                    data.inputs[0].id
                else data.inputs[0].address

                to = data.outputs[0].address
                time = Utils.createDatefromTimeStamp(
                    Utils.timeStampfromCreatedAt(data.inserted_at.time).toString()
                )
                amount = a.toString()
            }
        }

        holder.binding.tvTitle.text =
            context.resources.getString(R.string.txn_hash) + " : $blockHash"
        if (to.isEmpty())
            holder.binding.tvTo.visibility = GONE
        else
            holder.binding.tvTo.text = context.resources.getString(R.string.to) + " : $to"
        if (from?.isEmpty() == true)
            holder.binding.tvFrom.visibility = GONE
        else
            holder.binding.tvFrom.text = context.resources.getString(R.string.from) + " : $from"
        if (amount.isEmpty())
            holder.binding.tvAmount.visibility = GONE
        else
            holder.binding.tvAmount.text =
                Utils.getDoubleValueFromString6(amount) + " " + coin.symbol

        holder.binding.tvDesc.text = time

        holder.binding.tvBrowse.setOnClickListener {
            Utils.goToExplorer(context, blockHash, coin)
        }
        holder.binding.tvCopy.setOnClickListener {
            Utils.copyTextToClipboard(context, blockHash)
        }
    }


    override fun getItemCount(): Int {
        return list.size
    }


//    interface TrxInterface {
//        fun trxCallback(position: Int) {}
//    }
}