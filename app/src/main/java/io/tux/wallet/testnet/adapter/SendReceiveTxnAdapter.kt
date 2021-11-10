package io.tux.wallet.testnet.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.tux.wallet.testnet.R
import io.tux.wallet.testnet.databinding.ItemSendReceiveBinding
import io.tux.wallet.testnet.model.transaction.eth.ETHTransactionHistoryModel
import android.graphics.Shader

import android.graphics.LinearGradient
import io.tux.wallet.testnet.model.coins.CoinListModel
import io.tux.wallet.testnet.model.transaction.ada.adaTrxModel.AdaTrxResponse
import io.tux.wallet.testnet.model.transaction.ada.adaTrxModel.Data
import io.tux.wallet.testnet.model.transaction.bch.BCHTransactionHistoryModel
import io.tux.wallet.testnet.model.transaction.btc.BTCTransactionHistoryModel
import io.tux.wallet.testnet.model.transaction.eth.ERC20HistoryModel
import io.tux.wallet.testnet.model.transaction.trx.TRXTransactionHistoryModel
import io.tux.wallet.testnet.model.transaction.xlm.XLMTransactionHistoryModel
import io.tux.wallet.testnet.model.transaction.xrp.XRPTransactionHistoryModel


class SendReceiveTxnAdapter(
    var context: Context,
    var list: MutableList<Any>,
    var coin: CoinListModel,
    var txnCallBack: TxnCallBack
) : RecyclerView.Adapter<SendReceiveTxnAdapter.ViewHolder>() {

    private var mLastClickTime = System.currentTimeMillis()
    private val CLICK_TIME_INTERVAL: Long = 2000

    inner class ViewHolder(var binding: ItemSendReceiveBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            hasStableIds()
            binding.root.setOnClickListener {
                val now = System.currentTimeMillis()
                if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                    return@setOnClickListener
                }
                mLastClickTime = now
                txnCallBack.txnCallBack(list[adapterPosition])
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemSendReceiveBinding.inflate(
                LayoutInflater.from(
                    context
                ), parent, false
            )
        )

    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        val hash = when (data) {
            is ETHTransactionHistoryModel.Data -> data.hash
            is ERC20HistoryModel.Data -> data.hash
            is TRXTransactionHistoryModel.Data1.Data2 -> data.hash
            is XLMTransactionHistoryModel.Data.Embedded.Record -> data.hash
            is XRPTransactionHistoryModel.Data.Transaction -> data.hash
            is BTCTransactionHistoryModel.Data.Tx -> data.hash
            is BCHTransactionHistoryModel.Data -> data.payload[0].hash
            is Data -> data.id
            else -> null
        }

        holder.binding.tvWallet.text = hash.toString()

        val shader = LinearGradient(
            0f,
            0f,
            0f,
            holder.binding.tvDetails.textSize,
            context.resources.getColor(R.color.colorPrimaryPurple, context.resources.newTheme()),
            context.resources.getColor(R.color.colorPrimaryBlue, context.resources.newTheme()),
            Shader.TileMode.CLAMP
        )
        holder.binding.tvDetails.paint.shader = shader



        when (position % 2) {
            0 -> holder.binding.constRank.setBackgroundResource(R.drawable.card_light);
            else -> holder.binding.constRank.setBackgroundResource(R.drawable.card_dark);
        }


    }


    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }
}

interface TxnCallBack {
    fun txnCallBack(data: Any) {}
}
